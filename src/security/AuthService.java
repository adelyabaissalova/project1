package security;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final Map<String, User> users = new HashMap<>();

    public AuthService() {
        users.put("admin",      new User("admin",      "admin123",      Role.ADMIN));
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
