package hr.clientreferraltrackingsystem.controller.user;

import hr.clientreferraltrackingsystem.controller.user.dto.ReferredClientsFormData;
import hr.clientreferraltrackingsystem.controller.user.helper.UserDashboardClientsHelper;
import hr.clientreferraltrackingsystem.model.Client;
import hr.clientreferraltrackingsystem.repository.database.ReferralDatabaseRepository;
import hr.clientreferraltrackingsystem.repository.database.ClientsDatabaseRepository;
import hr.clientreferraltrackingsystem.service.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class UserDashboardClientsController {
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
    private TextField clientFirstNameTextField;
    @FXML
    private TextField clientLastNameTextField;
    @FXML
    private TextField clientEmailTextField;
    @FXML
    private TextField clientPhoneNumberTextField;

    private final ClientsDatabaseRepository clientsDatabaseRepository =
            new ClientsDatabaseRepository();

    private final ReferralDatabaseRepository referralDatabaseRepository =
            new ReferralDatabaseRepository();

    private Client selectedClient;

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

        showClients();

        clientsTable.setOnMouseClicked(event -> {
            Client client = clientsTable.getSelectionModel().getSelectedItem();
            if (client != null) {
                selectedClient = client;
                clientFirstNameTextField.setText(selectedClient.getFirstName());
                clientLastNameTextField.setText(selectedClient.getLastName());
                clientEmailTextField.setText(selectedClient.getEmail());
                clientPhoneNumberTextField.setText(selectedClient.getPhoneNumber());
            }
        });

        clientsTable.setRowFactory(tv -> {
            TableRow<Client> row = new TableRow<>();

            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Delete");
            MenuItem recommendItem = new MenuItem("Recommend client");

            deleteItem.setOnAction(event -> {
                Client client = row.getItem();
                if (client != null) {
                    delete(client);
                }
            });

            recommendItem.setOnAction(event -> {
                Client client = row.getItem();
                if (client != null) {
                    recommendClient();
                }
            });

            contextMenu.getItems().addAll(deleteItem, recommendItem);

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
                .findAllByCreatedUser(SessionManager.instance.getLoggedUser().getId())
                .stream()
                .filter(client -> Boolean.FALSE.equals(client.getCurrentlyRecommended()))
                .toList();

        String firstNameFilter = clientFirstNameTextField.getText().trim();
        String lastNameFilter = clientLastNameTextField.getText().trim();
        String emailFilter = clientEmailTextField.getText().trim();
        String phoneFilter = clientPhoneNumberTextField.getText().trim();

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

        clientsList = clientsList.stream()
                .sorted(Comparator.comparing(Client::getLastName))
                .toList();

        clientsTable.setItems(FXCollections.observableList(clientsList));
    }



    public void saveOrEdit() {
        ReferredClientsFormData formData = new ReferredClientsFormData(
                clientFirstNameTextField.getText().trim(), clientLastNameTextField.getText().trim(),
                clientEmailTextField.getText().trim(), clientPhoneNumberTextField.getText().trim()
        );

        UserDashboardClientsHelper.handleSaveOrEdit(formData, clientsDatabaseRepository, selectedClient);

        clearForm();
        showClients();
        clientsTable.getSelectionModel().clearSelection();
    }

    public void delete(Client client) {
        UserDashboardClientsHelper.handleDelete(
                client,
                clientsDatabaseRepository,
                () -> {
                    showClients();
                    clearForm();
                    clientsTable.getSelectionModel().clearSelection();
                }
        );
    }

    public void recommendClient() {
        UserDashboardClientsHelper.handleRecommendClient(
                referralDatabaseRepository,
                clientsDatabaseRepository,
                selectedClient,
                () -> {
                    showClients();
                    clientsTable.getSelectionModel().clearSelection();
                }
        );
    }

    private void clearForm() {
        clientFirstNameTextField.clear();
        clientLastNameTextField.clear();
        clientEmailTextField.clear();
        clientPhoneNumberTextField.clear();
        selectedClient = null;
    }

    private void showClients() {
        Set<Client> clients = clientsDatabaseRepository.findAllByCreatedUser(SessionManager.instance.getLoggedUser().getId());
        List<Client> clientsList = clients.stream()
                .filter(client -> Boolean.FALSE.equals(client.getCurrentlyRecommended()))
                .sorted(Comparator.comparing(Client::getLastName))
                .toList();
        clientsTable.setItems(FXCollections.observableList(clientsList));
    }
}

