package models.dto;

import java.time.LocalDate;

public class FullBookDescription {
    private int bookId;
    private String title;
    private String genre;
    private String status;
    private String authorName;
    private String categoryName;
    private String borrowedBy;
    private LocalDate dueDate;
    private boolean returned;

    public FullBookDescription(
            int bookId,
            String title,
            String genre,
            String status,
            String authorName,
            String categoryName,
            String borrowedBy,
            LocalDate dueDate,
            boolean returned

    ) {
        this.bookId = bookId;
        this.title = title;
        this.genre = genre;
        this.status = status;
        this.authorName = authorName;
        this.categoryName = categoryName;
        this.borrowedBy = borrowedBy;
        this.dueDate = dueDate;
        this.returned = returned;
    }

    @Override
    public String toString() {
        return """
                Book ID: %d
                Title: %s
                Genre: %s
                Status: %s
                Author: %s
                Category: %s
                Borrowed by: %s
                """.formatted(bookId, title, genre, status,
                authorName, categoryName,
                borrowedBy == null ? "—" : borrowedBy);
    }

    public int getBookId() {
        return bookId;
    }


    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getStatus() {
        return status;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getBorrowedBy() {
        return borrowedBy;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void getBorrowerName() {
    }
}


