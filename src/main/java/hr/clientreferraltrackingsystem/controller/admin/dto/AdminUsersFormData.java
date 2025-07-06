package hr.clientreferraltrackingsystem.controller.admin.dto;

import hr.clientreferraltrackingsystem.enumeration.Role;

public record AdminUsersFormData(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String username,
        Role role,
        String password,
        String rePassword
) {
}
