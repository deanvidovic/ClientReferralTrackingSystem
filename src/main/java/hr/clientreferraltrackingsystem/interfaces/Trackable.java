package hr.clientreferraltrackingsystem.interfaces;

import hr.clientreferraltrackingsystem.enumeration.ReferralStatus;

public interface Trackable {
    ReferralStatus getReferralStatus();
    void setReferralStatus(ReferralStatus referralStatus);
}
