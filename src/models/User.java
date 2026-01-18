package models;

public class User {
    private int id;
    private String username;

    public User(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
