package hr.clientreferraltrackingsystem.repository.database;

import hr.clientreferraltrackingsystem.enumeration.Role;
import hr.clientreferraltrackingsystem.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Repository class for managing User entities in the database.
 */
public class UserDatabaseRepository {
    private static Logger logger = LoggerFactory.getLogger(UserDatabaseRepository.class);

    private final DatabaseManager databaseManager;

    /**
     * Constructs a new UserDatabaseRepository and initializes the DatabaseManager.
     */
    public UserDatabaseRepository() {
        this.databaseManager = new DatabaseManager();
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return an Optional containing the found User or empty if not found
     */
    public Optional<User> findByUsername(String username) {
        return findAll()
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the user ID
     * @return an Optional containing the found User or empty if not found
     */
    public Optional<User> findById(Integer id) {
        return findAll().stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a Set of all User entities
     */
    public Set<User> findAll() {
        Set<User> users = new HashSet<>();
        String usersQueryFindAll = "SELECT id, username, password, email, first_name, last_name, phone_number, role FROM users";

        try (Connection connection = databaseManager.connectToDatabase();
             Statement usersStatement = connection.createStatement();
             ResultSet resultSet = usersStatement.executeQuery(usersQueryFindAll)) {

            while (resultSet.next()) {
                User user = extractUserFromResultSet(resultSet);
                users.add(user);
            }

        } catch (IOException | SQLException e) {
            logger.error("Error occurred while reading users from database", e);
        }

        return users;
    }

    /**
     * Saves a new user to the database.
     *
     * @param entity the User entity to save
     */
    public void save(User entity) {
        String usersQuerySave =
                "INSERT INTO users (username, password, email, first_name, last_name, phone_number, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = databaseManager.connectToDatabase();
             PreparedStatement usersPreparedStatement = connection.prepareStatement(usersQuerySave)) {

            usersPreparedStatement.setString(1, entity.getUsername());
            usersPreparedStatement.setString(2, entity.getPassword());
            usersPreparedStatement.setString(3, entity.getEmail());
            usersPreparedStatement.setString(4, entity.getFirstName());
            usersPreparedStatement.setString(5, entity.getLastName());
            usersPreparedStatement.setString(6, entity.getPhoneNumber());
            usersPreparedStatement.setString(7, entity.getRole().toString());

            usersPreparedStatement.executeUpdate();
        } catch (IOException | SQLException e) {
            logger.error("Error occurred while saving users to database", e);
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     */
    public void delete(Integer id) {
        String usersQueryDelete = "DELETE FROM users WHERE id = ?";

        try (Connection connection = databaseManager.connectToDatabase();
             PreparedStatement usersPreparedStatement = connection.prepareStatement(usersQueryDelete)) {

            usersPreparedStatement.setInt(1, id);

            usersPreparedStatement.executeUpdate();
        } catch (IOException | SQLException e) {
            logger.error("Error occurred while deleting user from database", e);
        }
    }

    /**
     * Updates an existing user in the database.
     *
     * @param entity the User entity with updated data
     */
    public void update(User entity) {
        String usersQueryUpdate = "UPDATE users SET username = ?, password = ?, email = ?, first_name = ?, last_name = ?, phone_number = ?, role = ? WHERE id = ?";

        try (Connection connection = databaseManager.connectToDatabase();
             PreparedStatement usersPreparedStatement = connection.prepareStatement(usersQueryUpdate)) {

            usersPreparedStatement.setString(1, entity.getUsername());
            usersPreparedStatement.setString(2, entity.getPassword());
            usersPreparedStatement.setString(3, entity.getEmail());
            usersPreparedStatement.setString(4, entity.getFirstName());
            usersPreparedStatement.setString(5, entity.getLastName());
            usersPreparedStatement.setString(6, entity.getPhoneNumber());
            usersPreparedStatement.setString(7, entity.getRole().toString());
            usersPreparedStatement.setInt(8, entity.getId());

            usersPreparedStatement.executeUpdate();

        } catch (IOException | SQLException e) {
            logger.error("Error occurred while updating user in database", e);
        }
    }

    /**
     * Extracts a User entity from the current row of the given ResultSet.
     *
     * @param resultSet the ResultSet positioned at the current row
     * @return a User entity populated with data from the ResultSet
     * @throws SQLException if a database access error occurs
     */
    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String email = resultSet.getString("email");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String phoneNumber = resultSet.getString("phone_number");
        Role role = Role.valueOf(resultSet.getString("role"));

        return new User.UserBuilder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .role(role)
                .build();
    }
}
