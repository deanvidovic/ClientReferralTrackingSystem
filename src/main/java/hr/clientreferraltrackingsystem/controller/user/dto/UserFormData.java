package hr.clientreferraltrackingsystem.controller.user.dto;

public record UserFormData(
        String username,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String password,
        String rePassword
) { }
