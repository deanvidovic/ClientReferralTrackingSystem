package hr.clientreferraltrackingsystem.model;

import java.util.Objects;

public class Client extends Entity {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private User createdBy;
    private Boolean isCurrentlyRecommended;


    public Client(String firstName, String lastName, String email, String phoneNumber, User createdBy, boolean isCurrentlyRecommended) {
        super(null);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdBy = createdBy;
        this.isCurrentlyRecommended = isCurrentlyRecommended;
    }

    public Client(String firstName, String lastName, String email, String phoneNumber, User createdBy) {
        super(null);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdBy = createdBy;
        this.isCurrentlyRecommended = false;
    }

    public Client(Integer id, String firstName, String lastName, String email, String phoneNumber, User createdBy) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdBy = createdBy;
        this.isCurrentlyRecommended = false;
    }

    public Client(Integer id, String firstName, String lastName, String email, String phoneNumber, User createdBy, boolean isCurrentlyRecommended) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdBy = createdBy;
        this.isCurrentlyRecommended = isCurrentlyRecommended;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getCurrentlyRecommended() {
        return isCurrentlyRecommended;
    }

    public void setCurrentlyRecommended(Boolean currentlyRecommended) {
        isCurrentlyRecommended = currentlyRecommended;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client that = (Client) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, phoneNumber);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
