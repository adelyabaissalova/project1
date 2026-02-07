package security;

public class SessionContext {
    private static SessionContext instance;
    private User currentUser;

    private SessionContext() {}

    public static synchronized SessionContext getInstance() {
        if (instance == null) instance = new SessionContext();
        return instance;
    }

    public static Role getRole() {
        return null;
    }

    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User user) { this.currentUser = user; }
    public void clear() { this.currentUser = null; }
}
