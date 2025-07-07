package hr.clientreferraltrackingsystem.exception;

/**
 * Custom exception thrown when an invalid password is encountered.
 */
public class InvalidPasswordException extends Exception {

    /**
     * Constructs a new InvalidPasswordException with {@code null} as its detail message.
     */
    public InvalidPasswordException() {
        super();
    }

    /**
     * Constructs a new InvalidPasswordException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidPasswordException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidPasswordException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new InvalidPasswordException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public InvalidPasswordException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new InvalidPasswordException with the specified detail message, cause,
     * suppression enabled or disabled, and writable stack trace enabled or disabled.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     * @param enableSuppression whether or not suppression is enabled
     * @param writableStackTrace whether or not the stack trace should be writable
     */
    public InvalidPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
