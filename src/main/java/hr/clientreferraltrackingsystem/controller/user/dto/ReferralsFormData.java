package hr.clientreferraltrackingsystem.controller.user.dto;

import hr.clientreferraltrackingsystem.enumeration.ReferralStatus;

import java.time.LocalDateTime;

public record ReferralsFormData(
        String referredClientFirstName,
        String referredClientLastName,
        String referredClientEmail,
        String referredClientPhoneNumber,
        ReferralStatus referralStatus,
        LocalDateTime referralData
) {
}
