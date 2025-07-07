package hr.clientreferraltrackingsystem.service;

import hr.clientreferraltrackingsystem.model.User;

public class SessionManager {
    public static final SessionManager instance = getSingletonInstance();
    private static SessionManager singletonInstance;
    private User loggedUser;

    private SessionManager() {}

    private static SessionManager getSingletonInstance() {
        if (singletonInstance == null) {
            singletonInstance = new SessionManager();
        }

        return new SessionManager();
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public User getLoggedUser() {
        return loggedUser;
    }
}
