package hr.clientreferraltrackingsystem.controller.admin.helper;

import hr.clientreferraltrackingsystem.model.Client;
import hr.clientreferraltrackingsystem.repository.database.ClientsDatabaseRepository;
import hr.clientreferraltrackingsystem.utils.InputValidator;
import hr.clientreferraltrackingsystem.utils.Message;

/**
 * Helper class for performing administrative actions related to client management
 * in the admin dashboard.
 * <p>
 * This class provides utility methods for handling client-related operations such as deletion.
 * It is not intended to be instantiated.
 */
public class AdminDashboardClientsHelper {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private AdminDashboardClientsHelper() {}

    /**
     * Handles the deletion of a client from the system, including user confirmation and logging.
     * <p>
     * Prompts the user with a confirmation dialog. If confirmed, logs the deletion of the client's
     * data using the {@link InputValidator}, removes the client from the database, and executes a
     * post-deletion callback.
     *
     * @param client                 the client to be deleted
     * @param clientDatabaseRepository the repository used to perform the delete operation
     * @param afterDelete            a {@link Runnable} to be executed after successful deletion
     */
    public static void handleDelete(Client client, ClientsDatabaseRepository clientDatabaseRepository,
                                    Runnable afterDelete) {
        boolean confirmed = Message.showConfirmation(
                "Confirm Delete of Client",
                null,
                "Are you sure you want to delete client " + client.getFirstName() + "?"
        );

        if (confirmed) {
            InputValidator.serializeSave("Client - delete: ID", client.getId(), "none");
            InputValidator.serializeSave("Client - delete: First Name", client.getFirstName(), "none");
            InputValidator.serializeSave("Client - delete: Last Name", client.getLastName(), "none");
            InputValidator.serializeSave("Client - delete: Email", client.getEmail(), "none");
            InputValidator.serializeSave("Client - delete: Phone Number", client.getPhoneNumber(), "none");

            clientDatabaseRepository.delete(client.getId());
            afterDelete.run();
        }
    }
}
