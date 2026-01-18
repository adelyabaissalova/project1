package models;

public class ReadingStatus {
    private int id;
    private int userId;
    private int bookId;
    private String status;

    public ReadingStatus(int userId, int bookId, String status) {
        this.userId = userId;
        this.bookId = bookId;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
