package security;

public class SessionContext {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static Role getRole() {
        return currentUser == null ? Role.USER : currentUser.getRole();
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static String getDisplayName() {
        return null;
    }

    public static void setDisplayName(String name) {

    }
}
