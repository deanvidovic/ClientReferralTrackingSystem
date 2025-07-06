package hr.clientreferraltrackingsystem.controller.admin.helper;

import hr.clientreferraltrackingsystem.controller.admin.dto.AdminUsersFormData;
import hr.clientreferraltrackingsystem.exception.EmptyFieldsException;
import hr.clientreferraltrackingsystem.exception.InvalidEmailException;
import hr.clientreferraltrackingsystem.exception.InvalidPasswordException;
import hr.clientreferraltrackingsystem.exception.PasswordsDoNotMatchException;
import hr.clientreferraltrackingsystem.model.User;
import hr.clientreferraltrackingsystem.repository.database.UserDatabaseRepository;
import hr.clientreferraltrackingsystem.service.AuthService;
import hr.clientreferraltrackingsystem.utils.InputValidator;
import hr.clientreferraltrackingsystem.utils.Message;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AdminDashboardUserHelper {
    private static final Logger log = LoggerFactory.getLogger(AdminDashboardUserHelper.class);

    private AdminDashboardUserHelper() {}

    public static void handleSaveOrEdit(
            AdminUsersFormData adminUsersFormData,
            UserDatabaseRepository userDatabaseRepository,
            User selectedUser
    ) {

        boolean confirmed = Message.showConfirmation(
                "Confirm Delete",
                null,
                "Are you sure you want to change user data?"
        );
        if (confirmed) {
            if (!validateForm(adminUsersFormData)) return;

            if (selectedUser != null) {
                String finalPassword = adminUsersFormData.password().isEmpty() ? selectedUser.getPassword() : AuthService.hashPassword(adminUsersFormData.password());

                User updatedUser = new User.UserBuilder()
                        .id(selectedUser.getId())
                        .username(adminUsersFormData.username())
                        .password(finalPassword)
                        .email(adminUsersFormData.email())
                        .firstName(adminUsersFormData.firstName())
                        .lastName(adminUsersFormData.lastName())
                        .phoneNumber(adminUsersFormData.phoneNumber())
                        .role(adminUsersFormData.role())
                        .build();

                userDatabaseRepository.update(updatedUser);
                Message.showAlert(Alert.AlertType.INFORMATION, "Success", null, "User updated successfully!");
            } else {
                if (adminUsersFormData.password().isEmpty()) {
                    Message.showAlert(Alert.AlertType.ERROR, "Error while creating new account!",
                            null, "Password is required for new users!");
                    return;
                }

                User newUser = new User.UserBuilder()
                        .username(adminUsersFormData.username())
                        .password(AuthService.hashPassword(adminUsersFormData.password()))
                        .email(adminUsersFormData.email())
                        .firstName(adminUsersFormData.firstName())
                        .lastName(adminUsersFormData.lastName())
                        .phoneNumber(adminUsersFormData.phoneNumber())
                        .role(adminUsersFormData.role())
                        .build();

                userDatabaseRepository.save(newUser);
                Message.showAlert(Alert.AlertType.INFORMATION, "Success",
                        null, "User added successfully!");
            }
        }
    }

    private static boolean validateForm(AdminUsersFormData adminUsersFormData) {
        try {
            isFormEmpty(adminUsersFormData);
            if (!adminUsersFormData.email().isEmpty()) {
                InputValidator.emailValidator(adminUsersFormData.email(), "Invalid e-mail address!");
            }
            if (!adminUsersFormData.password().isEmpty() && !adminUsersFormData.rePassword().isEmpty()) {
                InputValidator.passwordMatcher(adminUsersFormData.password(), adminUsersFormData.rePassword(), "Passwords do not match!");
                InputValidator.passwordValidator(adminUsersFormData.password(), "Not valid password!");
            }
            return true;
        } catch (InvalidEmailException e) {
            log.error(e.getMessage(), "E-mail address is invalid!");
            Message.showAlert(Alert.AlertType.ERROR, "Invalid!",
                    null, "E-mail address is invalid!");
            return false;
        } catch (InvalidPasswordException e) {
            log.error(e.getMessage(), "Password is not valid!");
            Message.showAlert(Alert.AlertType.ERROR, "Invalid!",
                    null, "Password is not valid!");
            return false;
        } catch (PasswordsDoNotMatchException e) {
            log.error(e.getMessage(), "Passwords do not match!");
            Message.showAlert(Alert.AlertType.ERROR, "Not matching passwords!",
                    null, "Password and re-password do not match!");
            return false;
        } catch (EmptyFieldsException e) {
            log.error(e.getMessage(), "Empty fields!");
            Message.showAlert(Alert.AlertType.ERROR, "Required fields are empty!",
                    null, "All fields are required!");
            return false;
        }
    }

    public static void handleDelete(User user, UserDatabaseRepository userDatabaseRepository, Runnable afterDelete) {
        boolean confirmed = Message.showConfirmation(
                "Confirm Delete",
                null,
                "Are you sure you want to delete user " + user.getUsername() + "?"
        );

        if (confirmed) {
            userDatabaseRepository.delete(user.getId());
            afterDelete.run();
        }
    }

    private static void isFormEmpty(AdminUsersFormData formData) {
        Map<String, String> fields = Map.of(
                "First Name", formData.firstName(),
                "Last Name", formData.lastName(),
                "Email", formData.email(),
                "Phone Number", formData.phoneNumber(),
                "Username", formData.username()
        );

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                throw new EmptyFieldsException(entry.getKey() + " is required!");
            }
        }

        if (formData.role() == null) {
            throw new EmptyFieldsException("Role is required!");
        }
    }

}
