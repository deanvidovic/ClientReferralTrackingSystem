package hr.clientreferraltrackingsystem.repository.dat;

import hr.clientreferraltrackingsystem.model.Referral;
import hr.clientreferraltrackingsystem.model.Reward;
import hr.clientreferraltrackingsystem.repository.database.ReferralDatabaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RewardRepository<T extends Reward> extends AbstractRepository<T> {
    private static final String REWARDS_FILE_PATH = "dat/rewards.txt";
    private static final Integer REWARDS_LINES = 5;
    public static final String DATE_FORMAT = "dd.MM.yyyy.";
    private static final Logger log = LoggerFactory.getLogger(RewardRepository.class);
    private final ReferralDatabaseRepository referralDatabaseRepository = new ReferralDatabaseRepository();

    @Override
    public List<T> findAllById(Integer id) {
        return findAll().stream()
                .filter(r -> r.getReferral().getRefferer().getId().equals(id))
                .toList();
    }

    @Override
    public List<T> findAll() {
        List<T> rewards = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Path.of(REWARDS_FILE_PATH))) {
            List<String> fileRows = stream.toList();

            for (int i = 0; i < fileRows.size(); i += 5) {
                int id = Integer.parseInt(fileRows.get(i));
                int referralId = Integer.parseInt(fileRows.get(i + 1));
                String description = fileRows.get(i + 2);
                LocalDate rewardDate = LocalDate.parse(fileRows.get(i + 3), DateTimeFormatter.ofPattern(DATE_FORMAT));
                BigDecimal value = new BigDecimal(fileRows.get(i + 4));

                Referral referral = referralDatabaseRepository.findById(referralId);

                Reward reward = new Reward.Builder()
                        .id(id)
                        .referral(referral)
                        .description(description)
                        .rewardDate(rewardDate.atStartOfDay())
                        .value(value)
                        .build();

                rewards.add((T) reward);
            }

        } catch (IOException e) {
            log.error("Error reading rewards file", e);
        }

        return rewards;
    }


    @Override
    public void save(T entity) {
        try {
            List<String> lines = Files.exists(Path.of(REWARDS_FILE_PATH)) ?
                    Files.readAllLines(Path.of(REWARDS_FILE_PATH)) : new ArrayList<>();

            int newId = generateNextId(lines);
            entity.setId(newId);

            List<String> rewardLines = new ArrayList<>();
            rewardLines.add(String.valueOf(entity.getId()));
            rewardLines.add(String.valueOf(entity.getReferral().getId()));
            rewardLines.add(entity.getDescription());
            rewardLines.add(entity.getRewardDate().toLocalDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
            rewardLines.add(entity.getValue().toPlainString());

            Files.write(Path.of(REWARDS_FILE_PATH), rewardLines, java.nio.file.StandardOpenOption.APPEND, java.nio.file.StandardOpenOption.CREATE);

        } catch (IOException e) {
            log.error("Error while saving rewards in file", e);
        }
    }

    @Override
    public void update(T entity) {
        List<T> allRewards = findAll();
        List<String> updatedLines = new ArrayList<>();

        for (T reward : allRewards) {
            if (reward.getId().equals(entity.getId())) {
                reward = entity;
            }

            updatedLines.add(reward.getId().toString());
            updatedLines.add(reward.getReferral().getId().toString());
            updatedLines.add(reward.getDescription());
            updatedLines.add(reward.getRewardDate().toLocalDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
            updatedLines.add(reward.getValue().toString());
        }

        try {
            Files.write(Path.of(REWARDS_FILE_PATH), updatedLines);
        } catch (IOException e) {
            log.error("Error while updating rewards in file", e);
        }
    }



    @Override
    public void delete(T entity) {
        List<T> allRewards = findAll();
        List<T> remainingRewards = allRewards.stream()
                .filter(r -> !r.getId().equals(entity.getId()))
                .toList();

        List<String> linesToWrite = new ArrayList<>();

        for (T reward : remainingRewards) {
            linesToWrite.add(String.valueOf(reward.getId()));
            linesToWrite.add(String.valueOf(reward.getReferral().getId()));
            linesToWrite.add(reward.getDescription());
            linesToWrite.add(reward.getRewardDate().toLocalDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
            linesToWrite.add(reward.getValue().toString());
        }

        try {
            Files.write(Path.of(REWARDS_FILE_PATH), linesToWrite);
        } catch (IOException e) {
            log.error("Error while deleting from file!", e);
        }
    }



    public static int generateNextId(List<String> lines) {
        int maxId = 0;
        for (int i = 0; i < lines.size(); i += REWARDS_LINES) {
            int currentId = Integer.parseInt(lines.get(i));
            if (currentId > maxId) {
                maxId = currentId;
            }
        }
        return maxId + 1;
    }

}
