package hr.clientreferraltrackingsystem.controller.admin.helper;

import hr.clientreferraltrackingsystem.model.Client;
import hr.clientreferraltrackingsystem.repository.database.ClientsDatabaseRepository;
import hr.clientreferraltrackingsystem.utils.Message;

public class AdminDashboardClientsHelper {

    private AdminDashboardClientsHelper() {}

    public static void handleDelete(Client client, ClientsDatabaseRepository clientDatabaseRepository,
                                    Runnable afterDelete) {
        boolean confirmed = Message.showConfirmation(
                "Confirm Delete of Client",
                null,
                "Are you sure you want to delete client " + client.getFirstName() + "?"
        );

        if (confirmed) {
            clientDatabaseRepository.delete(client.getId());
            afterDelete.run();
        }
    }
}
