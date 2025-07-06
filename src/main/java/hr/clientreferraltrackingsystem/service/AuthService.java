package hr.clientreferraltrackingsystem.service;

import hr.clientreferraltrackingsystem.model.User;
import hr.clientreferraltrackingsystem.repository.database.UserDatabaseRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthService {
    private final UserDatabaseRepository userDatabaseRepository = new UserDatabaseRepository();

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

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
