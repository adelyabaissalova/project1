package repository;

import data.interfaces.IDB;
import models.Category;
import repository.interfaces.ICategoryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository implements ICategoryRepository {
    private final IDB db;

    public CategoryRepository(IDB db) {
        if (db == null) throw new IllegalArgumentException("DB is null");
        this.db = db;
    }

    @Override
    public boolean createCategory(Category category) {
        String sql = "INSERT INTO categories(name) VALUES (?)";
        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, category.getName());
            return st.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("createCategory error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Category> getAllCategories() {
        String sql = "SELECT id, name FROM categories ORDER BY id";
        List<Category> list = new ArrayList<>();
        try (Connection conn = db.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
        } catch (Exception e) {
            System.out.println("getAllCategories error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Category getCategoryByName(String name) {
        String sql = "SELECT id, name FROM categories WHERE LOWER(name)=LOWER(?)";
        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, name);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return new Category(rs.getInt("id"), rs.getString("name"));
            }
        } catch (Exception e) {
            System.out.println("getCategoryByName error: " + e.getMessage());
        }
        return null;
    }
}