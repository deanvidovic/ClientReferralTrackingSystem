package hr.clientreferraltrackingsystem.repository.database;

import hr.clientreferraltrackingsystem.model.Client;
import hr.clientreferraltrackingsystem.model.User;
import hr.clientreferraltrackingsystem.service.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Repository class for managing Client entities in the database.
 */
public class ClientsDatabaseRepository {
    private static Logger logger = LoggerFactory.getLogger(ClientsDatabaseRepository.class);

    private final DatabaseManager databaseManager;

    private final UserDatabaseRepository userDatabaseRepository = new UserDatabaseRepository();

    public ClientsDatabaseRepository() {
        this.databaseManager = new DatabaseManager();
    }

    /**
     * Finds a client by their ID.
     *
     * @param id the client ID
     * @return an Optional containing the found Client or empty if not found
     */
    public Optional<Client> findById(Integer id) {
        return findAll().stream().filter(reffered -> reffered.getId().equals(id)).findFirst();
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return a Set of all clients
     */
    public Set<Client> findAll() {
        Set<Client> clients = new HashSet<>();
        String clientsQueryFindAll = "SELECT id, first_name, last_name, email, phone_number, created_by FROM referred_client";

        try (Connection connection = databaseManager.connectToDatabase();
             Statement clientsStatement = connection.createStatement();
             ResultSet resultSet = clientsStatement.executeQuery(clientsQueryFindAll)) {

            while (resultSet.next()) {
                Client client = extractClientFromResultSetAdmin(resultSet);
                clients.add(client);
            }

        } catch (IOException | SQLException e) {
            logger.error("Error occurred while reading referred clients from database", e);
        }

        return clients;
    }

    /**
     * Retrieves all clients created by a specific user.
     *
     * @param loggedUserId the ID of the user who created the clients
     * @return a Set of clients created by the user
     */
    public Set<Client> findAllByCreatedUser(Integer loggedUserId) {
        Set<Client> clients = new HashSet<>();
        String clientsQueryFindAll = "SELECT id, first_name, last_name, email, phone_number, created_by, is_currently_recommended " +
                "FROM referred_client WHERE created_by = ?";

        try (Connection connection = databaseManager.connectToDatabase();
             PreparedStatement clientsStatement = connection.prepareStatement(clientsQueryFindAll)) {

            clientsStatement.setInt(1, loggedUserId);

            try (ResultSet resultSet = clientsStatement.executeQuery()) {
                while (resultSet.next()) {
                    Client client = extractClientFromResultSetForUser(resultSet);
                    clients.add(client);
                }
            }

        } catch (IOException | SQLException e) {
            logger.error("Error occurred while reading referred clients from database", e);
        }

        return clients;
    }

    /**
     * Saves a new client in the database.
     *
     * @param entity      the client to save
     * @param loggedUserId the ID of the user creating the client
     */
    public void save(Client entity, Integer loggedUserId) {
        String clientsQuerySave =
                "INSERT INTO referred_client (first_name, last_name, email, phone_number, created_by) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = databaseManager.connectToDatabase();
             PreparedStatement clientsPreparedStatement = connection.prepareStatement(clientsQuerySave)) {

            clientsPreparedStatement.setString(1, entity.getFirstName());
            clientsPreparedStatement.setString(2, entity.getLastName());
            clientsPreparedStatement.setString(3, entity.getEmail());
            clientsPreparedStatement.setString(4, entity.getPhoneNumber());
            clientsPreparedStatement.setInt(5, loggedUserId);

            clientsPreparedStatement.executeUpdate();
        } catch (IOException | SQLException e) {
            logger.error("Error occurred while saving referred client to database", e);
        }
    }

    /**
     * Deletes a client from the database by ID.
     *
     * @param id the ID of the client to delete
     */
    public void delete(Integer id) {
        String clientsQueryDelete =
                "DELETE FROM referred_client WHERE id = ?";

        try (Connection connection = databaseManager.connectToDatabase();
             PreparedStatement clientsPreparedStatement = connection.prepareStatement(clientsQueryDelete)) {

            clientsPreparedStatement.setInt(1, id);

            clientsPreparedStatement.executeUpdate();
        } catch (IOException | SQLException e) {
            logger.error("Error occurred while deleting referred client from database", e);
        }
    }

    /**
     * Updates an existing client in the database.
     *
     * @param entity       the client entity with updated data
     * @param loggedUserId the ID of the user updating the client
     */
    public void update(Client entity, Integer loggedUserId) {
        String clientsQueryUpdate = "UPDATE referred_client " +
                "SET first_name = ?, last_name = ?, email = ?, phone_number = ?, " +
                "created_by = ?, is_currently_recommended = ? WHERE id = ?";

        try (Connection connection = databaseManager.connectToDatabase();
             PreparedStatement clientsPreparedStatement = connection.prepareStatement(clientsQueryUpdate)) {

            clientsPreparedStatement.setString(1, entity.getFirstName());
            clientsPreparedStatement.setString(2, entity.getLastName());
            clientsPreparedStatement.setString(3, entity.getEmail());
            clientsPreparedStatement.setString(4, entity.getPhoneNumber());
            clientsPreparedStatement.setInt(5, loggedUserId);
            clientsPreparedStatement.setBoolean(6, entity.getCurrentlyRecommended());
            clientsPreparedStatement.setInt(7, entity.getId());

            clientsPreparedStatement.executeUpdate();
        } catch (IOException | SQLException e) {
            logger.error("Error occurred while updating referred client in database", e);
        }
    }

    /**
     * Constructs a Client object from a ResultSet for the currently logged-in user.
     *
     * @param resultSet the ResultSet containing client data
     * @return a Client object populated with data from the ResultSet and current logged user
     * @throws SQLException if an SQL error occurs while reading from the ResultSet
     */

    private Client extractClientFromResultSetForUser(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String phoneNumber = resultSet.getString("phone_number");
        boolean isCurrentlyRecommended = resultSet.getBoolean("is_currently_recommended");

        return new Client(id, firstName, lastName, email, phoneNumber, SessionManager.instance.getLoggedUser(), isCurrentlyRecommended);
    }

    /**
     * Constructs a Client object from a ResultSet for admin view.
     *
     * @param resultSet the ResultSet containing client data
     * @return a Client object populated with data from the ResultSet
     * @throws SQLException if an SQL error occurs while reading from the ResultSet
     */

    private Client extractClientFromResultSetAdmin(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String phoneNumber = resultSet.getString("phone_number");
        User user = userDatabaseRepository.findById(resultSet.getInt("created_by")).orElse(null);

        return new Client(id, firstName, lastName, email, phoneNumber, user);
    }
}
