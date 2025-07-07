package hr.clientreferraltrackingsystem.controller.admin.helper;

import hr.clientreferraltrackingsystem.model.Reward;
import hr.clientreferraltrackingsystem.repository.dat.RewardRepository;
import hr.clientreferraltrackingsystem.utils.InputValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Utility helper class for managing reward-related operations in the admin dashboard.
 * <p>
 * Provides methods for editing rewards and filtering reward lists by various criteria
 * such as client name, description, value range, and date range.
 */
public class AdminDashboardRewardsHelper {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private AdminDashboardRewardsHelper() {}

    /**
     * Updates the given reward with a new description and value,
     * saves a serialized log entry, and updates the reward in the repository.
     *
     * @param description the new description for the reward
     * @param value the new value for the reward
     * @param selectedReward the reward to be updated
     */
    public static void handleEdit(String description, BigDecimal value, Reward selectedReward) {
        selectedReward.setDescription(description);
        selectedReward.setValue(value);

        InputValidator.serializeSave(
                "Reward updated",
                selectedReward.getId(),
                selectedReward.getReferral().getRefferedClient().getEmail()
        );

        RewardRepository<Reward> rewardRepository = new RewardRepository<>();
        rewardRepository.update(selectedReward);
    }

    /**
     * Filters a list of rewards by client full name containing the given filter text (case-insensitive).
     *
     * @param rewards the list of rewards to filter
     * @param clientNameFilter the client name filter text
     * @return a filtered list of rewards matching the client name criteria;
     *         returns the original list if the filter is null or empty
     */
    public static List<Reward> filterByClientName(List<Reward> rewards, String clientNameFilter) {
        if (clientNameFilter == null || clientNameFilter.isEmpty()) {
            return rewards;
        }
        String lowerFilter = clientNameFilter.toLowerCase();
        return rewards.stream()
                .filter(r -> r.getReferral() != null &&
                        r.getReferral().getRefferedClient() != null &&
                        r.getReferral().getRefferedClient().getFullName() != null &&
                        r.getReferral().getRefferedClient().getFullName().toLowerCase().contains(lowerFilter))
                .toList();
    }

    /**
     * Filters a list of rewards by description containing the given filter text (case-insensitive).
     *
     * @param rewards the list of rewards to filter
     * @param descriptionFilter the description filter text
     * @return a filtered list of rewards matching the description criteria;
     *         returns the original list if the filter is null or empty
     */
    public static List<Reward> filterByDescription(List<Reward> rewards, String descriptionFilter) {
        if (descriptionFilter == null || descriptionFilter.isEmpty()) {
            return rewards;
        }
        String lowerFilter = descriptionFilter.toLowerCase();
        return rewards.stream()
                .filter(r -> r.getDescription() != null &&
                        r.getDescription().toLowerCase().contains(lowerFilter))
                .toList();
    }

    /**
     * Filters a list of rewards with value greater than or equal to the specified lower bound.
     *
     * @param rewards the list of rewards to filter
     * @param valueFromText the lower bound for value filter as a string
     * @return a filtered list of rewards with value >= valueFromText;
     *         returns the original list if the valueFromText is invalid or null
     */
    public static List<Reward> filterByValueFrom(List<Reward> rewards, String valueFromText) {
        if (!InputValidator.isValidBigDecimal(valueFromText)) {
            return rewards;
        }
        BigDecimal valueFrom = new BigDecimal(valueFromText.trim());
        return rewards.stream()
                .filter(r -> r.getValue() != null && r.getValue().compareTo(valueFrom) >= 0)
                .toList();
    }

    /**
     * Filters a list of rewards with value less than or equal to the specified upper bound.
     *
     * @param rewards the list of rewards to filter
     * @param valueToText the upper bound for value filter as a string
     * @return a filtered list of rewards with value <= valueToText;
     *         returns the original list if the valueToText is invalid or null
     */
    public static List<Reward> filterByValueTo(List<Reward> rewards, String valueToText) {
        if (!InputValidator.isValidBigDecimal(valueToText)) {
            return rewards;
        }
        BigDecimal valueTo = new BigDecimal(valueToText.trim());
        return rewards.stream()
                .filter(r -> r.getValue() != null && r.getValue().compareTo(valueTo) <= 0)
                .toList();
    }

    /**
     * Filters a list of rewards with reward dates on or after the specified start date.
     *
     * @param rewards the list of rewards to filter
     * @param dateFrom the lower bound date (inclusive)
     * @return a filtered list of rewards with rewardDate >= dateFrom;
     *         returns the original list if dateFrom is null
     */
    public static List<Reward> filterByDateFrom(List<Reward> rewards, LocalDateTime dateFrom) {
        if (dateFrom == null) {
            return rewards;
        }
        return rewards.stream()
                .filter(r -> r.getRewardDate() != null && !r.getRewardDate().isBefore(dateFrom))
                .toList();
    }

    /**
     * Filters a list of rewards with reward dates on or before the specified end date.
     *
     * @param rewards the list of rewards to filter
     * @param dateTo the upper bound date (inclusive)
     * @return a filtered list of rewards with rewardDate <= dateTo;
     *         returns the original list if dateTo is null
     */
    public static List<Reward> filterByDateTo(List<Reward> rewards, LocalDateTime dateTo) {
        if (dateTo == null) {
            return rewards;
        }
        return rewards.stream()
                .filter(r -> r.getRewardDate() != null && !r.getRewardDate().isAfter(dateTo))
                .toList();
    }
}
