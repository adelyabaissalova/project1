package security;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final Map<String, User> users = new HashMap<>();

    public AuthService() {
        users.put("Friend1", new User("Yeldana", "admin123", Role.ADMIN));
        users.put("Friend2", new User("Adelya", "admin123", Role.ADMIN));
        users.put("Inkar", new User("Inkar", "admin123", Role.ADMIN));
        users.put("librarian",  new User("librarian",  "lib123",        Role.LIBRARIAN));
        users.put("user",       new User("user",       "user123",       Role.USER));
    }

    public User login(String username, String password) {
        User u = users.get(username);
        if (u == null) return null;
        if (!u.getPassword().equals(password)) return null;
        return u;
    }
}
