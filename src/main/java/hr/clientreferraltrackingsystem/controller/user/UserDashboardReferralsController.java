package hr.clientreferraltrackingsystem.controller.user;

import hr.clientreferraltrackingsystem.controller.user.helper.UserDashboardReferralsHelper;
import hr.clientreferraltrackingsystem.enumeration.ReferralStatus;
import hr.clientreferraltrackingsystem.model.Referral;
import hr.clientreferraltrackingsystem.repository.database.ReferralDatabaseRepository;
import hr.clientreferraltrackingsystem.repository.database.ClientsDatabaseRepository;
import hr.clientreferraltrackingsystem.service.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class UserDashboardReferralsController {
    @FXML
    private TableView<Referral> referralsTable;
    @FXML
    private TableColumn<Referral, String> referralClientIdColumn;
    @FXML
    private TableColumn<Referral, String> referralFirstNameColumn;
    @FXML
    private TableColumn<Referral, String> referralLastNameColumn;
    @FXML
    private TableColumn<Referral, String> referralEmailColumn;
    @FXML
    private TableColumn<Referral, String> referralPhoneNumberColumn;
    @FXML
    private TableColumn<Referral, String> referralStatusColumn;
    @FXML
    private TableColumn<Referral, String> referralDateColumn;
    @FXML
    private TextField referralFirstNameTextField;
    @FXML
    private TextField referralLastNameTextField;
    @FXML
    private TextField referralEmailTextField;
    @FXML
    private TextField referralPhoneNumberTextField;
    @FXML
    private ComboBox<ReferralStatus> referralStatusComboBox;
    @FXML
    private DatePicker referralDatePickerFrom;
    @FXML
    private DatePicker referralDatePickerTo;

    private final ReferralDatabaseRepository referralDatabaseRepository = new ReferralDatabaseRepository();
    private final ClientsDatabaseRepository clientsDatabaseRepository = new ClientsDatabaseRepository();
    private Referral selectedReferral;

    public void initialize() {
        referralClientIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        referralFirstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRefferedClient().getFirstName())));
        referralLastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRefferedClient().getLastName())));
        referralEmailColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRefferedClient().getEmail())));
        referralPhoneNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRefferedClient().getPhoneNumber())));
        referralStatusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getReferralStatus())));
        referralDateColumn.setCellValueFactory(cellData -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                String formattedDate = cellData.getValue().getRefferalDate().format(formatter);
                return new SimpleStringProperty(formattedDate);
        });
        referralStatusComboBox.setItems(FXCollections.observableArrayList(ReferralStatus.values()));

        showReferrals();

        referralsTable.setRowFactory(tv -> {
            TableRow<Referral> row = new TableRow<>();

            ContextMenu contextMenu = new ContextMenu();
            MenuItem removeRecommendation = new MenuItem("Remove recommendation");

            removeRecommendation.setOnAction(event -> {
                Referral referral = row.getItem();
                if (referral != null) {
                    selectedReferral = referral;
                    removeRecommendation();
                }
            });

            contextMenu.getItems().add(removeRecommendation);

            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            contextMenu.setOnHidden(event -> referralsTable.getSelectionModel().clearSelection());

            return row;
        });
    }

    private void removeRecommendation() {
        UserDashboardReferralsHelper.removeRecommendationHandler(
                referralDatabaseRepository,
                clientsDatabaseRepository,
                selectedReferral,
                () -> {
                    showReferrals();
                    referralsTable.getSelectionModel().clearSelection();
                }
        );
    }

    private void showReferrals() {
        Set<Referral> referrals = referralDatabaseRepository.findAllByReferral(SessionManager.instance.getLoggedUser().getId());

        List<Referral> referralList = referrals.stream()
                .sorted(Comparator.comparing(Referral::getRefferalDate))
                .toList();

        referralsTable.setItems(FXCollections.observableList(referralList));
    }

    public void filterReferrals() {
        List<Referral> referralList = referralDatabaseRepository
                .findAllByReferral(SessionManager.instance.getLoggedUser().getId())
                .stream()
                .toList();

        String firstNameFilter = referralFirstNameTextField.getText().trim();
        String lastNameFilter = referralLastNameTextField.getText().trim();
        String emailFilter = referralEmailTextField.getText().trim();
        String phoneFilter = referralPhoneNumberTextField.getText().trim();
        ReferralStatus statusFilter = referralStatusComboBox.getValue();
        LocalDateTime dateFrom = referralDatePickerFrom.getValue() != null
                ? referralDatePickerFrom.getValue().atStartOfDay()
                : null;
        LocalDateTime dateTo = referralDatePickerTo.getValue() != null
                ? referralDatePickerTo.getValue().atStartOfDay()
                : null;


        if (!firstNameFilter.isEmpty()) {
            referralList = referralList.stream()
                    .filter(r -> r.getRefferedClient().getFirstName().toLowerCase().contains(firstNameFilter.toLowerCase()))
                    .toList();
        }

        if (!lastNameFilter.isEmpty()) {
            referralList = referralList.stream()
                    .filter(r -> r.getRefferedClient().getLastName().toLowerCase().contains(lastNameFilter.toLowerCase()))
                    .toList();
        }

        if (!emailFilter.isEmpty()) {
            referralList = referralList.stream()
                    .filter(r -> r.getRefferedClient().getEmail().toLowerCase().contains(emailFilter.toLowerCase()))
                    .toList();
        }

        if (!phoneFilter.isEmpty()) {
            referralList = referralList.stream()
                    .filter(r -> r.getRefferedClient().getPhoneNumber().toLowerCase().contains(phoneFilter.toLowerCase()))
                    .toList();
        }

        if (statusFilter != null) {
            referralList = referralList.stream()
                    .filter(r -> r.getReferralStatus().toString().toLowerCase().contains(statusFilter.toString().toLowerCase()))
                    .toList();
        }

        if (dateFrom != null) {
            referralList = referralList.stream()
                    .filter(r -> !r.getRefferalDate().isBefore(dateFrom))
                    .toList();
        }

        if (dateTo != null) {
            referralList = referralList.stream()
                    .filter(r -> !r.getRefferalDate().isAfter(dateTo))
                    .toList();
        }

        referralList = referralList.stream()
                .sorted(Comparator.comparing(Referral::getRefferalDate))
                .toList();

        referralsTable.setItems(FXCollections.observableList(referralList));
    }
}
