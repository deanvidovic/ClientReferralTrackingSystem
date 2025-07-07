package hr.clientreferraltrackingsystem.controller.admin.helper;

import hr.clientreferraltrackingsystem.controller.DialogController;
import hr.clientreferraltrackingsystem.enumeration.ReferralStatus;
import hr.clientreferraltrackingsystem.model.Referral;
import hr.clientreferraltrackingsystem.model.Reward;
import hr.clientreferraltrackingsystem.repository.database.ReferralDatabaseRepository;
import hr.clientreferraltrackingsystem.utils.InputValidator;
import hr.clientreferraltrackingsystem.utils.Message;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility helper class for handling admin operations related to referrals,
 * including approval, rejection, and reward dialog interaction.
 * <p>
 * This class is used in the admin dashboard context.
 */
public class AdminDashboardReferralHelper {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private AdminDashboardReferralHelper() {}

    /**
     * Handles the approval of a referral.
     * <p>
     * Displays a reward input dialog and updates the referral status
     * to {@link ReferralStatus#APPROVED} if applicable.
     *
     * @param referral the referral to be approved
     * @param referralDatabaseRepository the repository used to update the referral status
     */
    public static void handleApproveReferral(Referral referral, ReferralDatabaseRepository referralDatabaseRepository) {
        if (referral.getReferralStatus() == ReferralStatus.APPROVED) {
            Message.showAlert(Alert.AlertType.ERROR,
                    "Already Approved",
                    null,
                    "This referral is already approved.");
            return;
        }

        if (referral.getReferralStatus() == ReferralStatus.REJECTED) {
            Message.showAlert(Alert.AlertType.ERROR,
                    "Already Rejected",
                    null,
                    "This referral has already been rejected and cannot be approved.");
            return;
        }

        Reward reward = showRewardInputDialog(referral);

        if (reward == null) {
            return;
        }

        referral.setReferralStatus(ReferralStatus.APPROVED);
        InputValidator.serializeSave("Referral approved", referral.getId(), referral.getRefferedClient().getEmail());
        referralDatabaseRepository.updateStatus(referral.getId(), ReferralStatus.APPROVED);

        Message.showAlert(Alert.AlertType.INFORMATION,
                "Success",
                "Referral Approved",
                "You have successfully approved the referral.");
    }

    /**
     * Handles the rejection of a referral.
     * <p>
     * Updates the referral status to {@link ReferralStatus#REJECTED}
     * if it has not already been approved or rejected.
     *
     * @param referral the referral to be rejected
     * @param referralDatabaseRepository the repository used to update the referral status
     */
    public static void handleRejectReferral(Referral referral, ReferralDatabaseRepository referralDatabaseRepository) {
        if (referral.getReferralStatus() == ReferralStatus.REJECTED) {
            Message.showAlert(Alert.AlertType.ERROR,
                    "Already Rejected",
                    null,
                    "This referral is already rejected.");
            return;
        }

        if (referral.getReferralStatus() == ReferralStatus.APPROVED) {
            Message.showAlert(Alert.AlertType.ERROR,
                    "Already Approved",
                    null,
                    "This referral has already been approved and cannot be rejected.");
            return;
        }

        referral.setReferralStatus(ReferralStatus.REJECTED);
        InputValidator.serializeSave("Referral rejected", referral.getId(), referral.getRefferedClient().getEmail());
        referralDatabaseRepository.updateStatus(referral.getId(), ReferralStatus.REJECTED);

        Message.showAlert(Alert.AlertType.INFORMATION,
                "Rejected",
                "Referral Rejected",
                "You have successfully rejected the referral.");
    }

    /**
     * Displays a modal dialog for entering reward details when approving a referral.
     *
     * @param referral the referral associated with the reward
     * @return a {@link Reward} object if one was entered, or {@code null} if the dialog was cancelled
     */
    public static Reward showRewardInputDialog(Referral referral) {
        try {
            FXMLLoader loader = new FXMLLoader(AdminDashboardReferralHelper.class.getResource("/hr/clientreferraltrackingsystem/user/rewardDialog.fxml"));
            Parent root = loader.load();

            DialogController controller = loader.getController();
            controller.setReferral(referral);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Reward");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            dialogStage.showAndWait();

            return controller.getReward();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
