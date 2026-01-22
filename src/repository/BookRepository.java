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

        String sql = "INSERT INTO books (title, genre, status, author_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getGenre());
            stmt.setString(3, book.getStatus());

            Integer authorId = book.getAuthorId();
            if (authorId == null) stmt.setNull(4, Types.INTEGER);
            else stmt.setInt(4, authorId);

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error saving: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Book> getAllBooks() {
        String sql =
                "SELECT b.id, b.title, b.genre, b.status, b.author_id, " +
                        "COALESCE(a.full_name, 'Unknown') AS author_name " +
                        "FROM books b " +
                        "LEFT JOIN authors a ON a.id = b.author_id " +
                        "ORDER BY b.id";

        List<Book> books = new ArrayList<>();

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("status"),
                        (Integer) rs.getObject("author_id"),
                        rs.getString("author_name")
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
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error update: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Book getBookById(int id) {
        String sql =
                "SELECT b.id, b.title, b.genre, b.status, b.author_id, " +
                        "COALESCE(a.full_name, 'Unknown') AS author_name " +
                        "FROM books b " +
                        "LEFT JOIN authors a ON a.id = b.author_id " +
                        "WHERE b.id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            rs.getString("status"),
                            (Integer) rs.getObject("author_id"),
                            rs.getString("author_name")
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("Error retrieving by id: " + e.getMessage());
        }

        return null;
    }
}
