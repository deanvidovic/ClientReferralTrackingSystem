package hr.clientreferraltrackingsystem.controller.admin;

import hr.clientreferraltrackingsystem.controller.admin.dto.AdminUsersFormData;
import hr.clientreferraltrackingsystem.controller.admin.helper.AdminDashboardUserHelper;
import hr.clientreferraltrackingsystem.enumeration.Role;
import hr.clientreferraltrackingsystem.model.User;
import hr.clientreferraltrackingsystem.repository.database.UserDatabaseRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Comparator;
import java.util.List;
import java.util.Set;


public class AdminDashboardUsersController {
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> userIdColumn;
    @FXML
    private TableColumn<User, String> userFirstNameColumn;
    @FXML
    private TableColumn<User, String> userLastNameColumn;
    @FXML
    private TableColumn<User, String> userEmailColumn;
    @FXML
    private TableColumn<User, String> userPhoneNumberColumn;
    @FXML
    private TableColumn<User, String> userUsernameColumn;
    @FXML
    private TableColumn<User, String> userRoleColumn;
    @FXML
    private TextField userFirstNameTextField;
    @FXML
    private TextField userLastNameTextField;
    @FXML
    private TextField userEmailTextField;
    @FXML
    private TextField userPhoneNumberTextField;
    @FXML
    private TextField userUsernameTextField;
    @FXML
    private ComboBox<Role> userRoleComboBox;
    @FXML
    private TextField userPasswordTextField;
    @FXML
    private TextField userRePasswordTextField;
    @FXML
    private Button saveButton;

    private final UserDatabaseRepository userDatabaseRepository = new UserDatabaseRepository();
    private User selectedUser;

    public void initialize() {
        userIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        userFirstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getFirstName())));
        userLastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getLastName())));
        userEmailColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getEmail())));
        userPhoneNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPhoneNumber())));
        userUsernameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getUsername())));
        userRoleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRole())));
        userRoleComboBox.setItems(FXCollections.observableArrayList(Role.values()));

        showUsers();

        usersTable.setOnMouseClicked(event -> {
            User user = usersTable.getSelectionModel().getSelectedItem();
            if (user != null) {
                selectedUser = user;
                userFirstNameTextField.setText(selectedUser.getFirstName());
                userLastNameTextField.setText(selectedUser.getLastName());
                userEmailTextField.setText(selectedUser.getEmail());
                userPhoneNumberTextField.setText(selectedUser.getPhoneNumber());
                userUsernameTextField.setText(selectedUser.getUsername());
                userRoleComboBox.setValue(selectedUser.getRole());
            }
        });


        usersTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();

            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Delete");

            deleteItem.setOnAction(event -> {
                User user = row.getItem();
                if (user != null) {
                    delete(user);
                }
            });

            contextMenu.getItems().add(deleteItem);

            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            contextMenu.setOnHidden(event -> {
                usersTable.getSelectionModel().clearSelection();
                clearForm();
            });

            return row;
        });
    }

    public void filterUsers() {
        List<User> userList = userDatabaseRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.USER)
                .toList();

        String firstNameFilter = userFirstNameTextField.getText().trim();
        String lastNameFilter = userLastNameTextField.getText().trim();
        String emailFilter = userEmailTextField.getText().trim();
        String phoneFilter = userPhoneNumberTextField.getText().trim();
        String usernameFilter = userUsernameTextField.getText().trim();
        Role selectedRole = userRoleComboBox.getValue();

        if (!firstNameFilter.isEmpty()) {
            userList = userList.stream()
                    .filter(user -> user.getFirstName().toLowerCase().contains(firstNameFilter.toLowerCase()))
                    .toList();
        }

        if (!lastNameFilter.isEmpty()) {
            userList = userList.stream()
                    .filter(user -> user.getLastName().toLowerCase().contains(lastNameFilter.toLowerCase()))
                    .toList();
        }

        if (!emailFilter.isEmpty()) {
            userList = userList.stream()
                    .filter(user -> user.getEmail().toLowerCase().contains(emailFilter.toLowerCase()))
                    .toList();
        }

        if (!phoneFilter.isEmpty()) {
            userList = userList.stream()
                    .filter(user -> user.getPhoneNumber().toLowerCase().contains(phoneFilter.toLowerCase()))
                    .toList();
        }

        if (!usernameFilter.isEmpty()) {
            userList = userList.stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(usernameFilter.toLowerCase()))
                    .toList();
        }

        if (selectedRole != null) {
            userList = userList.stream()
                    .filter(user -> user.getRole() == selectedRole)
                    .toList();
        }

        usersTable.setItems(FXCollections.observableList(userList));
    }


    public void save() {
        AdminUsersFormData formData = new AdminUsersFormData(
                userFirstNameTextField.getText().trim(), userLastNameTextField.getText().trim(),
                userEmailTextField.getText().trim(), userPhoneNumberTextField.getText().trim(),
                userUsernameTextField.getText().trim(), userRoleComboBox.getValue(),
                userPasswordTextField.getText(), userRePasswordTextField.getText()
        );

        AdminDashboardUserHelper.handleSaveOrEdit(formData, userDatabaseRepository, selectedUser);

        clearForm();
        showUsers();
        usersTable.getSelectionModel().clearSelection();
    }

    public void delete(User user) {
        hr.clientreferraltrackingsystem.controller.admin.helper.AdminDashboardUserHelper.handleDelete(
                user,
                userDatabaseRepository,
                () -> {
                    showUsers();
                    clearForm();
                    usersTable.getSelectionModel().clearSelection();
                }
        );
    }

    private void clearForm() {
        userFirstNameTextField.clear();
        userLastNameTextField.clear();
        userEmailTextField.clear();
        userPhoneNumberTextField.clear();
        userUsernameTextField.clear();
        userRoleComboBox.getSelectionModel().clearSelection();
        userPasswordTextField.clear();
        userRePasswordTextField.clear();
        selectedUser = null;
    }

    private void showUsers() {
        Set<User> users = userDatabaseRepository.findAll();
        List<User> userList = users.stream()
                .filter(u -> u.getRole() == Role.USER)
                .sorted(Comparator.comparing(User::getLastName))
                .toList();
        usersTable.setItems(FXCollections.observableList(userList));
    }
}
