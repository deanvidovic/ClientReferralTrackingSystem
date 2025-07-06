package hr.clientreferraltrackingsystem.interfaces;

import hr.clientreferraltrackingsystem.model.Reward;
import hr.clientreferraltrackingsystem.model.User;

import java.util.List;

public sealed interface Rewardable permits User {
    List<Reward> getRewards();
}
