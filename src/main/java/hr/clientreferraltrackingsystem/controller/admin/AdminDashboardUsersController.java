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


/**
 * Controller class for managing users in the admin dashboard.
 * Provides functionalities to list, filter, create, edit, and delete users.
 * Interacts with the {@link UserDatabaseRepository} to persist changes.
 * Also connects with the helper class {@link AdminDashboardUserHelper} to handle business logic.
 *
 * <p>This class is connected to a JavaFX UI and uses {@code @FXML} annotations
 * to link UI elements to the controller.</p>
 *
 */
public class AdminDashboardUsersController {

    /**
     * Table displaying users.
     */
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

    /**
     * Initializes the controller, sets up table columns, loads users,
     * and configures selection and context menu behavior.
     */
    public void initialize() {
        userIdColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        userFirstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName()));
        userLastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastName()));
        userEmailColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmail()));
        userPhoneNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPhoneNumber()));
        userUsernameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUsername()));
        userRoleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getRole())));

        userRoleComboBox.setItems(FXCollections.observableArrayList(Role.values()));

        showUsers();

        usersTable.setOnMouseClicked(event -> {
            User user = usersTable.getSelectionModel().getSelectedItem();
            if (user != null) {
                selectedUser = user;
                userFirstNameTextField.setText(user.getFirstName());
                userLastNameTextField.setText(user.getLastName());
                userEmailTextField.setText(user.getEmail());
                userPhoneNumberTextField.setText(user.getPhoneNumber());
                userUsernameTextField.setText(user.getUsername());
                userRoleComboBox.setValue(user.getRole());
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

    /**
     * Filters the users displayed in the table based on input form fields.
     */
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

    /**
     * Saves a new user or updates an existing one using form input fields.
     * Uses the {@link AdminDashboardUserHelper} to handle business logic and validation.
     */
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

    /**
     * Deletes the specified user and refreshes the table and form.
     *
     * @param user The user to be deleted.
     */
    public void delete(User user) {
        AdminDashboardUserHelper.handleDelete(
                user,
                userDatabaseRepository,
                () -> {
                    showUsers();
                    clearForm();
                    usersTable.getSelectionModel().clearSelection();
                }
        );
    }

    /**
     * Clears all form input fields and resets the selected user.
     */
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

    /**
     * Loads all users with the role {@code USER} from the repository
     * and displays them in the table, sorted by last name.
     */
    private void showUsers() {
        Set<User> users = userDatabaseRepository.findAll();
        List<User> userList = users.stream()
                .filter(u -> u.getRole() == Role.USER)
                .sorted(Comparator.comparing(User::getLastName))
                .toList();
        usersTable.setItems(FXCollections.observableList(userList));
    }
}
