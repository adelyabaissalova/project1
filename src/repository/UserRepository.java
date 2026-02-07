package repository;

import data.interfaces.IDB;
import repository.interfaces.IUserRepository;
import security.User;
import security.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class UserRepository implements IUserRepository {
    private final IDB db;

    public UserRepository(IDB db) {
        if (db == null) throw new IllegalArgumentException("DB is null");
        this.db = db;
    }

    @Override
    public User login(String username, String password) {
        String userSql = "SELECT id, username FROM users WHERE username=? AND password=?";
        String rolesSql = """
            SELECT r.name
            FROM user_roles ur
            JOIN roles r ON ur.role_id = r.id
            WHERE ur.user_id = ?
        """;

        try (Connection conn = db.getConnection();
             PreparedStatement uSt = conn.prepareStatement(userSql)) {

            uSt.setString(1, username);
            uSt.setString(2, password);

            try (ResultSet rs = uSt.executeQuery()) {
                if (!rs.next()) return null;

                int userId = rs.getInt("id");
                String uname = rs.getString("username");

                Set<UserRole> roles = new HashSet<>();
                try (PreparedStatement rSt = conn.prepareStatement(rolesSql)) {
                    rSt.setInt(1, userId);
                    try (ResultSet rrs = rSt.executeQuery()) {
                        while (rrs.next()) roles.add(UserRole.valueOf(rrs.getString("name")));
                    }
                }

                return new User(userId, uname, roles);
            }
        } catch (Exception e) {
            System.out.println("login error: " + e.getMessage());
            return null;
        }
    }
}