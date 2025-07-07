    package hr.clientreferraltrackingsystem.controller.user;

    import hr.clientreferraltrackingsystem.controller.user.dto.UserFormData;
    import hr.clientreferraltrackingsystem.controller.user.helper.UserDashboardMyProfileHelper;
    import hr.clientreferraltrackingsystem.model.User;
    import hr.clientreferraltrackingsystem.repository.database.UserDatabaseRepository;
    import hr.clientreferraltrackingsystem.service.AuthService;
    import hr.clientreferraltrackingsystem.service.SessionManager;
    import hr.clientreferraltrackingsystem.utils.InputValidator;
    import javafx.fxml.FXML;
    import javafx.scene.control.PasswordField;
    import javafx.scene.control.TextField;


    public class UserDashboardMyProfileController {
        @FXML
        private TextField usernameTextField;
        @FXML
        private TextField emailTextField;
        @FXML
        private TextField firstNameTextField;
        @FXML
        private TextField lastNameTextField;
        @FXML
        private TextField phoneNumberTextField;
        @FXML
        public PasswordField passwordPasswordField;
        @FXML
        public PasswordField rePasswordPasswordField;

        private final UserDatabaseRepository userDatabaseRepository = new UserDatabaseRepository();

        public void initialize() {
            setFields();
        }

        public void changeData() {
            User currUser = SessionManager.instance.getLoggedUser();
            UserFormData userFormData = new UserFormData(
                    usernameTextField.getText().trim(), emailTextField.getText().trim(),
                    firstNameTextField.getText().trim(), lastNameTextField.getText().trim(),
                    phoneNumberTextField.getText().trim(), passwordPasswordField.getText().trim(),
                    rePasswordPasswordField.getText().trim()
            );

            InputValidator.serializeSave("User: username", currUser.getUsername(), userFormData.username());
            InputValidator.serializeSave("User: email", currUser.getEmail(), userFormData.email());
            InputValidator.serializeSave("User: firstName", currUser.getFirstName(), userFormData.firstName());
            InputValidator.serializeSave("User: lastName", currUser.getLastName(), userFormData.lastName());
            InputValidator.serializeSave("User: phoneNumber", currUser.getPhoneNumber(), userFormData.phoneNumber());

            if (!userFormData.password().isBlank()) {
                String hashedNewPassword = AuthService.hashPassword(userFormData.password());
                InputValidator.serializeSave("password", currUser.getPassword(), hashedNewPassword);
            }

            UserDashboardMyProfileHelper.changeDataHandle(userFormData, userDatabaseRepository, this::setFields);
        }

        public void setFields() {
            usernameTextField.setText(SessionManager.instance.getLoggedUser().getUsername());
            emailTextField.setText(SessionManager.instance.getLoggedUser().getEmail());
            firstNameTextField.setText(SessionManager.instance.getLoggedUser().getFirstName());
            lastNameTextField.setText(SessionManager.instance.getLoggedUser().getLastName());
            phoneNumberTextField.setText(SessionManager.instance.getLoggedUser().getPhoneNumber());
        }

    }
