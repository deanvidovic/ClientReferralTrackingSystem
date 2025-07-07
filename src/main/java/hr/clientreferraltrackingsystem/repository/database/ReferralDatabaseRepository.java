package hr.clientreferraltrackingsystem.repository.database;

import hr.clientreferraltrackingsystem.enumeration.ReferralStatus;
import hr.clientreferraltrackingsystem.model.Referral;
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
 * Repository class for performing CRUD operations on Referral entities in the database.
 * It supports saving, updating, deleting, and querying referrals.
 * Access to database operations is synchronized to prevent concurrent modification.
 */
public class ReferralDatabaseRepository {
    private static Logger logger = LoggerFactory.getLogger(ReferralDatabaseRepository.class);
    final ClientsDatabaseRepository clientsDatabaseRepository = new ClientsDatabaseRepository();
    final UserDatabaseRepository userDatabaseRepository = new UserDatabaseRepository();
    private static Boolean databaseAccessInProgress = false;
    private final DatabaseManager databaseManager;

    /**
     * Constructs a ReferralDatabaseRepository instance.
     */
    public ReferralDatabaseRepository() {
        this.databaseManager = new DatabaseManager();
    }

    /**
     * Saves a new Referral entity to the database.
     *
     * @param entity the Referral to be saved
     */
    public synchronized void save(Referral entity) {
        while (Boolean.TRUE.equals(databaseAccessInProgress)) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Error occurred while waiting for database access", e);
            }
        }
        databaseAccessInProgress = true;

        String referralQuerySave = "INSERT INTO referral (referrer_user_id, referred_client_id, status) VALUES (?, ?, ?)";
        try (Connection connection = databaseManager.connectToDatabase(); PreparedStatement referralsPreparedStatement = connection.prepareStatement(referralQuerySave);) {
            referralsPreparedStatement.setInt(1, entity.getRefferer().getId());
            referralsPreparedStatement.setInt(2, entity.getRefferedClient().getId());
            referralsPreparedStatement.setString(3, String.valueOf(entity.getReferralStatus()));

            referralsPreparedStatement.executeUpdate();
        } catch (IOException | SQLException e) {
            logger.error("Error occurred while saving referral to database in save", e);
        } finally {
            databaseAccessInProgress = false;
            notifyAll();
        }
    }

    /**
     * Updates the status of an existing referral by its ID.
     *
     * @param referralId the ID of the referral to update
     * @param newStatus  the new ReferralStatus to set
     */
    public synchronized void updateStatus(Integer referralId, ReferralStatus newStatus) {
        while (Boolean.TRUE.equals(databaseAccessInProgress)) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Error occurred while waiting for database access in updateStatus", e);
            }
        }
        databaseAccessInProgress = true;

        String updateStatusQuery = "UPDATE referral SET status = ? WHERE id = ?";
        try (Connection connection = databaseManager.connectToDatabase(); PreparedStatement statement = connection.prepareStatement(updateStatusQuery)) {
            statement.setString(1, newStatus.name());
            statement.setInt(2, referralId);
            statement.executeUpdate();
        } catch (IOException | SQLException e) {
            logger.error("Error occurred while updating referral status", e);
        } finally {
            databaseAccessInProgress = false;
            notifyAll();
        }
    }

    /**
     * Retrieves all referrals made by a specific user.
     *
     * @param loggedUserId the ID of the logged-in user (referrer)
     * @return a set of Referral objects made by the user
     */
    public synchronized Set<Referral> findAllByReferral(Integer loggedUserId) {
        while (Boolean.TRUE.equals(databaseAccessInProgress)) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Error occurred while waiting for database  in findAllByReferral", e);
            }
        }
        databaseAccessInProgress = true;

        Set<Referral> referrals = new HashSet<>();
        String referralsQueryFindAll = "SELECT id, referred_client_id, status, created_at " +
                "FROM referral WHERE referrer_user_id = ?";

        try (Connection connection = databaseManager.connectToDatabase(); PreparedStatement referralsStatement = connection.prepareStatement(referralsQueryFindAll)) {
            referralsStatement.setInt(1, loggedUserId);

            try (ResultSet resultSet = referralsStatement.executeQuery()) {
                while (resultSet.next()) {
                    Referral referral = extractClientFromResultSetForCurrentUser(resultSet);
                    referrals.add(referral);
                }
            }
        } catch (IOException | SQLException e) {
            logger.error("Error occurred while reading referrals from database", e);
        } finally {
            databaseAccessInProgress = false;
            notifyAll();
        }
        return referrals;
    }

    /**
     * Retrieves all referrals from the database.
     *
     * @return a set of all Referral objects
     */
    public synchronized Set<Referral> findAll() {
        while (Boolean.TRUE.equals(databaseAccessInProgress)) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Error occurred while waiting for database access in findAll", e);
            }
        }
        databaseAccessInProgress = true;

        Set<Referral> referrals = new HashSet<>();
        String referralsQueryFindAll = "SELECT id, referrer_user_id, referred_client_id, status, created_at FROM referral";
        try (Connection connection = databaseManager.connectToDatabase(); Statement referralsStatement = connection.createStatement()) {
            try (ResultSet resultSet = referralsStatement.executeQuery(referralsQueryFindAll)) {
                while (resultSet.next()) {
                    Referral referral = extractClientFromResultSetForAdmin(resultSet);
                    referrals.add(referral);
                }
            }
        } catch (IOException | SQLException e) {
            logger.error("Error occurred while reading referrals from database", e);
        } finally {
            databaseAccessInProgress = false;
            notifyAll();
        }
        return referrals;
    }

    /**
     * Finds a referral by its ID.
     *
     * @param id the ID of the referral to find
     * @return the Referral if found, or null otherwise
     */
    public Referral findById(Integer id) {
        return findAll().stream().filter(referral -> referral.getId().equals(id)).findAny().orElse(null);
    }

    /**
     * Deletes a referral from the database by its ID.
     *
     * @param id the ID of the referral to delete
     */
    public synchronized void delete(Integer id) {
        while (Boolean.TRUE.equals(databaseAccessInProgress)) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Error occurred while waiting for database access in delete", e);
            }
        }
        databaseAccessInProgress = true;

        String referralsQueryDelete = "DELETE FROM referral WHERE id = ?";
        try (Connection connection = databaseManager.connectToDatabase(); PreparedStatement referralsPreparedStatement = connection.prepareStatement(referralsQueryDelete);) {
            referralsPreparedStatement.setInt(1, id);
            referralsPreparedStatement.executeUpdate();
        } catch (IOException | SQLException e) {
            logger.error("Error occurred while deleting referrals from database", e);
        } finally {
            databaseAccessInProgress = false;
            notifyAll();
        }
    }

    /**
     * Finds the most recently created referral from the database.
     *
     * @return an Optional containing the latest Referral, or empty if none found
     */
    public synchronized Optional<Referral> findLatestReferralFromDb() {
        while (Boolean.TRUE.equals(databaseAccessInProgress)) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Error occurred while waiting for database access findLatestReferralFromDb", e);
            }
        }
        databaseAccessInProgress = true;

        String query = "SELECT id, referrer_user_id, referred_client_id, status, created_at FROM referral ORDER BY created_at DESC LIMIT 1";
        try (Connection connection = databaseManager.connectToDatabase(); Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return Optional.of(extractClientFromResultSetForAdmin(resultSet));
            }
        } catch (IOException | SQLException e) {
            logger.error("Error occurred while fetching latest referral", e);
        } finally {
            databaseAccessInProgress = false;
            notifyAll();
        }
        return Optional.empty();
    }

    /**
     * Extracts a Referral object for the currently logged-in user from the ResultSet.
     *
     * @param resultSet the ResultSet containing referral data
     * @return the constructed Referral object
     * @throws SQLException if an SQL error occurs while reading from the ResultSet
     */
    private Referral extractClientFromResultSetForCurrentUser(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        Client client = clientsDatabaseRepository.findById(resultSet.getInt("referred_client_id")).orElse(null);
        ReferralStatus status = ReferralStatus.valueOf(resultSet.getString("status"));
        Date createdAt = resultSet.getDate("created_at");
        return new Referral(id, SessionManager.instance.getLoggedUser(), client, createdAt.toLocalDate().atStartOfDay(), status);
    }

    /**
     * Extracts a Referral object for admin view from the ResultSet.
     *
     * @param resultSet the ResultSet containing referral data
     * @return the constructed Referral object
     * @throws SQLException if an SQL error occurs while reading from the ResultSet
     */
    private Referral extractClientFromResultSetForAdmin(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        User user = userDatabaseRepository.findById(resultSet.getInt("referrer_user_id")).orElse(null);
        Client client = clientsDatabaseRepository.findById(resultSet.getInt("referred_client_id")).orElse(null);
        ReferralStatus status = ReferralStatus.valueOf(resultSet.getString("status"));
        Date createdAt = resultSet.getDate("created_at");
        return new Referral(id, user, client, createdAt.toLocalDate().atStartOfDay(), status);
    }
}
