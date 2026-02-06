package repository;

import data.interfaces.IDB;
import models.Book;
import models.BookStatus;
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
        String sql = "INSERT INTO books (title, genre, status, category_id, author_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getGenre());
            stmt.setString(3, book.getStatus().name());
            stmt.setObject(4, book.getCategoryId()); // nullable
            stmt.setObject(5, book.getAuthorId());   // nullable

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error saving book: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Book> getAllBooks() {
        String sql = "SELECT id, title, genre, status, author_id FROM books ORDER BY id";
        List<Book> books = new ArrayList<>();

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BookStatus status = BookStatus.valueOf(rs.getString("status"));
                Book b = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        BookStatus.valueOf(rs.getString("status").toUpperCase())
                );
                b.setAuthorId(rs.getObject("author_id", Integer.class));
                books.add(b);
            }

        } catch (Exception e) {
            System.out.println("Error retrieving books: " + e.getMessage());
        }

        return books;
    }

    @Override
    public Book getBookById(int id) {
        String sql = "SELECT id, title, genre, status,  author_id FROM books WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BookStatus status = BookStatus.valueOf(rs.getString("status"));
                    Book b = new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            BookStatus.valueOf(rs.getString("status"))
                    );
                    b.setAuthorId(rs.getObject("author_id", Integer.class));
                    return b;
                }
            }

        } catch (Exception e) {
            System.out.println("Error retrieving by id: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateStatus(int bookId, BookStatus status) {
        String sql = "UPDATE books SET status = ? WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            stmt.setInt(2, bookId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error updating status: " + e.getMessage());
            return false;
        }
    }

    // Admin/Librarian helper methods
    public boolean markAvailable(int bookId) {
        return updateStatus(bookId, BookStatus.AVAILABLE);
    }

    public boolean markBorrowed(int bookId) {
        return updateStatus(bookId, BookStatus.BORROWED);
    }

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
            System.out.println("Error checking bookExists: " + e.getMessage());
            return false;
        }
    }


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
            System.out.println("Error checking isBookBorrowed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean borrowBook(int bookId, String borrowerName) {
        String sql = "INSERT INTO loans (book_id, borrower_name, due_date, returned) " +
                "VALUES (?, ?, CURRENT_DATE + INTERVAL '14 days', FALSE)";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            stmt.setString(2, borrowerName);

            boolean ok = stmt.executeUpdate() > 0;
            if (ok) markBorrowed(bookId); // Update book status to BORROWED
            return ok;

        } catch (Exception e) {
            System.out.println("Error borrowing book: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean returnBook(int bookId) {
        String sql = "UPDATE loans SET returned = TRUE WHERE book_id = ? AND returned = FALSE";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            boolean ok = stmt.executeUpdate() > 0;
            if (ok) markAvailable(bookId); // Update book status to AVAILABLE
            return ok;

        } catch (Exception e) {
            System.out.println("Error returning book: " + e.getMessage());
            return false;
        }
    }

    @Override
    public FullBookDescription getFullBookDescription(int bookId) {
        String sql = """
            SELECT\s
                b.id,
                b.title,
                b.genre,
                b.status,
                a.name AS author_name,
                c.name AS category_name,
                l.borrower_name,
                l.due_date,
                l.returned
            FROM books b
            LEFT JOIN authors a ON b.author_id = a.id
            LEFT JOIN categories c ON b.category_id = c.id
            LEFT JOIN loans l ON b.id = l.book_id AND l.returned = FALSE
            WHERE b.id = ?
        """;

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new FullBookDescription(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            rs.getString("status"),
                            rs.getString("author_name"),
                            rs.getString("category_name"),
                            rs.getString("borrower_name"),
                            rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null,
                            rs.getBoolean("returned")
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("Error getting full book description: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Book> getBooksByGenre(String genre) {
        String sql = "SELECT id, title, genre, status, category_id, author_id FROM books " +
                "WHERE LOWER(genre) = LOWER(?) ORDER BY id";
        List<Book> books = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, genre);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BookStatus status = BookStatus.valueOf(rs.getString("status"));
                    Book b = new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            status
                    );
                    b.setCategoryId(rs.getObject("category_id", Integer.class));
                    b.setAuthorId(rs.getObject("author_id", Integer.class));
                    books.add(b);
                }
            }

        } catch (Exception e) {
            System.out.println("Error getBooksByGenre: " + e.getMessage());
        }

        return books;
    }

    @Override
    public List<Book> getBooksByCategory(int categoryId) {
        return List.of();
    }

    @Override
    public List<Book> getBooksByCategoryName(String categoryName) {
        return List.of();
    }

    @Override
    public List<Book> getReadBooks() {
        return List.of();
    }

    @Override
    public List<Book> getNotReadBooks() {
        return List.of();
    }
}

