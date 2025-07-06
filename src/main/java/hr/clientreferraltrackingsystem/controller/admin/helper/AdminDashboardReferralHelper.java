package hr.clientreferraltrackingsystem.controller.admin.helper;

import hr.clientreferraltrackingsystem.controller.DialogController;
import hr.clientreferraltrackingsystem.enumeration.ReferralStatus;
import hr.clientreferraltrackingsystem.model.Referral;
import hr.clientreferraltrackingsystem.model.Reward;
import hr.clientreferraltrackingsystem.model.User;
import hr.clientreferraltrackingsystem.repository.database.ReferralDatabaseRepository;
import hr.clientreferraltrackingsystem.repository.database.UserDatabaseRepository;
import hr.clientreferraltrackingsystem.utils.Message;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AdminDashboardReferralHelper {
    private AdminDashboardReferralHelper() {}

    private static final UserDatabaseRepository userDatabaseRepository = new UserDatabaseRepository();

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
        referralDatabaseRepository.updateStatus(referral.getId(), ReferralStatus.APPROVED);

        Message.showAlert(Alert.AlertType.INFORMATION,
                "Success",
                "Referral Approved",
                "You have successfully approved the referral.");
    }

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
        referralDatabaseRepository.updateStatus(referral.getId(), ReferralStatus.REJECTED);

        Message.showAlert(Alert.AlertType.INFORMATION,
                "Rejected",
                "Referral Rejected",
                "You have successfully rejected the referral.");
    }

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
