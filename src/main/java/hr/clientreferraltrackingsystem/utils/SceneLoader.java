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

public class SceneLoader {
    private static final Logger log = LoggerFactory.getLogger(SceneLoader.class);

    private static Stage stage;

    private SceneLoader() {}

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
        Image icon = new Image(SceneLoader.class.getResource
                ("/hr/clientreferraltrackingsystem/images/icon.png").toExternalForm());
        stage.getIcons().add(icon);
        stage.setTitle("Client Referral Tracking System");
    }

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

    public static void loadDashboard(Role role) {
        String path = switch (role) {
            case ADMIN -> "/hr/clientreferraltrackingsystem/admin/adminDashboardUsersView.fxml";
            case USER -> "/hr/clientreferraltrackingsystem/user/userDashboardClientsView.fxml";
            default -> "/hr/clientreferraltrackingsystem/error/accessDeniedView.fxml";
        };

        switchToScene(path);
    }
}
