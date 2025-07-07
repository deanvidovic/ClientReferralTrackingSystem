package hr.clientreferraltrackingsystem.model;

import hr.clientreferraltrackingsystem.enumeration.ReferralStatus;
import hr.clientreferraltrackingsystem.interfaces.Trackable;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a referral entity linking a user (referrer) with a client and tracking
 * the referral date and status.
 */
public class Referral extends Entity implements Trackable {

    private User refferer;
    private Client client;
    private LocalDateTime refferalDate;
    private ReferralStatus status;

    /**
     * Constructs a Referral without an ID.
     *
     * @param refferer     the user who made the referral
     * @param client       the referred client
     * @param refferalDate the date and time of the referral
     * @param status       the current status of the referral
     */
    public Referral(User refferer, Client client, LocalDateTime refferalDate, ReferralStatus status) {
        super(null);
        this.refferer = refferer;
        this.client = client;
        this.refferalDate = refferalDate;
        this.status = status;
    }

    /**
     * Constructs a Referral with an ID.
     *
     * @param id           the referral ID
     * @param refferer     the user who made the referral
     * @param client       the referred client
     * @param refferalDate the date and time of the referral
     * @param status       the current status of the referral
     */
    public Referral(Integer id, User refferer, Client client, LocalDateTime refferalDate, ReferralStatus status) {
        super(id);
        this.refferer = refferer;
        this.client = client;
        this.refferalDate = refferalDate;
        this.status = status;
    }

    /**
     * Returns the user who made the referral.
     *
     * @return the referrer user
     */
    public User getRefferer() {
        return refferer;
    }

    /**
     * Sets the user who made the referral.
     *
     * @param refferer the user to set as referrer
     */
    public void setRefferer(User refferer) {
        this.refferer = refferer;
    }

    /**
     * Returns the client who was referred.
     *
     * @return the referred client
     */
    public Client getRefferedClient() {
        return client;
    }

    /**
     * Sets the referred client.
     *
     * @param client the client to set
     */
    public void setRefferedClient(Client client) {
        this.client = client;
    }

    /**
     * Returns the date and time of the referral.
     *
     * @return the referral date and time
     */
    public LocalDateTime getRefferalDate() {
        return refferalDate;
    }

    /**
     * Sets the date and time of the referral.
     *
     * @param refferalDate the referral date and time to set
     */
    public void setRefferalDate(LocalDateTime refferalDate) {
        this.refferalDate = refferalDate;
    }

    /**
     * Returns a formatted string with referral details.
     *
     * @return formatted referral information
     */
    public String threadPrint() {
        return "Refferer: " + refferer.getUserFullName() + ", " +
                " Client: " + client.getFullName() + ", " +
                " Refferal Date: " + refferalDate.toLocalDate() + ", " +
                " Status: " + status;
    }

    /**
     * Returns the current referral status.
     *
     * @return the referral status
     */
    @Override
    public ReferralStatus getReferralStatus() {
        return status;
    }

    /**
     * Sets the referral status.
     *
     * @param referralStatus the new referral status
     */
    @Override
    public void setReferralStatus(ReferralStatus referralStatus) {
        this.status = referralStatus;
    }

    /**
     * Compares this referral with another object for equality.
     * Two referrals are equal if their referrer, client, referral date, and status are equal.
     *
     * @param o the object to compare to
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Referral referral = (Referral) o;
        return Objects.equals(refferer, referral.refferer) &&
                Objects.equals(client, referral.client) &&
                Objects.equals(refferalDate, referral.refferalDate) &&
                status == referral.status;
    }

    /**
     * Returns the hash code of this referral based on referrer, client, referral date, and status.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(refferer, client, refferalDate, status);
    }
}
