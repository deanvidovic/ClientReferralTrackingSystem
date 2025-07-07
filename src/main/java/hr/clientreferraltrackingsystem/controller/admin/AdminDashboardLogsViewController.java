package hr.clientreferraltrackingsystem.controller.admin;

import hr.clientreferraltrackingsystem.serialization.ChangeLogHolder;
import hr.clientreferraltrackingsystem.serialization.ChangeLogManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * Controller class for the admin dashboard logs view.
 * <p>
 * Responsible for displaying a list of change logs (field changes, old/new values, etc.)
 * in a JavaFX TableView. The data is deserialized using {@link ChangeLogManager}.
 */
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

    /**
     * Initializes the controller by setting up column value factories and loading the log data.
     */
    public void initialize() {
        logFieldChange.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFieldChanged()));

        logOldChange.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOldValue()));

        logNewChange.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNewValue()));

        logDateChange.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRole())); // Might be misnamed (shows role, not date)

        logChangeRole.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFormattedDateTime())); // Might be misnamed (shows date)

        showLogs();
    }

    /**
     * Loads the list of change logs from storage and populates the TableView.
     */
    public void showLogs() {
        List<ChangeLogHolder> listData = ChangeLogManager.deserializeChanges();
        changesTable.setItems(FXCollections.observableArrayList(listData));
    }
}
