package hr.clientreferraltrackingsystem.utils;

import hr.clientreferraltrackingsystem.enumeration.Role;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * Utility class responsible for managing the primary JavaFX stage and switching between scenes.
 */
public class SceneLoader {
    private static final Logger log = LoggerFactory.getLogger(SceneLoader.class);

    private static Stage stage;

    private SceneLoader() {}

    /**
     * Sets the primary stage of the application and initializes the stage icon and title.
     *
     * @param primaryStage the main stage of the application
     */
    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
        Image icon = new Image(SceneLoader.class.getResource("/hr/clientreferraltrackingsystem/images/icon.png").toExternalForm());
        stage.getIcons().add(icon);
        stage.setTitle("Client Referral Tracking System");
    }

    /**
     * Switches the current scene of the primary stage to the FXML resource specified by the path.
     * Applies a fade-in transition when displaying the scene.
     *
     * @param fxmlPath the path to the FXML file for the new scene
     */
    public static void switchToScene(String fxmlPath) {
        if (stage == null) {
            log.error("Stage is not set. Cannot load scene: {}", fxmlPath);
            return;
        }

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(SceneLoader.class.getResource(fxmlPath)));
            Scene scene = new Scene(root, 1300, 700);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.setFullScreen(false);
            stage.show();

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.20), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        } catch (IOException | NullPointerException e) {
            log.error("Failed to load scene in class {}!", e.getClass().getSimpleName(), e);
        }
    }

    /**
     * Loads the dashboard scene based on the user role.
     * Admins are directed to the admin dashboard, users to the user dashboard,
     * and others to an access denied error view.
     *
     * @param role the role of the user
     */
    public static void loadDashboard(Role role) {
        String path = switch (role) {
            case ADMIN -> "/hr/clientreferraltrackingsystem/admin/adminDashboardUsersView.fxml";
            case USER -> "/hr/clientreferraltrackingsystem/user/userDashboardClientsView.fxml";
            default -> "/hr/clientreferraltrackingsystem/error/accessDeniedView.fxml";
        };

        switchToScene(path);
    }
}
