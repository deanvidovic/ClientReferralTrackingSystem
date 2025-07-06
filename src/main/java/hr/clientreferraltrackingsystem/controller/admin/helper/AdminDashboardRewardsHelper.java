package hr.clientreferraltrackingsystem.controller.admin.helper;

import hr.clientreferraltrackingsystem.model.Reward;
import hr.clientreferraltrackingsystem.repository.dat.RewardRepository;
import hr.clientreferraltrackingsystem.utils.InputValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class AdminDashboardRewardsHelper {

    private AdminDashboardRewardsHelper() {}

    public static void handleEdit(String description, BigDecimal value, Reward selectedReward) {
        selectedReward.setDescription(description);
        selectedReward.setValue(value);

        RewardRepository<Reward> rewardRepository = new RewardRepository<>();
        rewardRepository.update(selectedReward);
    }

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

    public static List<Reward> filterByValueFrom(List<Reward> rewards, String valueFromText) {
        if (!InputValidator.isValidBigDecimal(valueFromText)) {
            return rewards;
        }
        BigDecimal valueFrom = new BigDecimal(valueFromText.trim());
        return rewards.stream()
                .filter(r -> r.getValue() != null && r.getValue().compareTo(valueFrom) >= 0)
                .toList();
    }

    public static List<Reward> filterByValueTo(List<Reward> rewards, String valueToText) {
        if (!InputValidator.isValidBigDecimal(valueToText)) {
            return rewards;
        }
        BigDecimal valueTo = new BigDecimal(valueToText.trim());
        return rewards.stream()
                .filter(r -> r.getValue() != null && r.getValue().compareTo(valueTo) <= 0)
                .toList();
    }

    public static List<Reward> filterByDateFrom(List<Reward> rewards, LocalDateTime dateFrom) {
        if (dateFrom == null) {
            return rewards;
        }
        return rewards.stream()
                .filter(r -> r.getRewardDate() != null && !r.getRewardDate().isBefore(dateFrom))
                .toList();
    }

    public static List<Reward> filterByDateTo(List<Reward> rewards, LocalDateTime dateTo) {
        if (dateTo == null) {
            return rewards;
        }
        return rewards.stream()
                .filter(r -> r.getRewardDate() != null && !r.getRewardDate().isAfter(dateTo))
                .toList();
    }
}
