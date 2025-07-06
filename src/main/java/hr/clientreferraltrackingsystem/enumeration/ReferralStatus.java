package hr.clientreferraltrackingsystem.enumeration;

public enum ReferralStatus {
    PENDING("Referral is pending review."),
    APPROVED("Referral has been approved.ss"),
    REJECTED("Referral has been rejected.");

    private final String message;

    ReferralStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
