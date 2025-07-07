package hr.clientreferraltrackingsystem.controller.admin;

import hr.clientreferraltrackingsystem.controller.admin.helper.AdminDashboardReferralHelper;
import hr.clientreferraltrackingsystem.enumeration.ReferralStatus;
import hr.clientreferraltrackingsystem.enumeration.Role;
import hr.clientreferraltrackingsystem.model.Referral;
import hr.clientreferraltrackingsystem.model.User;
import hr.clientreferraltrackingsystem.repository.database.ReferralDatabaseRepository;
import hr.clientreferraltrackingsystem.repository.database.UserDatabaseRepository;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller class for managing referrals in the admin dashboard.
 * <p>
 * This class handles displaying all referrals, filtering them by various criteria,
 * approving or rejecting them, and showing the most recent referral in real time.
 */
public class AdminDashboardReferralsController {

    @FXML private TableView<Referral> referralsTable;
    @FXML private TableColumn<Referral, String> referralClientIdColumn;
    @FXML private TableColumn<Referral, String> referralFirstNameColumn;
    @FXML private TableColumn<Referral, String> referralLastNameColumn;
    @FXML private TableColumn<Referral, String> referralEmailColumn;
    @FXML private TableColumn<Referral, String> referralPhoneNumberColumn;
    @FXML private TableColumn<Referral, String> referralStatusColumn;
    @FXML private TableColumn<Referral, String> referralDateColumn;
    @FXML private TableColumn<Referral, String> referralReferredByColumn;

    @FXML private TextField referralFirstNameTextField;
    @FXML private TextField referralLastNameTextField;
    @FXML private TextField referralEmailTextField;
    @FXML private TextField referralPhoneNumberTextField;

    @FXML private ComboBox<ReferralStatus> referralStatusComboBox;
    @FXML private DatePicker referralDatePickerFrom;
    @FXML private DatePicker referralDatePickerTo;
    @FXML private ComboBox<String> referralReferredByComboBox;

    @FXML private Label latestReferral;

    private final ReferralDatabaseRepository referralDatabaseRepository = new ReferralDatabaseRepository();
    private final UserDatabaseRepository userDatabaseRepository = new UserDatabaseRepository();
    private Referral selectedReferral;

    /**
     * Initializes the controller after the FXML fields are loaded.
     * Sets up the table columns, filters, and real-time display of the latest referral.
     */
    public void initialize() {
        referralClientIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        referralFirstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRefferedClient().getFirstName()));
        referralLastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRefferedClient().getLastName()));
        referralEmailColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRefferedClient().getEmail()));
        referralPhoneNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRefferedClient().getPhoneNumber()));
        referralStatusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReferralStatus().toString()));
        referralDateColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            return new SimpleStringProperty(cellData.getValue().getRefferalDate().format(formatter));
        });
        referralReferredByColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRefferer().getUserFullName()));

        List<String> allUsers = userDatabaseRepository.findAll().stream()
                .filter(u -> u.getRole() != Role.ADMIN)
                .map(User::getUserFullName)
                .toList();
        referralReferredByComboBox.setItems(FXCollections.observableList(allUsers));
        referralStatusComboBox.setItems(FXCollections.observableArrayList(ReferralStatus.values()));

        showReferrals();
        setupRowContextMenu();
        setupLatestReferralTimer();
    }

    /**
     * Sets up a context menu on each table row to allow approving or rejecting referrals.
     */
    private void setupRowContextMenu() {
        referralsTable.setRowFactory(tv -> {
            TableRow<Referral> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem approveItem = new MenuItem("Approve");
            approveItem.setOnAction(event -> {
                Referral referral = row.getItem();
                if (referral != null) {
                    selectedReferral = referral;
                    approveReferral(selectedReferral);
                }
            });

            MenuItem rejectItem = new MenuItem("Reject");
            rejectItem.setOnAction(event -> {
                Referral referral = row.getItem();
                if (referral != null) {
                    selectedReferral = referral;
                    rejectReferral(selectedReferral);
                }
            });

            contextMenu.getItems().addAll(approveItem, rejectItem);

            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            contextMenu.setOnHidden(event -> referralsTable.getSelectionModel().clearSelection());
            return row;
        });
    }

    /**
     * Starts a timeline that updates the latest referral every second.
     */
    private void setupLatestReferralTimer() {
        Timeline latestReferralTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> {
                    Optional<Referral> latestReferralOpt = referralDatabaseRepository.findLatestReferralFromDb();
                    latestReferralOpt.ifPresent(r -> latestReferral.setText(r.threadPrint()));
                }),
                new KeyFrame(Duration.seconds(1))
        );
        latestReferralTimeline.setCycleCount(Animation.INDEFINITE);
        latestReferralTimeline.play();
    }

    /**
     * Approves a given referral using the referral helper.
     *
     * @param referral the referral to approve
     */
    public void approveReferral(Referral referral) {
        AdminDashboardReferralHelper.handleApproveReferral(referral, referralDatabaseRepository);
        showReferrals();
        referralsTable.refresh();
    }

    /**
     * Rejects a given referral using the referral helper.
     *
     * @param referral the referral to reject
     */
    public void rejectReferral(Referral referral) {
        AdminDashboardReferralHelper.handleRejectReferral(referral, referralDatabaseRepository);
        showReferrals();
        referralsTable.refresh();
    }

    /**
     * Loads and displays all referrals sorted by referral date.
     */
    private void showReferrals() {
        Set<Referral> referrals = referralDatabaseRepository.findAll();
        List<Referral> referralList = referrals.stream()
                .sorted(Comparator.comparing(Referral::getRefferalDate))
                .toList();
        referralsTable.setItems(FXCollections.observableArrayList(referralList));
    }

    /**
     * Clears all filter input fields.
     */
    public void clearForm() {
        referralFirstNameTextField.clear();
        referralLastNameTextField.clear();
        referralEmailTextField.clear();
        referralPhoneNumberTextField.clear();
        referralStatusComboBox.getSelectionModel().clearSelection();
        referralDatePickerFrom.setValue(null);
        referralDatePickerTo.setValue(null);
        referralReferredByComboBox.getSelectionModel().clearSelection();
    }

    /**
     * Filters the referral list based on the input fields and selection criteria.
     * Applies filtering by first name, last name, email, phone, status, date range, and referrer.
     */
    public void filterReferrals() {
        List<Referral> referralList = referralDatabaseRepository.findAll().stream().toList();

        String firstNameFilter = referralFirstNameTextField.getText().trim();
        String lastNameFilter = referralLastNameTextField.getText().trim();
        String emailFilter = referralEmailTextField.getText().trim();
        String phoneFilter = referralPhoneNumberTextField.getText().trim();
        ReferralStatus statusFilter = referralStatusComboBox.getValue();
        LocalDateTime dateFrom = referralDatePickerFrom.getValue() != null ? referralDatePickerFrom.getValue().atStartOfDay() : null;
        LocalDateTime dateTo = referralDatePickerTo.getValue() != null ? referralDatePickerTo.getValue().atStartOfDay() : null;
        String referredByFilter = referralReferredByComboBox.getValue();

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
                    .filter(r -> r.getReferralStatus().equals(statusFilter))
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
        if (referredByFilter != null) {
            referralList = referralList.stream()
                    .filter(r -> r.getRefferer().getUserFullName().equalsIgnoreCase(referredByFilter))
                    .toList();
        }

        referralList = referralList.stream()
                .sorted(Comparator.comparing(Referral::getRefferalDate))
                .toList();

        referralsTable.setItems(FXCollections.observableList(referralList));
        clearForm();
    }
}
