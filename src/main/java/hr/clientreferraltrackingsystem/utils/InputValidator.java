package hr.clientreferraltrackingsystem.utils;

import hr.clientreferraltrackingsystem.exception.InvalidEmailException;
import hr.clientreferraltrackingsystem.exception.InvalidPasswordException;
import hr.clientreferraltrackingsystem.exception.PasswordsDoNotMatchException;
import hr.clientreferraltrackingsystem.serialization.ChangeLogHolder;
import hr.clientreferraltrackingsystem.serialization.ChangeLogManager;
import hr.clientreferraltrackingsystem.service.SessionManager;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Utility class for input validation, including email and password validation,
 * password matching, BigDecimal validation, and serialization of change logs.
 */
public class InputValidator {

    private InputValidator() {}

    /**
     * Validates the format of an email address.
     *
     * @param email the email address to validate
     * @param errorMessage the error message to use in the exception
     * @throws InvalidEmailException if the email format is invalid
     */
    public static void emailValidator(String email, String errorMessage) throws InvalidEmailException {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        if (!pattern.matcher(email).matches()) {
            throw new InvalidEmailException(errorMessage);
        }
    }

    /**
     * Validates the strength and format of a password.
     * Password must contain at least one lowercase letter, one uppercase letter,
     * one digit, one special character and be at least 8 characters long.
     *
     * @param password the password to validate
     * @param errorMessage the error message to use in the exception
     * @throws InvalidPasswordException if the password format is invalid
     */
    public static void passwordValidator(String password, String errorMessage) throws InvalidPasswordException {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!password.matches(pattern)) {
            throw new InvalidPasswordException(errorMessage);
        }
    }

    /**
     * Checks if two passwords match.
     *
     * @param password the original password
     * @param rePassword the password to compare
     * @param errorMessage the error message to use in the exception
     * @throws PasswordsDoNotMatchException if the passwords do not match
     */
    public static void passwordMatcher(String password, String rePassword, String errorMessage) throws PasswordsDoNotMatchException {
        if (!password.equals(rePassword)) {
            throw new PasswordsDoNotMatchException(errorMessage);
        }
    }

    /**
     * Validates if the given input string is a valid BigDecimal representation.
     *
     * @param input the input string to validate
     * @return true if the input is a valid BigDecimal, false otherwise
     */
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

    /**
     * Serializes a change if the old and new values differ.
     *
     * @param fieldChanged the name of the changed field
     * @param oldValue the old value of the field
     * @param newValue the new value of the field
     */
    public static void serializeSave(String fieldChanged, Object oldValue, Object newValue){
        if (oldValue != null && newValue != null && !oldValue.toString().equals(newValue.toString())) {
            ChangeLogManager.serializeChange(new ChangeLogHolder(fieldChanged, oldValue.toString(), newValue.toString(), SessionManager.instance.getLoggedUser().getRole().name(), LocalDateTime.now()));
        }
    }
}
