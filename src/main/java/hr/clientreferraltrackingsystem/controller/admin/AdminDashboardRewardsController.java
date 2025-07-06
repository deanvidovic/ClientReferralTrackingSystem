package hr.clientreferraltrackingsystem.controller.admin;

import hr.clientreferraltrackingsystem.controller.admin.helper.AdminDashboardRewardsHelper;
import hr.clientreferraltrackingsystem.model.Reward;
import hr.clientreferraltrackingsystem.repository.dat.AbstractRepository;
import hr.clientreferraltrackingsystem.repository.dat.RewardRepository;
import hr.clientreferraltrackingsystem.utils.InputValidator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import static hr.clientreferraltrackingsystem.utils.Message.showAlert;

public class AdminDashboardRewardsController {
    @FXML
    private TableView<Reward> rewardsTable;
    @FXML
    private TableColumn<Reward, String> rewardIdColumn;
    @FXML
    private TableColumn<Reward, String> rewardReferredClientColumn;
    @FXML
    private TableColumn<Reward, String> rewardDescriptionColumn;
    @FXML
    private TableColumn<Reward, String> rewardValueColumn;
    @FXML
    private TableColumn<Reward, String> rewardDateColumn;

    @FXML
    private TextField rewardClientTextField;
    @FXML
    private TextField rewardDescriptionTextField;
    @FXML
    private TextField rewardValueFromTextField;
    @FXML
    private TextField rewardValueToTextField;
    @FXML
    private DatePicker rewardDatePickerFrom;
    @FXML
    private DatePicker rewardDatePickerTo;
    @FXML
    private TextField rewardDescriptionEditTextField;
    @FXML
    private TextField rewardValueEditTextField;

    private final AbstractRepository<Reward> rewardRepository = new RewardRepository<>();
    private Reward selectedReward;

    public void initialize() {
        rewardIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        rewardReferredClientColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getReferral().getRefferedClient().getFullName())));
        rewardDescriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getDescription())));
        rewardValueColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValue())));
        rewardDateColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            String formattedDate = cellData.getValue().getRewardDate().format(formatter);
            return new SimpleStringProperty(formattedDate);
        });

        rewardsTable.setOnMouseClicked(event -> {
            Reward reward = rewardsTable.getSelectionModel().getSelectedItem();
            if (reward != null) {
                selectedReward = reward;
                rewardDescriptionEditTextField.setText(selectedReward.getDescription());
                rewardValueEditTextField.setText(selectedReward.getValue().toString());
            }
        });

        showRewards();
    }

    public void editRewards() {
        String description = rewardDescriptionEditTextField.getText();
        String valueText = rewardValueEditTextField.getText().trim();

        if (selectedReward == null) {
            showAlert(Alert.AlertType.ERROR, "Selection error", null, "You must select a reward for edit.");
            return;
        }

        if (description.isEmpty() || valueText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "All fields are required.");
            return;
        }

        if (!InputValidator.isValidBigDecimal(valueText)) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Value must be a valid number.");
            return;
        }

        BigDecimal value = new BigDecimal(valueText);

        AdminDashboardRewardsHelper.handleEdit(description, value, selectedReward);

        clearForm();
        showRewards();
        rewardsTable.refresh();
        rewardsTable.getSelectionModel().clearSelection();
    }


    private void showRewards() {
        List<Reward> rewards = rewardRepository.findAll();
        List<Reward> referralList = rewards.stream().sorted(Comparator.comparing(Reward::getRewardDate)).toList();
        rewardsTable.setItems(FXCollections.observableArrayList(referralList));
    }

    public void filterRewards() {
        List<Reward> rewardList = rewardRepository.findAll();

        String clientNameFilter = rewardClientTextField.getText().trim();
        String descriptionFilter = rewardDescriptionTextField.getText().trim();
        String valueFromText = rewardValueFromTextField.getText().trim();
        String valueToText = rewardValueToTextField.getText().trim();

        LocalDateTime dateFrom = rewardDatePickerFrom.getValue() != null ? rewardDatePickerFrom.getValue().atStartOfDay() : null;
        LocalDateTime dateTo = rewardDatePickerTo.getValue() != null ? rewardDatePickerTo.getValue().atStartOfDay() : null;

        rewardList = AdminDashboardRewardsHelper.filterByClientName(rewardList, clientNameFilter);
        rewardList = AdminDashboardRewardsHelper.filterByDescription(rewardList, descriptionFilter);
        rewardList = AdminDashboardRewardsHelper.filterByValueFrom(rewardList, valueFromText);
        rewardList = AdminDashboardRewardsHelper.filterByValueTo(rewardList, valueToText);
        rewardList = AdminDashboardRewardsHelper.filterByDateFrom(rewardList, dateFrom);
        rewardList = AdminDashboardRewardsHelper.filterByDateTo(rewardList, dateTo);

        rewardList = rewardList.stream()
                .sorted(Comparator.comparing(Reward::getRewardDate))
                .toList();

        clearForm();
        rewardsTable.setItems(FXCollections.observableArrayList(rewardList));
    }


    private void clearForm() {
        rewardClientTextField.clear();
        rewardDescriptionTextField.clear();
        rewardValueFromTextField.clear();
        rewardValueToTextField.clear();
        rewardDatePickerFrom.setValue(null);
        rewardDatePickerTo.setValue(null);
        rewardDescriptionEditTextField.clear();
        rewardValueEditTextField.clear();
    }
}
