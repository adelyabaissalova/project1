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


    public Book() {}


    public Book(int id, String title, String genre, String status) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.status = status;
    }


    public Book(String title, String genre, Integer authorId) {
        this.title = title;
        this.genre = genre;
        this.authorId = authorId;
        this.status = "Not read";
    }


    public Book(int id,
                String title,
                String genre,
                String status,
                Integer authorId,
                String authorName,
                String borrowerName,
                LocalDate dueDate,
                Boolean returned) {
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
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getAuthorId() { return authorId; }
    public void setAuthorId(Integer authorId) { this.authorId = authorId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Boolean getReturned() { return returned; }
    public void setReturned(Boolean returned) { this.returned = returned; }
}