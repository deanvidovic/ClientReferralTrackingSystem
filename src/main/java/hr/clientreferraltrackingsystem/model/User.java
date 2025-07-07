package hr.clientreferraltrackingsystem.model;

import hr.clientreferraltrackingsystem.enumeration.Role;
import hr.clientreferraltrackingsystem.interfaces.Rewardable;
import hr.clientreferraltrackingsystem.repository.dat.AbstractRepository;
import hr.clientreferraltrackingsystem.repository.dat.RewardRepository;

import java.util.List;
import java.util.Objects;

/**
 * Represents a user in the client referral tracking system.
 * Implements Rewardable to provide access to the user's rewards.
 */
public non-sealed class User extends Entity implements Rewardable {
    private final String username;
    private final String password;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final Role role;

    /**
     * Constructs a User instance from the given UserBuilder.
     *
     * @param builder the builder containing user details
     */
    private User(UserBuilder builder) {
        super(builder.id);
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.phoneNumber = builder.phoneNumber;
        this.role = builder.role;
    }

    /**
     * Returns the username of the user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of the user.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the first name of the user.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of the user.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the phone number of the user.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns the role of the user.
     *
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Returns a list of rewards associated with this user.
     *
     * @return list of rewards
     */
    @Override
    public List<Reward> getRewards() {
        AbstractRepository<Reward> repos = new RewardRepository<>();
        return repos.findAllById(super.getId());
    }

    /**
     * Builder class for constructing User instances.
     */
    public static class UserBuilder {
        private Integer id;
        private String username;
        private String password;
        private String email;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private Role role;

        public UserBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        /**
         * Builds and returns a User instance.
         *
         * @return the constructed User
         */
        public User build() {
            return new User(this);
        }
    }

    /**
     * Returns a builder initialized with the current user's data.
     *
     * @return the builder
     */
    public UserBuilder toBuilder() {
        return new UserBuilder()
                .username(username)
                .password(password)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .role(role);
    }

    /**
     * Compares this user with another object for equality based on all fields.
     *
     * @param o the object to compare to
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                role == user.role;
    }

    /**
     * Returns the hash code for this user based on all fields.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, firstName, lastName, phoneNumber, role);
    }

    /**
     * Returns the user's full name as "firstName lastName".
     *
     * @return the full name
     */
    public String getUserFullName() {
        return firstName + " " + lastName;
    }
}
