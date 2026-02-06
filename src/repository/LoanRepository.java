package repository;

import data.interfaces.IDB;
import models.BookStatus;
import models.dto.LoanInfo;
import repository.interfaces.ILoanRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanRepository implements ILoanRepository {
    private final IDB db;

    public LoanRepository(IDB db) {
        if (db == null) throw new IllegalArgumentException("DB is null");
        this.db = db;
    }

    @Override
    public boolean borrowBook(int bookId, int userId) {
        String check = "SELECT 1 FROM loans WHERE book_id=? AND returned=FALSE LIMIT 1";
        String insert = "INSERT INTO loans(book_id, user_id, due_date, returned) VALUES (?, ?, CURRENT_DATE + INTERVAL '14 days', FALSE)";
        String updBook = "UPDATE books SET status=? WHERE id=?";

        try (Connection conn = db.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement c = conn.prepareStatement(check)) {
                c.setInt(1, bookId);
                try (ResultSet rs = c.executeQuery()) {
                    if (rs.next()) { conn.rollback(); return false; }
                }
            }

            try (PreparedStatement ins = conn.prepareStatement(insert)) {
                ins.setInt(1, bookId);
                ins.setInt(2, userId);
                if (ins.executeUpdate() <= 0) { conn.rollback(); return false; }
            }

            try (PreparedStatement up = conn.prepareStatement(updBook)) {
                up.setString(1, BookStatus.BORROWED.name());
                up.setInt(2, bookId);
                up.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            System.out.println("borrowBook error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean returnBookAsUser(int bookId, int userId) {
        String updLoan = "UPDATE loans SET returned=TRUE WHERE book_id=? AND user_id=? AND returned=FALSE";
        String updBook = "UPDATE books SET status=? WHERE id=?";

        try (Connection conn = db.getConnection()) {
            conn.setAutoCommit(false);

            int updated;
            try (PreparedStatement st = conn.prepareStatement(updLoan)) {
                st.setInt(1, bookId);
                st.setInt(2, userId);
                updated = st.executeUpdate();
            }

            if (updated <= 0) { conn.rollback(); return false; }

            try (PreparedStatement st2 = conn.prepareStatement(updBook)) {
                st2.setString(1, BookStatus.AVAILABLE.name());
                st2.setInt(2, bookId);
                st2.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            System.out.println("returnBookAsUser error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean returnBookAsStaff(int bookId) {
        String updLoan = "UPDATE loans SET returned=TRUE WHERE book_id=? AND returned=FALSE";
        String updBook = "UPDATE books SET status=? WHERE id=?";

        try (Connection conn = db.getConnection()) {
            conn.setAutoCommit(false);

            int updated;
            try (PreparedStatement st = conn.prepareStatement(updLoan)) {
                st.setInt(1, bookId);
                updated = st.executeUpdate();
            }

            if (updated <= 0) { conn.rollback(); return false; }

            try (PreparedStatement st2 = conn.prepareStatement(updBook)) {
                st2.setString(1, BookStatus.AVAILABLE.name());
                st2.setInt(2, bookId);
                st2.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            System.out.println("returnBookAsStaff error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<LoanInfo> getLoansByUser(int userId) {
        String sql = """
            SELECT l.id AS loan_id, b.id AS book_id, b.title, u.username, l.due_date, l.returned
            FROM loans l
            JOIN books b ON l.book_id = b.id
            JOIN users u ON l.user_id = u.id
            WHERE l.user_id = ?
            ORDER BY l.returned ASC, l.due_date ASC
        """;
        List<LoanInfo> list = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, userId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(new LoanInfo(
                            rs.getInt("loan_id"),
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("username"),
                            rs.getDate("due_date").toLocalDate(),
                            rs.getBoolean("returned")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("getLoansByUser error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public LoanInfo getActiveLoanByBook(int bookId) {
        String sql = """
            SELECT l.id AS loan_id, b.id AS book_id, b.title, u.username, l.due_date, l.returned
            FROM loans l
            JOIN books b ON l.book_id = b.id
            JOIN users u ON l.user_id = u.id
            WHERE l.book_id = ? AND l.returned = FALSE
            LIMIT 1
        """;

        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, bookId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new LoanInfo(
                            rs.getInt("loan_id"),
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("username"),
                            rs.getDate("due_date").toLocalDate(),
                            rs.getBoolean("returned")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("getActiveLoanByBook error: " + e.getMessage());
        }
        return null;
    }
}
