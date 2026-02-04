package repository;

import data.interfaces.IDB;
import models.Book;
import models.dto.FullBookDescription;
import repository.interfaces.IBookRepository;

import java.sql.*;
import java.time.LocalDate;
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

            if (book.getAuthorId() == null) stmt.setNull(4, Types.INTEGER);
            else stmt.setInt(4, book.getAuthorId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error saving: " + e.getMessage());
            return false;
        }
    }

    // NOTE: keep your existing JOIN with authors + loans
    private static final String FULL_SELECT =
            "SELECT b.id, b.title, b.genre, b.status, b.author_id, " +
                    "COALESCE(a.full_name, 'Unknown') AS author_name, " +
                    "l.borrower_name, l.due_date, l.returned " +
                    "FROM books b " +
                    "LEFT JOIN authors a ON a.id = b.author_id " +
                    "LEFT JOIN loans l ON l.book_id = b.id AND l.returned = FALSE ";

    @Override
    public List<Book> getAllBooks() {
        String sql = FULL_SELECT + "ORDER BY b.id";
        List<Book> books = new ArrayList<>();

        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) books.add(mapRow(rs));

        } catch (Exception e) {
            System.out.println("Error retrieving: " + e.getMessage());
        }
        return books;
    }

    @Override
    public List<Book> getBooksByGenre(String genre) {
        String sql = FULL_SELECT + "WHERE LOWER(b.genre) = LOWER(?) ORDER BY b.id";
        List<Book> books = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, genre);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) books.add(mapRow(rs));
            }
        } catch (Exception e) {
            System.out.println("Error getBooksByGenre: " + e.getMessage());
        }
        return books;
    }

    @Override
    public Book getBookById(int id) {
        String sql = FULL_SELECT + "WHERE b.id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }

        } catch (Exception e) {
            System.out.println("Error retrieving by id: " + e.getMessage());
        }

        return null;
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        Date due = rs.getDate("due_date");
        LocalDate dueDate = (due == null) ? null : due.toLocalDate();
        Boolean returned = (Boolean) rs.getObject("returned");

        return new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("genre"),
                rs.getString("status"),
                (Integer) rs.getObject("author_id"),
                rs.getString("author_name"),
                rs.getString("borrower_name"),
                dueDate,
                returned
        );
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
            System.out.println("Error isBorrowed: " + e.getMessage());
            return false;
        }
    }

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

    @Override
    public boolean returnBook(int bookId) {
        String sql = "UPDATE loans SET returned = TRUE WHERE book_id = ? AND returned = FALSE";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error return: " + e.getMessage());
            return false;
        }
    }

    @Override
    public FullBookDescription getFullBookDescription(int bookId) {
        String sql =
                "SELECT b.id, b.title, b.genre, b.status, b.author_id, " +
                        "COALESCE(a.full_name, 'Unknown') AS author_name, " +
                        "l.borrower_name, l.due_date, l.returned " +
                        "FROM books b " +
                        "LEFT JOIN authors a ON a.id = b.author_id " +
                        "LEFT JOIN loans l ON l.book_id = b.id AND l.returned = FALSE " +
                        "WHERE b.id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return null;

                FullBookDescription d = new FullBookDescription();
                d.setBookId(rs.getInt("id"));
                d.setTitle(rs.getString("title"));
                d.setGenre(rs.getString("genre"));
                d.setStatus(rs.getString("status"));
                d.setAuthorId((Integer) rs.getObject("author_id"));
                d.setAuthorName(rs.getString("author_name"));
                d.setBorrowerName(rs.getString("borrower_name"));

                Date due = rs.getDate("due_date");
                d.setDueDate(due == null ? null : due.toLocalDate());
                d.setReturned((Boolean) rs.getObject("returned"));

                return d;
            }

        } catch (Exception e) {
            System.out.println("Error getFullBookDescription: " + e.getMessage());
            return null;
        }
    }
}
