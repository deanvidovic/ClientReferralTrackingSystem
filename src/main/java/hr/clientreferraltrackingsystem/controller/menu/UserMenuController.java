package hr.clientreferraltrackingsystem.controller.menu;

import hr.clientreferraltrackingsystem.service.SessionManager;
import hr.clientreferraltrackingsystem.utils.Message;
import hr.clientreferraltrackingsystem.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserMenuController {
    @FXML
    private Label userFirstNameLabel;


    public void initialize() {
        userFirstNameLabel.setText(SessionManager.getInstance().getLoggedUser().getFirstName()
                + " " + SessionManager.getInstance().getLoggedUser().getLastName());
    }

    public void logOut() {
        boolean confirmed = Message.showConfirmation("Log out", "Please confirm", "Are you sure you want to log out?");

        if (confirmed) {
            SessionManager.getInstance().setLoggedUser(null);
            SceneLoader.switchToScene("/hr/clientreferraltrackingsystem/login/loginView.fxml");
        }
    }

    public void openClientsScreen() {
        SceneLoader.switchToScene("/hr/clientreferraltrackingsystem/user/userDashboardClientsView.fxml");
    }

    public void openReferralsScreen() {
        SceneLoader.switchToScene("/hr/clientreferraltrackingsystem/user/userDashboardReferralsView.fxml");
    }

    public void openMyProfileScreen() {
        SceneLoader.switchToScene("/hr/clientreferraltrackingsystem/user/userDashboardMyProfileView.fxml");
    }

    public void openRewardsScreen() {
        SceneLoader.switchToScene("/hr/clientreferraltrackingsystem/user/userDashboardRewardsView.fxml");
    }
}
