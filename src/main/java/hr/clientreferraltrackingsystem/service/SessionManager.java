package hr.clientreferraltrackingsystem.service;

import hr.clientreferraltrackingsystem.model.User;

public class SessionManager {
    private static SessionManager instance;
    private User loggedUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public User getLoggedUser() {
        return loggedUser;
    }
}
