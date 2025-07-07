package hr.clientreferraltrackingsystem.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Utility class for displaying different types of JavaFX alert dialogs.
 */
public class Message {
    private Message() {}

    /**
     * Shows an alert dialog with the specified parameters.
     *
     * @param alertType the type of alert to show
     * @param title the title of the alert window
     * @param header the header text of the alert
     * @param content the content message of the alert
     */
    public static void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = createAlert(alertType, title, header, content);
        alert.showAndWait();
    }

    /**
     * Creates an Alert object with the specified parameters.
     *
     * @param alertType the type of alert
     * @param title the title of the alert window
     * @param header the header text of the alert
     * @param content the content message of the alert
     * @return the created Alert object
     */
    public static Alert createAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    /**
     * Shows a confirmation dialog with Yes and No options.
     *
     * @param title the title of the confirmation dialog
     * @param header the header text of the dialog
     * @param content the content message of the dialog
     * @return true if the user clicked Yes, false otherwise
     */
    public static boolean showConfirmation(String title, String header, String content) {
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, content, yesButton, noButton);
        alert.setTitle(title);
        alert.setHeaderText(header);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }
}
