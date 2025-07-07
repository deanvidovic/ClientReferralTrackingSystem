package hr.clientreferraltrackingsystem.controller.user.helper;

import hr.clientreferraltrackingsystem.enumeration.ReferralStatus;
import hr.clientreferraltrackingsystem.model.Referral;
import hr.clientreferraltrackingsystem.model.Client;
import hr.clientreferraltrackingsystem.repository.database.ReferralDatabaseRepository;
import hr.clientreferraltrackingsystem.repository.database.ClientsDatabaseRepository;
import hr.clientreferraltrackingsystem.service.SessionManager;
import hr.clientreferraltrackingsystem.utils.InputValidator;
import hr.clientreferraltrackingsystem.utils.Message;
import javafx.scene.control.Alert;

import java.util.Optional;


public class UserDashboardReferralsHelper {
    private UserDashboardReferralsHelper() {}

    public static void removeRecommendationHandler(ReferralDatabaseRepository referralDatabaseRepository,
                                             ClientsDatabaseRepository clientsDatabaseRepository,
                                             Referral selectedReferral,
                                             Runnable afterRemoveRecommendation) {
        if (selectedReferral != null) {

            boolean confirmed = Message.showConfirmation(
                    "Confirm Remove",
                    null,
                    "Are you sure you want to remove recommendation?"
            );

            InputValidator.serializeSave("Referral - delete: user", selectedReferral.getRefferer().getUserFullName(), "none");
            InputValidator.serializeSave("Referral - delete: client", selectedReferral.getRefferedClient().getFullName(), "none");
            InputValidator.serializeSave("Referral - delete: referralDate", selectedReferral.getRefferalDate().toLocalDate(), "none");
            InputValidator.serializeSave("Referral - delete: phoneNumber", selectedReferral.getReferralStatus(), "none");

            if (selectedReferral.getReferralStatus() == ReferralStatus.APPROVED
                    || selectedReferral.getReferralStatus() == ReferralStatus.REJECTED) {
                Message.showAlert(Alert.AlertType.WARNING,
                        "Action Denied",
                        null,
                        "Approved recommendations cannot be removed.");
                return;
            }

            if (confirmed) {
                Optional<Client> referredClient = clientsDatabaseRepository.findById(
                        selectedReferral.getRefferedClient().getId());

                if (referredClient.isPresent()) {
                    referredClient.get().setCurrentlyRecommended(false);
                    clientsDatabaseRepository.update(referredClient.get(), SessionManager.instance.getLoggedUser().getId());
                    referralDatabaseRepository.delete(selectedReferral.getId());
                }

                Message.showAlert(Alert.AlertType.INFORMATION, "Successful removing",
                        null, "You successfully removed recommendation!");

                afterRemoveRecommendation.run();
            }
        }
    }
}
