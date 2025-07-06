package hr.clientreferraltrackingsystem.threads;

import hr.clientreferraltrackingsystem.model.Referral;
import hr.clientreferraltrackingsystem.repository.database.ReferralDatabaseRepository;
import javafx.scene.control.Label;

import java.util.Optional;

public class DisplayLatestReferral implements Runnable {
    private ReferralDatabaseRepository repository;
    private Label label;

    public DisplayLatestReferral(ReferralDatabaseRepository repository, Label label) {
        this.repository = repository;
        this.label = label;
    }

    @Override
    public void run() {
        Optional<Referral> latestReferral = repository.findLatestReferralFromDb();

        latestReferral.ifPresent(referral -> label.setText(referral.threadPrint()));
    }
}
