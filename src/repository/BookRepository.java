package repository;

import data.interfaces.IDB;
import models.Book;
import repository.interfaces.IBookRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository implements IBookRepository {
    private final IDB db;

    public BookRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createBook(Book book) {
        String sql = "INSERT INTO books (title, genre, status) VALUES (?, ?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getGenre());
            stmt.setString(3, book.getStatus());
            stmt.execute();
            return true;

        } catch (Exception e) {
            System.out.println("Error saving: " + e.getMessage());
            return false;
        }
    }


    @Override
    public List<Book> getAllBooks() {
        String sql = "SELECT * FROM books";
        List<Book> books = new ArrayList<>();

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("ID"),
                        rs.getString("Title"),
                        rs.getString("Genre"),
                        rs.getString("Status")
                ));
            }

        } catch (Exception e) {
            System.out.println("Error retrieving: " + e.getMessage());
        }

        return books;
    }

    @Override
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE books SET status = ? WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.execute();
            return true;

        } catch (Exception e) {
            System.out.println("Error updatе: " + e.getMessage());
            return false;
        }
    }
}
