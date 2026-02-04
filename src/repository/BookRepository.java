package repository;

import data.interfaces.IDB;
import models.Book;
import models.dto.FullBookDescription;
import repository.interfaces.IBookRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository implements IBookRepository {
    private final IDB db;

    public BookRepository(IDB db) {
        if (db == null) throw new IllegalArgumentException("DB is null");
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
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error saving: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Book> getAllBooks() {
        String sql = "SELECT id, title, genre, status FROM books ORDER BY id";
        List<Book> books = new ArrayList<>();

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("status")
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
            System.out.println("Error updating: " + e.getMessage());
            return false;
        }
    }

    // ✅ FIXED
    @Override
    public Book getBookById(int id) {
        String sql = "SELECT id, title, genre, status FROM books WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            rs.getString("status")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving by id: " + e.getMessage());
        }
        return null;
    }

    // ✅ FIXED
    @Override
    public boolean bookExists(int bookId) {
        String sql = "SELECT 1 FROM books WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.out.println("Error bookExists: " + e.getMessage());
            return false;
        }
    }

    // ✅ FIXED
    @Override
    public boolean isBookBorrowed(int bookId) {
        String sql = "SELECT 1 FROM loans WHERE book_id = ? AND returned = FALSE LIMIT 1";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.out.println("Error isBookBorrowed: " + e.getMessage());
            return false;
        }
    }

    // ✅ FIXED
    @Override
    public boolean borrowBook(int bookId, String borrowerName) {
        String sql =
                "INSERT INTO loans (book_id, borrower_name, due_date, returned) " +
                        "VALUES (?, ?, CURRENT_DATE + INTERVAL '14 days', FALSE)";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            stmt.setString(2, borrowerName);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error borrowing: " + e.getMessage());
            return false;
        }
    }

    // ✅ FIXED
    @Override
    public boolean returnBook(int bookId) {
        String sql = "UPDATE loans SET returned = TRUE WHERE book_id = ? AND returned = FALSE";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error returning: " + e.getMessage());
            return false;
        }
    }

    // Пока можно оставить (если не используешь этот пункт меню)
    @Override
    public FullBookDescription getFullBookDescription(int bookId) {
        return null;
    }

    @Override
    public List<Book> getBooksByGenre(String genre) {
        String sql = "SELECT id, title, genre, status FROM books WHERE LOWER(genre)=LOWER(?) ORDER BY id";
        List<Book> books = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, genre);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            rs.getString("status")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Error getBooksByGenre: " + e.getMessage());
        }
        return books;
    }
}
