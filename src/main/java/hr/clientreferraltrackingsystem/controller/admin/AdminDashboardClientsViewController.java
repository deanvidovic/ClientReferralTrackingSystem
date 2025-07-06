package hr.clientreferraltrackingsystem.controller.admin;

import hr.clientreferraltrackingsystem.controller.admin.helper.AdminDashboardClientsHelper;
import hr.clientreferraltrackingsystem.enumeration.Role;
import hr.clientreferraltrackingsystem.model.Client;
import hr.clientreferraltrackingsystem.model.User;
import hr.clientreferraltrackingsystem.repository.database.ClientsDatabaseRepository;
import hr.clientreferraltrackingsystem.repository.database.UserDatabaseRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class AdminDashboardClientsViewController {
    @FXML
    private TableView<Client> clientsTable;
    @FXML
    private TableColumn<Client, String> clientIdColumn;
    @FXML
    private TableColumn<Client, String> clientFirstNameColumn;
    @FXML
    private TableColumn<Client, String> clientLastNameColumn;
    @FXML
    private TableColumn<Client, String> clientEmailColumn;
    @FXML
    private TableColumn<Client, String> clientPhoneNumberColumn;
    @FXML
    private TableColumn<Client, String> clientCreatedByColumn;

    @FXML
    private TextField clientFirstNameTextField;
    @FXML
    private TextField clientLastNameTextField;
    @FXML
    private TextField clientEmailTextField;
    @FXML
    private TextField clientPhoneNumberTextField;
    @FXML
    private ComboBox<String> createdByComboBox;

    private final ClientsDatabaseRepository clientsDatabaseRepository =
            new ClientsDatabaseRepository();
    private final UserDatabaseRepository userDatabaseRepository =
            new UserDatabaseRepository();

    public void initialize() {
        clientIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        clientFirstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getFirstName())));
        clientLastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getLastName())));
        clientEmailColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getEmail())));
        clientPhoneNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPhoneNumber())));
        clientCreatedByColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getCreatedBy().getUserFullName())));

        List<String> allUsers = userDatabaseRepository.findAll()
                .stream()
                .filter(u -> u.getRole() != Role.ADMIN)
                .map(User::getUserFullName)
                .toList();
        createdByComboBox.setItems(FXCollections.observableArrayList(allUsers));

        showClients();

        clientsTable.setRowFactory(tv -> {
            TableRow<Client> row = new TableRow<>();

            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Delete");

            deleteItem.setOnAction(event -> {
                Client client = row.getItem();
                if (client != null) {
                    delete(client);
                }
            });

            contextMenu.getItems().add(deleteItem);

            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            contextMenu.setOnHidden(event -> {
                clientsTable.getSelectionModel().clearSelection();
                clearForm();
            });

            return row;
        });
    }

    public void filterClients() {
        List<Client> clientsList = clientsDatabaseRepository
                .findAll().stream()
                .filter(client -> Boolean.FALSE.equals(client.getCurrentlyRecommended()))
                .toList();

        String firstNameFilter = clientFirstNameTextField.getText().trim();
        String lastNameFilter = clientLastNameTextField.getText().trim();
        String emailFilter = clientEmailTextField.getText().trim();
        String phoneFilter = clientPhoneNumberTextField.getText().trim();
        String createdByFilter = createdByComboBox.getValue();


        if (!firstNameFilter.isEmpty()) {
            clientsList = clientsList.stream()
                    .filter(c -> c.getFirstName().toLowerCase().contains(firstNameFilter.toLowerCase()))
                    .toList();
        }

        if (!lastNameFilter.isEmpty()) {
            clientsList = clientsList.stream()
                    .filter(c -> c.getLastName().toLowerCase().contains(lastNameFilter.toLowerCase()))
                    .toList();
        }

        if (!emailFilter.isEmpty()) {
            clientsList = clientsList.stream()
                    .filter(c -> c.getEmail().toLowerCase().contains(emailFilter.toLowerCase()))
                    .toList();
        }

        if (!phoneFilter.isEmpty()) {
            clientsList = clientsList.stream()
                    .filter(c -> c.getPhoneNumber().toLowerCase().contains(phoneFilter.toLowerCase()))
                    .toList();
        }

        if (createdByFilter != null && !createdByFilter.isEmpty()) {
            clientsList = clientsList.stream()
                    .filter(c -> c.getCreatedBy() != null && createdByFilter.equals(c.getCreatedBy().getUserFullName()))
                    .toList();
        }

        clientsList = clientsList.stream()
                .sorted(Comparator.comparing(Client::getLastName))
                .toList();

        clientsTable.setItems(FXCollections.observableList(clientsList));
        clearForm();
    }


    public void delete(Client client) {
        AdminDashboardClientsHelper.handleDelete(
                client,
                clientsDatabaseRepository,
                () -> {
                    showClients();
                    clearForm();
                    clientsTable.getSelectionModel().clearSelection();
                }
        );
    }

    private void clearForm() {
        clientFirstNameTextField.clear();
        clientLastNameTextField.clear();
        clientEmailTextField.clear();
        clientPhoneNumberTextField.clear();
        createdByComboBox.setValue(null);
    }

    private void showClients() {
        Set<Client> clients = clientsDatabaseRepository.findAll();
        List<Client> clientsList = clients.stream()
                .filter(client -> Boolean.FALSE.equals(client.getCurrentlyRecommended()))
                .sorted(Comparator.comparing(Client::getLastName))
                .toList();
        clientsTable.setItems(FXCollections.observableList(clientsList));
    }
}
