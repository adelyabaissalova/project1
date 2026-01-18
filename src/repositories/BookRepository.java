package repositories;

import data.PostgresDB;
import models.Book;
import repositories.interfaces.BookRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BookRepository implements BookRepository {

    private final PostgresDB db = new PostgresDB();

    @Override
    public boolean save(Book book) {
        String sql = "INSERT INTO books(title, genre) VALUES (?, ?)";

        try (Connection con = db.connect();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, book.getTitle());
            st.setString(2, book.getGenre());
            st.executeUpdate();
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
