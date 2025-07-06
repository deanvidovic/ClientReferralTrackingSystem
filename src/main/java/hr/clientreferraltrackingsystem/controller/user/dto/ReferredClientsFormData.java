package hr.clientreferraltrackingsystem.controller.user.dto;

public record ReferredClientsFormData(
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {
}
