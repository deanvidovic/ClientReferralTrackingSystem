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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        referralReferredByColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRefferer().getUserFullName())));

        List<String> allUsers = userDatabaseRepository.findAll()
                .stream()
                .filter(u -> u.getRole() != Role.ADMIN)
                .map(User::getUserFullName)
                .toList();
        referralReferredByComboBox.setItems(FXCollections.observableList(allUsers));
        referralStatusComboBox.setItems(FXCollections.observableArrayList(ReferralStatus.values()));

        showReferrals();

        referralsTable.setRowFactory(tv -> {
            TableRow<Referral> row = new TableRow<>();

            ContextMenu contextMenu = new ContextMenu();
            MenuItem approveRecommendation = new MenuItem("Approve");
            MenuItem rejectRecommendation = new MenuItem("Reject");

            approveRecommendation.setOnAction(event -> {
                Referral referral = row.getItem();
                if (referral != null) {
                    selectedReferral = referral;
                    approveReferral(selectedReferral);
                }
            });
            rejectRecommendation.setOnAction(event -> {
                Referral referral = row.getItem();
                if (referral != null) {
                    selectedReferral = referral;
                    rejectReferral(selectedReferral);
                }
            });
            contextMenu.getItems().addAll(approveRecommendation, rejectRecommendation);
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            contextMenu.setOnHidden(event -> referralsTable.getSelectionModel().clearSelection());
            return row;
        });

        Timeline latestReferralTimeline = new Timeline(new KeyFrame(Duration.ZERO, event -> {
            Optional<Referral> latestReferralOpt = referralDatabaseRepository.findLatestReferralFromDb();
            if (latestReferralOpt.isPresent()) {
                latestReferral.setText(latestReferralOpt.get().threadPrint());
            }
        }), new KeyFrame(Duration.seconds(1)));
        latestReferralTimeline.setCycleCount(Animation.INDEFINITE);
        latestReferralTimeline.play();

    }

    public void approveReferral(Referral referral) {
        AdminDashboardReferralHelper.handleApproveReferral(referral, referralDatabaseRepository);
        showReferrals();
        referralsTable.refresh();
    }

    public void rejectReferral(Referral referral) {
        AdminDashboardReferralHelper.handleRejectReferral(referral, referralDatabaseRepository);
        showReferrals();
        referralsTable.refresh();
    }

    private void showReferrals() {
        Set<Referral> referrals = referralDatabaseRepository.findAll();
        List<Referral> referralList = referrals.stream().sorted(Comparator.comparing(Referral::getRefferalDate)).toList();
        referralsTable.setItems(FXCollections.observableArrayList(referralList));
    }

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
        if (referredByFilter != null) {
            referralList = referralList.stream()
                    .filter(r -> r.getRefferer().getUserFullName().equalsIgnoreCase(referredByFilter))
                    .toList();
        }
        referralList = referralList.stream().sorted(Comparator.comparing(Referral::getRefferalDate)).toList();
        referralsTable.setItems(FXCollections.observableList(referralList));
        clearForm();
    }
}
