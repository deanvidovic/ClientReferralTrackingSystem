package hr.clientreferraltrackingsystem.utils;

import hr.clientreferraltrackingsystem.exception.InvalidEmailException;
import hr.clientreferraltrackingsystem.exception.InvalidPasswordException;
import hr.clientreferraltrackingsystem.exception.PasswordsDoNotMatchException;
import hr.clientreferraltrackingsystem.serialization.ChangeLogHolder;
import hr.clientreferraltrackingsystem.serialization.ChangeLogManager;
import hr.clientreferraltrackingsystem.service.SessionManager;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class InputValidator {

    private InputValidator() {}

    public static void emailValidator(String email, String errorMessage) throws InvalidEmailException {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        if (!pattern.matcher(email).matches()) {
            throw new InvalidEmailException(errorMessage);
        }
    }

    public static void passwordValidator(String password, String errorMessage) throws InvalidPasswordException {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!password.matches(pattern)) {
            throw new InvalidPasswordException(errorMessage);
        }
    }

    public static void passwordMatcher(String password, String rePassword, String errorMessage) throws PasswordsDoNotMatchException {
        if (!password.equals(rePassword)) {
            throw new PasswordsDoNotMatchException(errorMessage);
        }
    }

    public static boolean isValidBigDecimal(String input) {
        if (input == null) {
            return false;
        }
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return false;
        }
        String decimalRegex = "-?(?:\\d+(?:\\.\\d+)?|\\.\\d+)";
        return trimmed.matches(decimalRegex);
    }

    public static void isDataEdited(String fieldChanged, Object oldValue, Object newValue){
        if (oldValue != null && newValue != null && !oldValue.toString().equals(newValue.toString())) {
            ChangeLogManager.serializeChange(new ChangeLogHolder(fieldChanged, oldValue.toString(), newValue.toString(), SessionManager.getInstance().getLoggedUser().getRole().name(), LocalDateTime.now()));
        }
    }
}

