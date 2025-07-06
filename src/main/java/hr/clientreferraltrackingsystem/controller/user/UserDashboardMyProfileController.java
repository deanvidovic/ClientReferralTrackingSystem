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
            User currUser = SessionManager.getInstance().getLoggedUser();
            UserFormData userFormData = new UserFormData(
                    usernameTextField.getText().trim(), emailTextField.getText().trim(),
                    firstNameTextField.getText().trim(), lastNameTextField.getText().trim(),
                    phoneNumberTextField.getText().trim(), passwordPasswordField.getText().trim(),
                    rePasswordPasswordField.getText().trim()
            );

            InputValidator.isDataEdited("User: username", currUser.getUsername(), userFormData.username());
            InputValidator.isDataEdited("User: email", currUser.getEmail(), userFormData.email());
            InputValidator.isDataEdited("User: firstName", currUser.getFirstName(), userFormData.firstName());
            InputValidator.isDataEdited("User: lastName", currUser.getLastName(), userFormData.lastName());
            InputValidator.isDataEdited("User: phoneNumber", currUser.getPhoneNumber(), userFormData.phoneNumber());

            if (!userFormData.password().isBlank()) {
                String hashedNewPassword = AuthService.hashPassword(userFormData.password());
                InputValidator.isDataEdited("password", currUser.getPassword(), hashedNewPassword);
            }

            UserDashboardMyProfileHelper.changeDataHandle(userFormData, userDatabaseRepository, this::setFields);
        }

        public void setFields() {
            usernameTextField.setText(SessionManager.getInstance().getLoggedUser().getUsername());
            emailTextField.setText(SessionManager.getInstance().getLoggedUser().getEmail());
            firstNameTextField.setText(SessionManager.getInstance().getLoggedUser().getFirstName());
            lastNameTextField.setText(SessionManager.getInstance().getLoggedUser().getLastName());
            phoneNumberTextField.setText(SessionManager.getInstance().getLoggedUser().getPhoneNumber());
        }

    }
