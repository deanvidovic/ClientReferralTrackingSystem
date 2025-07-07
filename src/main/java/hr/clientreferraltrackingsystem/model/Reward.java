package hr.clientreferraltrackingsystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a reward given for a referral with details such as description,
 * value, and the date the reward was issued.
 */
public class Reward extends Entity {
    private Referral referral;
    private String description;
    private BigDecimal value;
    private LocalDateTime rewardDate;

    /**
     * Constructs a Reward instance using the Builder pattern.
     *
     * @param builder the builder containing the reward details
     */
    private Reward(Builder builder) {
        super(builder.id);
        this.referral = builder.referral;
        this.description = builder.description;
        this.value = builder.value;
        this.rewardDate = builder.rewardDate;
    }

    /**
     * Builder class for constructing Reward instances.
     */
    public static class Builder {
        private Integer id;
        private Referral referral;
        private String description;
        private BigDecimal value;
        private LocalDateTime rewardDate;

        /**
         * Sets the ID for the reward.
         *
         * @param id the reward ID
         * @return the builder instance
         */
        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the referral associated with the reward.
         *
         * @param referral the referral
         * @return the builder instance
         */
        public Builder referral(Referral referral) {
            this.referral = referral;
            return this;
        }

        /**
         * Sets the description of the reward.
         *
         * @param description the reward description
         * @return the builder instance
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Sets the value of the reward.
         *
         * @param value the reward value
         * @return the builder instance
         */
        public Builder value(BigDecimal value) {
            this.value = value;
            return this;
        }

        /**
         * Sets the date the reward was issued.
         *
         * @param rewardDate the reward date
         * @return the builder instance
         */
        public Builder rewardDate(LocalDateTime rewardDate) {
            this.rewardDate = rewardDate;
            return this;
        }

        /**
         * Builds and returns a Reward instance.
         *
         * @return the constructed Reward
         */
        public Reward build() {
            return new Reward(this);
        }
    }

    /**
     * Returns the referral associated with this reward.
     *
     * @return the referral
     */
    public Referral getReferral() {
        return referral;
    }

    /**
     * Returns the description of the reward.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the value of the reward.
     *
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Returns the date the reward was issued.
     *
     * @return the reward date
     */
    public LocalDateTime getRewardDate() {
        return rewardDate;
    }

    /**
     * Sets the referral associated with this reward.
     *
     * @param referral the referral to set
     */
    public void setReferral(Referral referral) {
        this.referral = referral;
    }

    /**
     * Sets the description of the reward.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the value of the reward.
     *
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * Sets the date the reward was issued.
     *
     * @param rewardDate the reward date to set
     */
    public void setRewardDate(LocalDateTime rewardDate) {
        this.rewardDate = rewardDate;
    }

    /**
     * Compares this reward with another object for equality based on description,
     * value, and reward date.
     *
     * @param o the object to compare to
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reward reward = (Reward) o;
        return Objects.equals(description, reward.description) &&
                Objects.equals(value, reward.value) &&
                Objects.equals(rewardDate, reward.rewardDate);
    }

    /**
     * Returns the hash code for this reward based on description, value, and reward date.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(description, value, rewardDate);
    }
}
