package hr.clientreferraltrackingsystem.model;

import hr.clientreferraltrackingsystem.enumeration.ReferralStatus;
import hr.clientreferraltrackingsystem.interfaces.Trackable;

import java.time.LocalDateTime;
import java.util.Objects;

public class Referral extends Entity implements Trackable {

    private User refferer;
    private Client client;
    private LocalDateTime refferalDate;
    private ReferralStatus status;

    public Referral(User refferer, Client client, LocalDateTime refferalDate, ReferralStatus status) {
        super(null);
        this.refferer = refferer;
        this.client = client;
        this.refferalDate = refferalDate;
        this.status = status;
    }

    public Referral(Integer id, User refferer, Client client, LocalDateTime refferalDate, ReferralStatus status) {
        super(id);
        this.refferer = refferer;
        this.client = client;
        this.refferalDate = refferalDate;
        this.status = status;
    }

    public User getRefferer() {
        return refferer;
    }

    public void setRefferer(User refferer) {
        this.refferer = refferer;
    }

    public Client getRefferedClient() {
        return client;
    }

    public void setRefferedClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getRefferalDate() {
        return refferalDate;
    }

    public void setRefferalDate(LocalDateTime refferalDate) {
        this.refferalDate = refferalDate;
    }

    public String threadPrint() {
        return "Refferer: " + refferer.getUserFullName() + ", " +
                " Client: " + client.getFullName() + ", " +
                " Refferal Date: " + ", " + refferalDate.toLocalDate() + ", " +
                " Status: " + ", " + status;
    }

    @Override
    public ReferralStatus getReferralStatus() {
        return status;
    }

    @Override
    public void setReferralStatus(ReferralStatus referralStatus) {
        this.status = referralStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Referral referral = (Referral) o;
        return Objects.equals(refferer, referral.refferer) && Objects.equals(client, referral.client) && Objects.equals(refferalDate, referral.refferalDate) && status == referral.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(refferer, client, refferalDate, status);
    }
}
