package hr.clientreferraltrackingsystem.controller.admin.dto;

import hr.clientreferraltrackingsystem.enumeration.Role;

/**
 * DTO (Data Transfer Object) used for collecting and transferring user form data
 * related to administrative user management operations.
 * <p>
 * This record is typically used when creating or editing users via the admin interface.
 * </p>
 *
 * @param firstName  The first name of the user.
 * @param lastName   The last name of the user.
 * @param email      The email address of the user.
 * @param phoneNumber The phone number of the user.
 * @param username   The unique username used for login/authentication.
 * @param role       The role of the user in the system (e.g., ADMIN, USER).
 * @param password   The password for the user's account.
 * @param rePassword Confirmation of the password, typically used for validation during input.
 */
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
