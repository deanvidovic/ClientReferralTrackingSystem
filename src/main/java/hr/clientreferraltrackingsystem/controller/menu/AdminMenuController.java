package hr.clientreferraltrackingsystem.controller.menu;

import hr.clientreferraltrackingsystem.service.SessionManager;
import hr.clientreferraltrackingsystem.utils.Message;
import hr.clientreferraltrackingsystem.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.Map;


public class AdminMenuController {

    @FXML
    private Label adminNameLabel;

    public void initialize() {
        Map<String, String> adminInfo = new HashMap<>();
        adminInfo.put("firstName", SessionManager.instance.getLoggedUser().getFirstName());
        adminInfo.put("lastName", SessionManager.instance.getLoggedUser().getLastName());
        adminNameLabel.setText(SessionManager.instance.getLoggedUser().getFirstName()
                + " " + SessionManager.instance.getLoggedUser().getLastName());
    }

    public void logOut() {
        boolean confirmed = Message.showConfirmation("Log out", "Please confirm", "Are you sure you want to log out?");

        if (confirmed) {
            SessionManager.instance.setLoggedUser(null);
            SceneLoader.switchToScene("/hr/clientreferraltrackingsystem/login/loginView.fxml");
        }
    }

    public void openUsersScreen() {
        SceneLoader.switchToScene("/hr/clientreferraltrackingsystem/admin/adminDashboardUsersView.fxml");
    }

    public void openClientsScreen() {
        SceneLoader.switchToScene("/hr/clientreferraltrackingsystem/admin/adminDashboardAllClientsView.fxml");
    }

    public void openReferralsScreen() {
        SceneLoader.switchToScene("/hr/clientreferraltrackingsystem/admin/adminDashboardReferralsView.fxml");
    }

    public void openRewardsScreen() {
        SceneLoader.switchToScene("/hr/clientreferraltrackingsystem/admin/adminDashnoardRewardsView.fxml");
    }

    public void openChangesScreen() {
        SceneLoader.switchToScene("/hr/clientreferraltrackingsystem/admin/adminDashboardLogsView.fxml");
    }
}
