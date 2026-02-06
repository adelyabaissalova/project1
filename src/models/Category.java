package models;

public class Category {
    private int id;
    private String name;

    // No-argument constructor
    public Category() {
    }

    // Constructor with name
    public Category(String name) {
        this.name = name;
    }

    // ===== GETTERS =====
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // ===== SETTERS =====
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}