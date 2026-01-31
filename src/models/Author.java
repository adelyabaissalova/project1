package models;

public class Author {
    private int id;
    private String fullName;

    public Author() {}
}
 public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    @Override
    public String toString() {
        return id + " | " + fullName;
    }
}
