package repositories;

import data.PostgresDB;
import models.User;
import repositories.interfaces.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserRepository implements UserRepository {

    private final PostgresDB db = new PostgresDB();

    @Override
    public boolean save(User user) {
        String sql = "INSERT INTO users(username) VALUES (?)";

        try (Connection con = db.connect();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, user.getUsername());
            st.executeUpdate();
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}