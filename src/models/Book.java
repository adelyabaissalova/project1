package models;

public class Book {
    private int id;
    private String title;
    private String genre;

    public Book() {
    }

    public Book(String title, String genre) {
        this.title = title;
        this.genre = genre;
    }

    public Book(int id, String title, String genre) {
        this.id = id;
        this.title = title;
        this.genre = genre;
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
}
