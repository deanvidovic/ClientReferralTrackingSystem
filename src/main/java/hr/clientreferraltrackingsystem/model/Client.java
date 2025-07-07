package hr.clientreferraltrackingsystem.model;

import java.util.Objects;

/**
 * Represents a client entity with personal and contact details,
 * as well as information about the user who created this client
 * and whether the client is currently recommended.
 */
public class Client extends Entity {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private User createdBy;
    private Boolean isCurrentlyRecommended;

    /**
     * Constructs a Client with all fields including recommendation status.
     *
     * @param firstName            the client's first name
     * @param lastName             the client's last name
     * @param email                the client's email
     * @param phoneNumber          the client's phone number
     * @param createdBy            the user who created this client
     * @param isCurrentlyRecommended whether the client is currently recommended
     */
    public Client(String firstName, String lastName, String email, String phoneNumber, User createdBy, boolean isCurrentlyRecommended) {
        super(null);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdBy = createdBy;
        this.isCurrentlyRecommended = isCurrentlyRecommended;
    }

    /**
     * Constructs a Client without specifying recommendation status (defaults to false).
     *
     * @param firstName   the client's first name
     * @param lastName    the client's last name
     * @param email       the client's email
     * @param phoneNumber the client's phone number
     * @param createdBy   the user who created this client
     */
    public Client(String firstName, String lastName, String email, String phoneNumber, User createdBy) {
        super(null);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdBy = createdBy;
        this.isCurrentlyRecommended = false;
    }

    /**
     * Constructs a Client with specified ID, without recommendation status (defaults to false).
     *
     * @param id          the client ID
     * @param firstName   the client's first name
     * @param lastName    the client's last name
     * @param email       the client's email
     * @param phoneNumber the client's phone number
     * @param createdBy   the user who created this client
     */
    public Client(Integer id, String firstName, String lastName, String email, String phoneNumber, User createdBy) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdBy = createdBy;
        this.isCurrentlyRecommended = false;
    }

    /**
     * Constructs a Client with specified ID and recommendation status.
     *
     * @param id                   the client ID
     * @param firstName            the client's first name
     * @param lastName             the client's last name
     * @param email                the client's email
     * @param phoneNumber          the client's phone number
     * @param createdBy            the user who created this client
     * @param isCurrentlyRecommended whether the client is currently recommended
     */
    public Client(Integer id, String firstName, String lastName, String email, String phoneNumber, User createdBy, boolean isCurrentlyRecommended) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdBy = createdBy;
        this.isCurrentlyRecommended = isCurrentlyRecommended;
    }

    /**
     * Returns the client's first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the client's first name.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the client's last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the client's last name.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the client's email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the client's email.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the client's phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the client's phone number.
     *
     * @param phoneNumber the phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns whether the client is currently recommended.
     *
     * @return true if recommended, false otherwise
     */
    public Boolean getCurrentlyRecommended() {
        return isCurrentlyRecommended;
    }

    /**
     * Sets the recommendation status of the client.
     *
     * @param currentlyRecommended the new recommendation status
     */
    public void setCurrentlyRecommended(Boolean currentlyRecommended) {
        isCurrentlyRecommended = currentlyRecommended;
    }

    /**
     * Returns the user who created this client.
     *
     * @return the creator user
     */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the user who created this client.
     *
     * @param createdBy the user to set as creator
     */
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Checks if this client is equal to another object.
     * Equality is based on first name, last name, email, and phone number.
     *
     * @param o the object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client that = (Client) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(email, that.email) &&
                Objects.equals(phoneNumber, that.phoneNumber);
    }

    /**
     * Returns the hash code for this client based on first name, last name, email, and phone number.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, phoneNumber);
    }

    /**
     * Returns the full name of the client (first name + last name).
     *
     * @return the full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
