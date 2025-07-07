package hr.clientreferraltrackingsystem.enumeration;

/**
 * Enumeration representing the status of a referral.
 * Each status is associated with a descriptive message.
 */
public enum ReferralStatus {
    PENDING("Referral is pending review."),
    APPROVED("Referral has been approved.ss"),
    REJECTED("Referral has been rejected.");

    private final String message;

    /**
     * Constructs a {@code ReferralStatus} with an associated message.
     *
     * @param message the descriptive message of the status
     */
    ReferralStatus(String message) {
        this.message = message;
    }

    /**
     * Returns the descriptive message associated with this status.
     *
     * @return the status message
     */
    public String getMessage() {
        return message;
    }
}
