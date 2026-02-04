package models;

import java.time.LocalDate;

public class Book {
    private int id;
    private String title;
    private String genre;
    private String status;
    private Integer authorId;
    private String authorName;
    private String borrowerName;
    private LocalDate dueDate;
    private Boolean returned;

    public Book(int id, String title, String genre, String status) {}
    public Book(String title, String genre, Integer authorId) {
        this.title = title;
        this.genre = genre;
        this.authorId = authorId;
        this.status = "Not read";
    }

    public Book(int id, String title, String genre, String status,
                Integer authorId, String authorName,
                String borrowerName, LocalDate dueDate, Boolean returned) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.status = status;
        this.authorId = authorId;
        this.authorName = authorName;
        this.borrowerName = borrowerName;
        this.dueDate = dueDate;
        this.returned = returned;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getStatus() { return status; }
    public Integer getAuthorId() { return authorId; }
    public String getAuthorName() { return authorName; }
    public String getBorrowerName() { return borrowerName; }
    public LocalDate getDueDate() { return dueDate; }
    public Boolean getReturned() { return returned; }
}