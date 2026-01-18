package models;

public class Book {
    private int id;
    private String title;
    private String genre;
    private String status;

    public Book() {
    }

    public Book(String title, String genre) {
        this.title = title;
        this.genre = genre;
        this.status = "Not read";
    }

    public Book(int id, String title, String genre, String status) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.status = status;
    }

    public int getId() {
        return id;
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
}
