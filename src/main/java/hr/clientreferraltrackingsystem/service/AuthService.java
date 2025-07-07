package hr.clientreferraltrackingsystem.service;

import hr.clientreferraltrackingsystem.model.User;
import hr.clientreferraltrackingsystem.repository.database.UserDatabaseRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

/**
 * Service class responsible for user authentication.
 */
public class AuthService {
    private final UserDatabaseRepository userDatabaseRepository = new UserDatabaseRepository();

    /**
     * Attempts to authenticate a user by username and password.
     *
     * @param username the username of the user attempting to log in
     * @param password the plaintext password provided by the user
     * @return an {@link Optional} containing the authenticated {@link User} if successful,
     *         or empty if authentication fails
     */
    public Optional<User> login(String username, String password) {
        Optional<User> userOptional = userDatabaseRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (BCrypt.checkpw(password, user.getPassword())) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    /**
     * Hashes a plaintext password using BCrypt.
     *
     * @param password the plaintext password to hash
     * @return the hashed password
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
