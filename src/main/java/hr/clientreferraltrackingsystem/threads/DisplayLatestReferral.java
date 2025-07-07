package hr.clientreferraltrackingsystem.threads;

import hr.clientreferraltrackingsystem.model.Referral;
import hr.clientreferraltrackingsystem.repository.database.ReferralDatabaseRepository;
import javafx.scene.control.Label;

import java.util.Optional;

/**
 * Runnable implementation that fetches the latest referral from the database
 * and updates a JavaFX Label with its information.
 */
public class DisplayLatestReferral implements Runnable {
    private ReferralDatabaseRepository repository;
    private Label label;

    /**
     * Constructs a new DisplayLatestReferral runnable.
     *
     * @param repository the repository used to fetch referrals
     * @param label the JavaFX Label to update with referral information
     */
    public DisplayLatestReferral(ReferralDatabaseRepository repository, Label label) {
        this.repository = repository;
        this.label = label;
    }

    /**
     * Fetches the latest referral from the database and updates the label
     * with the referral's string representation if present.
     */
    @Override
    public void run() {
        Optional<Referral> latestReferral = repository.findLatestReferralFromDb();
        latestReferral.ifPresent(referral -> label.setText(referral.threadPrint()));
    }
}
