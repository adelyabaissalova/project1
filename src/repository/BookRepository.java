package repository;

import data.interfaces.IDB;
import models.Book;
import repository.interfaces.IBookRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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

            // лучше executeUpdate (возвращает сколько строк добавилось)
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error saving: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Book> getAllBooks() {
        // лучше явно перечислить колонки
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

            return stmt.executeUpdate() > 0; // true если реально обновило

        } catch (Exception e) {
            System.out.println("Error update: " + e.getMessage());
            return false;
        }
    }

    // ✅ ДОБАВЛЕНО: чтобы не было красным (если интерфейс требует этот метод)
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
            System.out.println("Error get by id: " + e.getMessage());
        }

        return null; // если не найдено
    }
}
