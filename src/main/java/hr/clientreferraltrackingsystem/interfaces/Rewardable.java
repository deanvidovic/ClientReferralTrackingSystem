package hr.clientreferraltrackingsystem.interfaces;

import hr.clientreferraltrackingsystem.model.Reward;
import hr.clientreferraltrackingsystem.model.User;

import java.util.List;

/**
 * A sealed interface representing entities that can have rewards.
 *
 * Only the {@link User} class is permitted to implement this interface.
 */
public sealed interface Rewardable permits User {

    /**
     * Returns the list of rewards associated with this entity.
     *
     * @return a list of {@link Reward} objects
     */
    List<Reward> getRewards();
}
