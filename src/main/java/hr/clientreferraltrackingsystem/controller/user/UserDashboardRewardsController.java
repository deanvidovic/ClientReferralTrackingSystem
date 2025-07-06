package hr.clientreferraltrackingsystem.controller.user;

import hr.clientreferraltrackingsystem.controller.admin.helper.AdminDashboardRewardsHelper;
import hr.clientreferraltrackingsystem.model.Reward;
import hr.clientreferraltrackingsystem.repository.dat.AbstractRepository;
import hr.clientreferraltrackingsystem.repository.dat.RewardRepository;
import hr.clientreferraltrackingsystem.service.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class UserDashboardRewardsController {
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

        showRewards();
    }


    private void showRewards() {
        List<Reward> rewards = SessionManager.getInstance().getLoggedUser().getRewards();
        List<Reward> referralList = rewards.stream().sorted(Comparator.comparing(Reward::getRewardDate)).toList();
        rewardsTable.setItems(FXCollections.observableArrayList(referralList));
    }

    public void filterRewards() {
        List<Reward> rewardList = SessionManager.getInstance().getLoggedUser().getRewards();

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
        rewardDescriptionTextField.clear();
        rewardValueFromTextField.clear();
        rewardValueToTextField.clear();
        rewardDatePickerFrom.setValue(null);
        rewardDatePickerTo.setValue(null);
    }

}
