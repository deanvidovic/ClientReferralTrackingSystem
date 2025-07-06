package hr.clientreferraltrackingsystem.controller.user.helper;

import hr.clientreferraltrackingsystem.controller.user.dto.UserFormData;
import hr.clientreferraltrackingsystem.exception.InvalidEmailException;
import hr.clientreferraltrackingsystem.exception.InvalidPasswordException;
import hr.clientreferraltrackingsystem.exception.PasswordsDoNotMatchException;
import hr.clientreferraltrackingsystem.model.User;
import hr.clientreferraltrackingsystem.repository.database.UserDatabaseRepository;
import hr.clientreferraltrackingsystem.service.AuthService;
import hr.clientreferraltrackingsystem.service.SessionManager;
import hr.clientreferraltrackingsystem.utils.InputValidator;
import hr.clientreferraltrackingsystem.utils.Message;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDashboardMyProfileHelper {

    private static final Logger log = LoggerFactory.getLogger(UserDashboardMyProfileHelper.class);

    private UserDashboardMyProfileHelper() {}

    public static void changeDataHandle(UserFormData userFormData, UserDatabaseRepository userDatabaseRepository, Runnable after) {
        User user = SessionManager.getInstance().getLoggedUser();

        boolean confirmed = Message.showConfirmation(
                "Confirm Delete",
                null,
                "Are you sure you want to change your data?"
        );

        if (!confirmed) {
            return;
        }

        User.UserBuilder userBuilder = user.toBuilder().id(user.getId());
        boolean isChanged = applyChanges(userFormData, user, userBuilder);

        if (!isChanged) {
            Message.showAlert(Alert.AlertType.ERROR, "Error!", null, "You haven't changed any data!");
            after.run();
            return;
        }

        User updatedUser = userBuilder.build();
        userDatabaseRepository.update(updatedUser);
        SessionManager.getInstance().setLoggedUser(updatedUser);

        Message.showAlert(Alert.AlertType.CONFIRMATION, "Success!", null, "Successfully changed your data!");
    }

    private static boolean applyChanges(UserFormData userFormData, User user, User.UserBuilder userBuilder) {
        boolean isChanged = false;

        if (!userFormData.username().equals(user.getUsername())) {
            userBuilder.username(userFormData.username());
            isChanged = true;
        }

        if (!userFormData.email().equals(user.getEmail())) {
            try {
                InputValidator.emailValidator(userFormData.email(), "E-mail is not valid!");
                userBuilder.email(userFormData.email());
                isChanged = true;
            } catch (InvalidEmailException e) {
                log.error(e.getMessage(), "E-mail address is invalid!");
                Message.showAlert(Alert.AlertType.ERROR, "Invalid!",
                        null, "E-mail address is invalid!");
                return false;
            }
        }
        if (!userFormData.firstName().equals(user.getFirstName())) {
            userBuilder.firstName(userFormData.firstName());
            isChanged = true;
        }
        if (!userFormData.lastName().equals(user.getLastName())) {
            userBuilder.lastName(userFormData.lastName());
            isChanged = true;
        }
        if (!userFormData.phoneNumber().equals(user.getPhoneNumber())) {
            userBuilder.phoneNumber(userFormData.phoneNumber());
            isChanged = true;
        }

        isChanged = passwordVerify(isChanged, userFormData, userBuilder);

        return isChanged;
    }

    public static boolean passwordVerify(Boolean isChanged, UserFormData userFormData, User.UserBuilder userBuilder) {
        String newPassword = userFormData.password();
        String reEnteredPassword = userFormData.rePassword();

        if (!newPassword.isEmpty() || !reEnteredPassword.isEmpty()) {
            try {
                InputValidator.passwordMatcher(newPassword, reEnteredPassword, "Passwords do not match!");
                InputValidator.passwordValidator(newPassword, "Password is not valid!");
                String passwordHash = AuthService.hashPassword(newPassword);
                userBuilder.password(passwordHash);
                return true;
            } catch (PasswordsDoNotMatchException e) {
                log.error(e.getMessage(), "Passwords do not match!");
                Message.showAlert(Alert.AlertType.ERROR, "Not matching passwords!",
                        null, "Password and re-password do not match!");
            } catch (InvalidPasswordException e) {
                log.error(e.getMessage(), "Password is not valid it doesnt meet requirements!");
                Message.showAlert(Alert.AlertType.ERROR, "Invalid!",
                        null, "Password is not valid it doesnt meet requirements!");
            }
        }
        return isChanged;
    }
}
