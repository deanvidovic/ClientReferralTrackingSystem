package hr.clientreferraltrackingsystem.controller.admin;

import hr.clientreferraltrackingsystem.serialization.ChangeLogHolder;
import hr.clientreferraltrackingsystem.serialization.ChangeLogManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class AdminDashboardLogsViewController {
    @FXML
    private TableView<ChangeLogHolder> changesTable;

    @FXML
    private TableColumn<ChangeLogHolder, String> logFieldChange;

    @FXML
    private TableColumn<ChangeLogHolder, String> logOldChange;

    @FXML
    private TableColumn<ChangeLogHolder, String> logNewChange;

    @FXML
    private TableColumn<ChangeLogHolder, String> logDateChange;

    @FXML
    private TableColumn<ChangeLogHolder, String> logChangeRole;

    public void initialize() {
        logFieldChange.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFieldChanged()));
        logOldChange.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOldValue()));
        logNewChange.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNewValue()));
        logDateChange.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));
        logChangeRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedDateTime()));
        showLogs();
    }

    public void showLogs() {
        List<ChangeLogHolder> listData = ChangeLogManager.deserializeChanges();
        changesTable.setItems(FXCollections.observableArrayList(listData));
    }
}
