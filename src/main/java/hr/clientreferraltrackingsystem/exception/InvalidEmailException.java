package hr.clientreferraltrackingsystem.exception;

/**
 * Custom exception thrown when an invalid email is encountered.
 */
public class InvalidEmailException extends Exception {

    /**
     * Constructs a new InvalidEmailException with {@code null} as its detail message.
     */
    public InvalidEmailException() {
        super();
    }

    /**
     * Constructs a new InvalidEmailException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidEmailException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidEmailException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public InvalidEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new InvalidEmailException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public InvalidEmailException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new InvalidEmailException with the specified detail message, cause,
     * suppression enabled or disabled, and writable stack trace enabled or disabled.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     * @param enableSuppression whether suppression is enabled
     * @param writableStackTrace whether the stack trace should be writable
     */
    public InvalidEmailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
