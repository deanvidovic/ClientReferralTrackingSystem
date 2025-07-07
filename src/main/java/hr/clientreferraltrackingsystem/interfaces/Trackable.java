package hr.clientreferraltrackingsystem.interfaces;

import hr.clientreferraltrackingsystem.enumeration.ReferralStatus;

/**
 * Interface representing an entity whose referral status can be tracked and updated.
 */
public interface Trackable {

    /**
     * Returns the current referral status.
     *
     * @return the referral status
     */
    ReferralStatus getReferralStatus();

    /**
     * Sets the referral status.
     *
     * @param referralStatus the new referral status to set
     */
    void setReferralStatus(ReferralStatus referralStatus);
}
