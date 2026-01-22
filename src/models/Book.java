package models;

public class Book {
    private int id;
    private String title;
    private String genre;
    private String status;

    private Integer authorId;
    private String authorName;

    public Book() { }


    public Book(String title, String genre, Integer authorId) {
        this.title = title;
        this.genre = genre;
        this.authorId = authorId;
        this.status = "Not read";
    }


    public Book(int id, String title, String genre, String status, Integer authorId, String authorName) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.status = status;
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getStatus() { return status; }
    public Integer getAuthorId() { return authorId; }
    public String getAuthorName() { return authorName; }
}
