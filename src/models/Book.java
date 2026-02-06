package models;

import java.time.LocalDate;

public class Book {

    private int id;
    private String title;
    private String genre;
    private BookStatus status;

    private Integer authorId;
    private Integer borrowedByUserId;

    private String authorName;     // для JOIN
    private String borrowerName;   // для JOIN
    private LocalDate dueDate;     // для JOIN
    private Boolean returned;      // для JOIN

    public Book() {}

    public Book(int id, String title, String genre, BookStatus status) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.status = status;
    }


    public Book(String title, String genre, Integer authorId) {
        this.title = title;
        this.genre = genre;
        this.authorId = authorId;
        this.status = BookStatus.AVAILABLE;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getBorrowedByUserId() {
        return borrowedByUserId;
    }

    public void setBorrowedByUserId(Integer borrowedByUserId) {
        this.borrowedByUserId = borrowedByUserId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getReturned() {
        return returned;
    }

    public void setReturned(Boolean returned) {
        this.returned = returned;
    }

    public void setCategoryId(Integer categoryId) {
    }

    public Object getCategoryId() {
        return null;
    }
}
