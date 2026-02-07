package security;

import java.util.Set;

public class User {
    private final int id;
    private final String username;
    private final Set<UserRole> roles;

    public User(int id, String username, Set<UserRole> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public Set<UserRole> getRoles() { return roles; }

    public boolean isAdmin() { return roles.contains(UserRole.ADMIN); }
    public boolean isLibrarian() { return roles.contains(UserRole.LIBRARIAN); }
    public boolean isStaff() { return isAdmin() || isLibrarian(); }
}