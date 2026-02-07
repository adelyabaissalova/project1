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
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, book.getTitle());
            st.setString(2, book.getGenre());
            st.setString(3, book.getStatus().name());
            st.setObject(4, book.getCategoryId());
            st.setObject(5, book.getAuthorId());
            return st.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("createBook error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateBook(int id, String title, String genre, Integer categoryId, Integer authorId) {
        String sql = "UPDATE books SET title=?, genre=?, category_id=?, author_id=? WHERE id=?";
        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, title);
            st.setString(2, genre);
            st.setObject(3, categoryId);
            st.setObject(4, authorId);
            st.setInt(5, id);
            return st.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("updateBook error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Book> getAllBooks() {
        String sql = "SELECT id, title, genre, status, category_id, author_id FROM books ORDER BY id";
        List<Book> list = new ArrayList<>();
        try (Connection conn = db.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                BookStatus status = BookStatus.valueOf(rs.getString("status"));
                Book b = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("genre"), status);
                b.setCategoryId(rs.getObject("category_id", Integer.class));
                b.setAuthorId(rs.getObject("author_id", Integer.class));
                list.add(b);
            }
        } catch (Exception e) {
            System.out.println("getAllBooks error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Book getBookById(int id) {
        String sql = "SELECT id, title, genre, status, category_id, author_id FROM books WHERE id=?";
        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    BookStatus status = BookStatus.valueOf(rs.getString("status"));
                    Book b = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("genre"), status);
                    b.setCategoryId(rs.getObject("category_id", Integer.class));
                    b.setAuthorId(rs.getObject("author_id", Integer.class));
                    return b;
                }
            }
        } catch (Exception e) {
            System.out.println("getBookById error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateStatus(int bookId, BookStatus status) {
        String sql = "UPDATE books SET status=? WHERE id=?";
        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, status.name());
            st.setInt(2, bookId);
            return st.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("updateStatus error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean setAvailability(int bookId, BookStatus status) {
        if (status != BookStatus.AVAILABLE && status != BookStatus.BORROWED) {
            throw new IllegalArgumentException("Only AVAILABLE/BORROWED allowed");
        }
        return updateStatus(bookId, status);
    }

    @Override
    public boolean bookExists(int bookId) {
        String sql = "SELECT 1 FROM books WHERE id=?";
        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, bookId);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.out.println("bookExists error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public FullBookDescription getFullBookDescription(int bookId) {
        String sql = """
            SELECT
                b.id,
                b.title,
                b.genre,
                b.status,
                a.name AS author_name,
                c.name AS category_name,
                u.username AS borrower_username,
                l.due_date
            FROM books b
            LEFT JOIN authors a ON b.author_id = a.id
            LEFT JOIN categories c ON b.category_id = c.id
            LEFT JOIN loans l ON b.id = l.book_id AND l.returned = FALSE
            LEFT JOIN users u ON l.user_id = u.id
            WHERE b.id = ?
        """;

        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, bookId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Date dd = rs.getDate("due_date");
                    return new FullBookDescription(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            rs.getString("status"),
                            rs.getString("author_name"),
                            rs.getString("category_name"),
                            rs.getString("borrower_username"),
                            dd == null ? null : dd.toLocalDate()
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("getFullBookDescription error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Book> getBooksByGenre(String genre) {
        String sql = "SELECT id, title, genre, status, category_id, author_id FROM books WHERE LOWER(genre)=LOWER(?) ORDER BY id";
        List<Book> list = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, genre);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    BookStatus status = BookStatus.valueOf(rs.getString("status"));
                    Book b = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("genre"), status);
                    b.setCategoryId(rs.getObject("category_id", Integer.class));
                    b.setAuthorId(rs.getObject("author_id", Integer.class));
                    list.add(b);
                }
            }
        } catch (Exception e) {
            System.out.println("getBooksByGenre error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Book> getBooksByCategory(int categoryId) {
        String sql = "SELECT id, title, genre, status, category_id, author_id FROM books WHERE category_id=? ORDER BY id";
        List<Book> list = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, categoryId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    BookStatus status = BookStatus.valueOf(rs.getString("status"));
                    Book b = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("genre"), status);
                    b.setCategoryId(rs.getObject("category_id", Integer.class));
                    b.setAuthorId(rs.getObject("author_id", Integer.class));
                    list.add(b);
                }
            }
        } catch (Exception e) {
            System.out.println("getBooksByCategory error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Book> getBooksByCategoryName(String categoryName) {
        String sql = """
            SELECT b.id, b.title, b.genre, b.status, b.category_id, b.author_id
            FROM books b
            JOIN categories c ON b.category_id = c.id
            WHERE LOWER(c.name)=LOWER(?)
            ORDER BY b.id
        """;
        List<Book> list = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, categoryName);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    BookStatus status = BookStatus.valueOf(rs.getString("status"));
                    Book b = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("genre"), status);
                    b.setCategoryId(rs.getObject("category_id", Integer.class));
                    b.setAuthorId(rs.getObject("author_id", Integer.class));
                    list.add(b);
                }
            }
        } catch (Exception e) {
            System.out.println("getBooksByCategoryName error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Book> getReadBooks() {
        return getAllBooks().stream()
                .filter(b -> b.getStatus() == BookStatus.READ)
                .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()))
                .toList();
    }

    @Override
    public List<Book> getNotReadBooks() {
        return getAllBooks().stream()
                .filter(b -> b.getStatus() == BookStatus.NOT_READ)
                .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()))
                .toList();
    }
}