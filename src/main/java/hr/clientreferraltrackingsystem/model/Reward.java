package hr.clientreferraltrackingsystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reward extends Entity {
    private Referral referral;
    private String description;
    private BigDecimal value;
    private LocalDateTime rewardDate;

    private Reward(Builder builder) {
        super(builder.id);
        this.referral = builder.referral;
        this.description = builder.description;
        this.value = builder.value;
        this.rewardDate = builder.rewardDate;
    }

    public static class Builder {
        private Integer id;
        private Referral referral;
        private String description;
        private BigDecimal value;
        private LocalDateTime rewardDate;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder referral(Referral referral) {
            this.referral = referral;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder value(BigDecimal value) {
            this.value = value;
            return this;
        }

        public Builder rewardDate(LocalDateTime rewardDate) {
            this.rewardDate = rewardDate;
            return this;
        }

        public Reward build() {
            return new Reward(this);
        }
    }

    public Referral getReferral() {
        return referral;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public LocalDateTime getRewardDate() {
        return rewardDate;
    }

    public void setReferral(Referral referral) {
        this.referral = referral;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setRewardDate(LocalDateTime rewardDate) {
        this.rewardDate = rewardDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reward reward = (Reward) o;
        return Objects.equals(description, reward.description) && Objects.equals(value, reward.value) && Objects.equals(rewardDate, reward.rewardDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, value, rewardDate);
    }
}

