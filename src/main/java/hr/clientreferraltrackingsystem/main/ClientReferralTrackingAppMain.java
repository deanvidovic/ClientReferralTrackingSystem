package hr.clientreferraltrackingsystem.main;

import hr.clientreferraltrackingsystem.utils.SceneLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;

public class ClientReferralTrackingAppMain extends Application {
    private static final Logger log = LoggerFactory.getLogger(ClientReferralTrackingAppMain.class);

    @Override
    public void start(Stage stage) throws IOException {
        log.info("Starting ClientReferralTrackingAppMain");
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/hr/clientreferraltrackingsystem/login/loginView.fxml")
        );

        SceneLoader.setStage(stage);

        Scene scene = new Scene(fxmlLoader.load(), 1300,700);

        Image icon = new Image(getClass().getResource
                ("/hr/clientreferraltrackingsystem/images/icon.png").toExternalForm());

        stage.getIcons().add(icon);
        stage.setTitle("Client Referral Tracking System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}