package hr.clientreferraltrackingsystem.exception;

/**
 * Runtime exception thrown when two passwords do not match.
 */
public class PasswordsDoNotMatchException extends RuntimeException {

    /**
     * Constructs a new PasswordsDoNotMatchException with {@code null} as its detail message.
     */
    public PasswordsDoNotMatchException() {
        super();
    }

    /**
     * Constructs a new PasswordsDoNotMatchException with the specified detail message.
     *
     * @param message the detail message
     */
    public PasswordsDoNotMatchException(String message) {
        super(message);
    }

    /**
     * Constructs a new PasswordsDoNotMatchException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public PasswordsDoNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new PasswordsDoNotMatchException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public PasswordsDoNotMatchException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new PasswordsDoNotMatchException with the specified detail message, cause,
     * suppression enabled or disabled, and writable stack trace enabled or disabled.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     * @param enableSuppression whether or not suppression is enabled
     * @param writableStackTrace whether or not the stack trace should be writable
     */
    public PasswordsDoNotMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
