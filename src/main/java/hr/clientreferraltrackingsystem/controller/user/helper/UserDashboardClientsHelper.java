package hr.clientreferraltrackingsystem.controller.user.helper;

import hr.clientreferraltrackingsystem.controller.user.dto.ReferredClientsFormData;
import hr.clientreferraltrackingsystem.enumeration.ReferralStatus;
import hr.clientreferraltrackingsystem.exception.EmptyFieldsException;
import hr.clientreferraltrackingsystem.exception.InvalidEmailException;
import hr.clientreferraltrackingsystem.generics.PairData;
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
import java.util.List;

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

                InputValidator.serializeSave("Client - edit: firstName", currClient.getFirstName(), clientFormData.firstName());
                InputValidator.serializeSave("Client - edit: lastName", currClient.getLastName(), clientFormData.lastName());
                InputValidator.serializeSave("Client - edit: email", currClient.getEmail(), clientFormData.email());
                InputValidator.serializeSave("Client - edit: phoneNumber", currClient.getPhoneNumber(), clientFormData.phoneNumber());

                Client updatedClient = new Client(selectedClient.getId(), clientFormData.firstName(),
                        clientFormData.lastName(), clientFormData.email(), clientFormData.phoneNumber(), SessionManager.instance.getLoggedUser());


                clientsDatabaseRepository.update(updatedClient, SessionManager.instance.getLoggedUser().getId());
                Message.showAlert(Alert.AlertType.INFORMATION, "Update success",
                        null, "Client updated successfully!");
            } else {

                InputValidator.serializeSave("Client - insert: firstName", "None", clientFormData.firstName());
                InputValidator.serializeSave("Client - insert: lastName", "None", clientFormData.lastName());
                InputValidator.serializeSave("Client - insert: email", "None", clientFormData.email());
                InputValidator.serializeSave("Client - insert: phoneNumber", "None", clientFormData.phoneNumber());
                Client newClient = new Client(clientFormData.firstName(),
                        clientFormData.lastName(), clientFormData.email(), clientFormData.phoneNumber(), SessionManager.instance.getLoggedUser());


                clientsDatabaseRepository.save(newClient, SessionManager.instance.getLoggedUser().getId());
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
            InputValidator.serializeSave("Referral - insert: client", "None", selectedClient.getFullName());
            InputValidator.serializeSave("Referral - insert: date", "None", LocalDateTime.now());
            InputValidator.serializeSave("Referral - insert: status", "None", ReferralStatus.PENDING);
            Referral newReferral = new Referral(
                    SessionManager.instance.getLoggedUser(), selectedClient,
                    LocalDateTime.now(), ReferralStatus.PENDING
            );

            selectedClient.setCurrentlyRecommended(true);
            clientsDatabaseRepository.update(selectedClient, SessionManager.instance.getLoggedUser().getId());

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

        InputValidator.serializeSave("Client - delete: firstName", client.getFirstName(), "none");
        InputValidator.serializeSave("Client - delete: lastName", client.getLastName(), "none");
        InputValidator.serializeSave("Client - delete: email", client.getEmail(), "none");
        InputValidator.serializeSave("Client - delete: phoneNumber", client.getPhoneNumber(), "none");

        if (confirmed) {
            clientDatabaseRepository.delete(client.getId());
            afterDelete.run();
        }

    }

    private static boolean validateForm(ReferredClientsFormData referredClientsFormData) {
        try {
            isFormEmpty(referredClientsFormData);

            if (!referredClientsFormData.email().isBlank()) {
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
                    null, e.getMessage());
            return false;
        }
    }

    private static void isFormEmpty(ReferredClientsFormData formData) {
        List<PairData<String, String>> fields = List.of(
                new PairData<>("First Name", formData.firstName()),
                new PairData<>("Last Name", formData.lastName()),
                new PairData<>("Email", formData.email()),
                new PairData<>("Phone Number", formData.phoneNumber())
        );

        for (PairData<String, String> field : fields) {
            if (field.getValue() == null || field.getValue().isBlank()) {
                throw new EmptyFieldsException(field.getKey() + " is required!");
            }
        }
    }
}
