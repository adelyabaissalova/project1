package models.dto;

import java.time.LocalDate;

public class LoanInfo {
    public final int loanId;
    public final int bookId;
    public final String bookTitle;
    public final String username;
    public final LocalDate dueDate;
    public final boolean returned;

    public LoanInfo(int loanId, int bookId, String bookTitle, String username, LocalDate dueDate, boolean returned) {
        this.loanId = loanId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.username = username;
        this.dueDate = dueDate;
        this.returned = returned;
    }

    @Override
    public String toString() {
        return "%d) Book #%d: %s | user=%s | due=%s | returned=%s"
                .formatted(loanId, bookId, bookTitle, username, dueDate, returned);
    }
}
