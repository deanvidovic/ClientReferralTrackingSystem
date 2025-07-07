package hr.clientreferraltrackingsystem.exception;

/**
 * Exception thrown to indicate that one or more required input fields are empty.
 * <p>
 * This runtime exception is typically used during form validation when
 * user input does not meet the minimum required conditions (e.g., required fields left blank).
 */
public class EmptyFieldsException extends RuntimeException {

    /**
     * Constructs a new {@code EmptyFieldsException} with {@code null} as its detail message.
     */
    public EmptyFieldsException() {
    }

    /**
     * Constructs a new {@code EmptyFieldsException} with the specified detail message.
     *
     * @param message the detail message
     */
    public EmptyFieldsException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code EmptyFieldsException} with the specified cause.
     *
     * @param cause the cause (can be retrieved later by {@link Throwable#getCause()})
     */
    public EmptyFieldsException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code EmptyFieldsException} with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause (can be retrieved later by {@link Throwable#getCause()})
     */
    public EmptyFieldsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code EmptyFieldsException} with full control over suppression and writability.
     *
     * @param message            the detail message
     * @param cause              the cause
     * @param enableSuppression  whether suppression is enabled
     * @param writableStackTrace whether the stack trace should be writable
     */
    public EmptyFieldsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
