package models;

public class Book {
    private int id;
    private String title;
    private String genre;
    private BookStatus status;
    private Integer categoryId;
    private Integer authorId;

    public Book(int id, String title, String genre, BookStatus status) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.status = status;
    }

    public Book(String title, String genre, BookStatus status, Integer categoryId, Integer authorId) {
        this.title = title;
        this.genre = genre;
        this.status = status;
        this.categoryId = categoryId;
        this.authorId = authorId;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public BookStatus getStatus() { return status; }
    public Integer getCategoryId() { return categoryId; }
    public Integer getAuthorId() { return authorId; }

    public void setStatus(BookStatus status) { this.status = status; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public void setAuthorId(Integer authorId) { this.authorId = authorId; }
}
