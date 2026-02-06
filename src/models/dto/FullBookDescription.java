package models.dto;

import java.time.LocalDate;

public class FullBookDescription {
    private final int id;
    private final String title;
    private final String genre;
    private final String status;
    private final String authorName;
    private final String categoryName;
    private final String borrowerUsername;
    private final LocalDate dueDate;

    public FullBookDescription(
            int id,
            String title,
            String genre,
            String status,
            String authorName,
            String categoryName,
            String borrowerUsername,
            LocalDate dueDate
    ) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.status = status;
        this.authorName = authorName;
        this.categoryName = categoryName;
        this.borrowerUsername = borrowerUsername;
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return """
                Id: %d
                Title: %s
                Genre: %s
                Status: %s
                Author: %s
                Category: %s
                Borrower: %s
                Due date: %s
                """.formatted(
                id,
                title,
                genre,
                status,
                authorName == null ? "-" : authorName,
                categoryName == null ? "-" : categoryName,
                borrowerUsername == null ? "-" : borrowerUsername,
                dueDate == null ? "-" : dueDate.toString()
        );
    }
}
