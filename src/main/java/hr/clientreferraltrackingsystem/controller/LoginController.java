package hr.clientreferraltrackingsystem.controller;

import hr.clientreferraltrackingsystem.enumeration.Role;
import hr.clientreferraltrackingsystem.model.User;
import hr.clientreferraltrackingsystem.service.AuthService;
import hr.clientreferraltrackingsystem.service.SessionManager;
import hr.clientreferraltrackingsystem.utils.Message;
import hr.clientreferraltrackingsystem.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Optional;

public class LoginController {
    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    private final AuthService authService = new AuthService();

    public void login() {
        String username = usernameTextField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Message.showAlert(
                    Alert.AlertType.ERROR,
                    "Loggin failed",
                    "Missing input!",
                    "Please enter both username and password."
            );
        } else {
            Optional<User> user = authService.login(username, password);

            if (user.isPresent()) {
                SessionManager.instance.setLoggedUser(user.get());

                Role role = user.get().getRole();
                SceneLoader.loadDashboard(role);
            } else {
                Message.showAlert(
                        Alert.AlertType.ERROR,
                        "Loggin failed",
                        "Wrong credentials!",
                        "Username or password is incorrect!"
                );
            }

        }

    }

}