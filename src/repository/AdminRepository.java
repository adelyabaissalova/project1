package repository;

import data.interfaces.IDB;
import repository.interfaces.IAdminRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdminRepository implements IAdminRepository {
    private final IDB db;

    public AdminRepository(IDB db) {
        if (db == null) throw new IllegalArgumentException("DB is null");
        this.db = db;
    }

    @Override
    public boolean createUser(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES (?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, username);
            st.setString(2, password);
            return st.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("createUser error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean assignRole(String username, String roleName) {
        String sql = """
            INSERT INTO user_roles(user_id, role_id)
            SELECT u.id, r.id
            FROM users u, roles r
            WHERE u.username = ? AND r.name = ?
            ON CONFLICT DO NOTHING
        """;
        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, username);
            st.setString(2, roleName);
            return st.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("assignRole error: " + e.getMessage());
            return false;
        }
    }
}