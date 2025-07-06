package hr.clientreferraltrackingsystem.controller;

import hr.clientreferraltrackingsystem.model.Referral;
import hr.clientreferraltrackingsystem.model.Reward;
import hr.clientreferraltrackingsystem.repository.dat.AbstractRepository;
import hr.clientreferraltrackingsystem.repository.dat.RewardRepository;
import hr.clientreferraltrackingsystem.utils.Message;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class DialogController {
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField valueField;

    private Referral referral;
    private Reward reward;

    public void setReferral(Referral referral) {
        this.referral = referral;
    }

    private final AbstractRepository<Reward> rewardRepository = new RewardRepository<>();

    @FXML
    private void onSave() {
        try {
            String desc = descriptionField.getText().trim();
            BigDecimal value = new BigDecimal(valueField.getText().trim());

            reward = new Reward.Builder()
                    .referral(referral)
                    .description(desc)
                    .value(value)
                    .rewardDate(LocalDateTime.now())
                    .build();
            rewardRepository.save(reward);


            ((Stage) descriptionField.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            Message.showAlert(Alert.AlertType.ERROR, "Invalid input", null, "Please enter a valid number for value.");
        }
    }

    @FXML
    private void onCancel() {
        reward = null;
        ((Stage) descriptionField.getScene().getWindow()).close();
    }

    public Reward getReward() {
        return reward;
    }
}
