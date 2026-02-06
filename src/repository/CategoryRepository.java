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
    public boolean addCategory(Category category) {
        String sql = "INSERT INTO categories(name) VALUES (?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error creating category: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createCategory(Category category) {
        return addCategory(category);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM categories ORDER BY id";
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                categories.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving categories: " + e.getMessage());
        }
        return categories;
    }


    @Override
    public Category getCategoryById(int id) {
        String sql = "SELECT id, name FROM categories WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Category c = new Category();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    return c;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error getting category by id: " + e.getMessage());
        }
        return null;
    }


    @Override
    public boolean categoryExists(int id) {
        String sql = "SELECT 1 FROM categories WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Error checking category exists: " + e.getMessage());
            return false;
        }
    }
}



