package hr.clientreferraltrackingsystem.controller.user.helper;

import hr.clientreferraltrackingsystem.controller.user.dto.ReferredClientsFormData;
import hr.clientreferraltrackingsystem.enumeration.ReferralStatus;
import hr.clientreferraltrackingsystem.exception.EmptyFieldsException;
import hr.clientreferraltrackingsystem.exception.InvalidEmailException;
import hr.clientreferraltrackingsystem.model.Referral;
import hr.clientreferraltrackingsystem.model.Client;
import hr.clientreferraltrackingsystem.repository.database.ReferralDatabaseRepository;
import hr.clientreferraltrackingsystem.repository.database.ClientsDatabaseRepository;
import hr.clientreferraltrackingsystem.service.SessionManager;
import hr.clientreferraltrackingsystem.utils.InputValidator;
import hr.clientreferraltrackingsystem.utils.Message;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class UserDashboardClientsHelper {
    private static final Logger log = LoggerFactory.getLogger(UserDashboardClientsHelper.class);

    private UserDashboardClientsHelper() {}

    public static void handleSaveOrEdit(
            ReferredClientsFormData clientFormData,
            ClientsDatabaseRepository clientsDatabaseRepository,
            Client selectedClient
    ) {

        boolean confirmed = Message.showConfirmation(
                "Confirm Delete",
                null,
                "Are you sure you want to change client?"
        );

        if (confirmed) {
            if (!validateForm(clientFormData)) return;


            if (selectedClient != null) {
                Client currClient = selectedClient;

                InputValidator.isDataEdited("Client - edit: firstName", currClient.getFirstName(), clientFormData.firstName());
                InputValidator.isDataEdited("Client - edit: lastName", currClient.getLastName(), clientFormData.lastName());
                InputValidator.isDataEdited("Client - edit: email", currClient.getEmail(), clientFormData.email());
                InputValidator.isDataEdited("Client - edit: phoneNumber", currClient.getPhoneNumber(), clientFormData.phoneNumber());

                Client updatedClient = new Client(selectedClient.getId(), clientFormData.firstName(),
                        clientFormData.lastName(), clientFormData.email(), clientFormData.phoneNumber(), SessionManager.getInstance().getLoggedUser());


                clientsDatabaseRepository.update(updatedClient, SessionManager.getInstance().getLoggedUser().getId());
                Message.showAlert(Alert.AlertType.INFORMATION, "Update success",
                        null, "Client updated successfully!");
            } else {

                InputValidator.isDataEdited("Client - insert: firstName", "None", clientFormData.firstName());
                InputValidator.isDataEdited("Client - insert: lastName", "None", clientFormData.lastName());
                InputValidator.isDataEdited("Client - insert: email", "None", clientFormData.email());
                InputValidator.isDataEdited("Client - insert: phoneNumber", "None", clientFormData.phoneNumber());
                Client newClient = new Client(clientFormData.firstName(),
                        clientFormData.lastName(), clientFormData.email(), clientFormData.phoneNumber(), SessionManager.getInstance().getLoggedUser());


                clientsDatabaseRepository.save(newClient, SessionManager.getInstance().getLoggedUser().getId());
                Message.showAlert(Alert.AlertType.INFORMATION, "Adding successful",
                        null, "Client added successfully!");
            }
        }
    }

    public static void handleRecommendClient(ReferralDatabaseRepository referralDatabaseRepository,
                                             ClientsDatabaseRepository clientsDatabaseRepository,
                                             Client selectedClient,
                                             Runnable afterRecommedation) {
        if (selectedClient != null) {
            InputValidator.isDataEdited("Referral - insert: client", "None", selectedClient.getFullName());
            InputValidator.isDataEdited("Referral - insert: date", "None", LocalDateTime.now());
            InputValidator.isDataEdited("Referral - insert: status", "None", ReferralStatus.PENDING);
            Referral newReferral = new Referral(
                    SessionManager.getInstance().getLoggedUser(), selectedClient,
                    LocalDateTime.now(), ReferralStatus.PENDING
            );

            selectedClient.setCurrentlyRecommended(true);
            clientsDatabaseRepository.update(selectedClient, SessionManager.getInstance().getLoggedUser().getId());

            referralDatabaseRepository.save(newReferral);

            Message.showAlert(Alert.AlertType.INFORMATION, "Recommendation successful",
                    null, "You successfully recommend client " + selectedClient.getFirstName() + "!");

            afterRecommedation.run();
        }

    }


    public static void handleDelete(Client client, ClientsDatabaseRepository clientDatabaseRepository,
                                    Runnable afterDelete) {
        boolean confirmed = Message.showConfirmation(
                "Confirm Delete",
                null,
                "Are you sure you want to delete client " + client.getFirstName() + "?"
        );

        InputValidator.isDataEdited("Client - delete: firstName", client.getFirstName(), "none");
        InputValidator.isDataEdited("Client - delete: lastName", client.getLastName(), "none");
        InputValidator.isDataEdited("Client - delete: email", client.getEmail(), "none");
        InputValidator.isDataEdited("Client - delete: phoneNumber", client.getPhoneNumber(), "none");

        if (confirmed) {
            clientDatabaseRepository.delete(client.getId());
            afterDelete.run();
        }

    }

    private static boolean validateForm(ReferredClientsFormData referredClientsFormData) {
        try {
            isFormEmpty(referredClientsFormData);
            if (!referredClientsFormData.email().isEmpty()) {
                InputValidator.emailValidator(referredClientsFormData.email(), "Invalid e-mail address!");
            }
            return true;
        } catch (InvalidEmailException e) {
            log.error(e.getMessage(), "E-mail address is invalid!");
            Message.showAlert(Alert.AlertType.ERROR, "Invalid!",
                    null, "E-mail address is invalid!");
            return false;
        } catch (EmptyFieldsException e) {
            log.error(e.getMessage(), "Empty fields!");
            Message.showAlert(Alert.AlertType.ERROR, "Required fields are empty!",
                    null, "All fields are required!");
            return false;
        }
    }

    private static void isFormEmpty(ReferredClientsFormData formData) {
        if (formData.firstName().isEmpty() || formData.lastName().isEmpty() || formData.email().isEmpty()
                || formData.phoneNumber().isEmpty()) {
            throw new EmptyFieldsException("All fields are required!");
        }
    }
}
