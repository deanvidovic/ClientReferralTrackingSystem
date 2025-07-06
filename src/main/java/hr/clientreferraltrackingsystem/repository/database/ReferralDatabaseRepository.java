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

public class ReferralDatabaseRepository {
    private static Logger logger = LoggerFactory.getLogger(ReferralDatabaseRepository.class);
    final ClientsDatabaseRepository clientsDatabaseRepository = new ClientsDatabaseRepository();
    final UserDatabaseRepository userDatabaseRepository = new UserDatabaseRepository();

    private static Boolean databaseAccessInProgress = false;

    private final DatabaseManager databaseManager;

    public ReferralDatabaseRepository() {
        this.databaseManager = new DatabaseManager();
    }

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

        String referralQuerySave =
                "INSERT INTO referral (referrer_user_id, referred_client_id, status) VALUES (?, ?, ?)";

        try (Connection connection = databaseManager.connectToDatabase();
             PreparedStatement referralsPreparedStatement = connection.prepareStatement(referralQuerySave);) {

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

        try (Connection connection = databaseManager.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(updateStatusQuery)) {

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

        try (Connection connection = databaseManager.connectToDatabase();
             PreparedStatement referralsStatement = connection.prepareStatement(referralsQueryFindAll)) {

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

        try (Connection connection = databaseManager.connectToDatabase();
             Statement referralsStatement = connection.createStatement()) {

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

    public Referral findById(Integer id) {
        return findAll().stream().filter(referral -> referral.getId().equals(id)).findAny().orElse(null);
    }


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

        String referralsQueryDelete =
                "DELETE FROM referral WHERE id = ?";

        try (Connection connection = databaseManager.connectToDatabase();
             PreparedStatement referralsPreparedStatement = connection.prepareStatement(referralsQueryDelete);) {

            referralsPreparedStatement.setInt(1, id);

            referralsPreparedStatement.executeUpdate();
        } catch (IOException | SQLException e) {
            logger.error("Error occurred while deleting referrals from database", e);
        } finally {
            databaseAccessInProgress = false;
            notifyAll();
        }
    }

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

        String query = "SELECT id, referrer_user_id, referred_client_id, status, created_at " +
                "FROM referral ORDER BY created_at DESC LIMIT 1";

        try (Connection connection = databaseManager.connectToDatabase();
             Statement statement = connection.createStatement();
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

    private Referral extractClientFromResultSetForCurrentUser(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        Optional<Client> referredClientOptional = clientsDatabaseRepository.findById(resultSet.getInt("referred_client_id"));
        Client client = referredClientOptional.orElse(null);
        ReferralStatus status = ReferralStatus.valueOf(resultSet.getString("status"));
        Date createdAt = resultSet.getDate("created_at");

        return new Referral(
                id,
                SessionManager.getInstance().getLoggedUser(),
                client,
                createdAt.toLocalDate().atStartOfDay(),
                status
        );
    }

    private Referral extractClientFromResultSetForAdmin(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");

        Optional<User> userOptional = userDatabaseRepository.findById(resultSet.getInt("referrer_user_id"));
        User user = userOptional.orElse(null);

        Optional<Client> referredClientOptional = clientsDatabaseRepository.findById(resultSet.getInt("referred_client_id"));
        Client client = referredClientOptional.orElse(null);

        ReferralStatus status = ReferralStatus.valueOf(resultSet.getString("status"));
        Date createdAt = resultSet.getDate("created_at");

        return new Referral(
                id,
                user,
                client,
                createdAt.toLocalDate().atStartOfDay(),
                status
        );
    }
}
