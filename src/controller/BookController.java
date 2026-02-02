package controller;

import controller.interfaces.IBookController;
import models.Book;
import repository.interfaces.IBookRepository;

public class BookController implements IBookController {
    private final IBookRepository repo;

    public BookController(IBookRepository repo) {
        this.repo = repo;
    }

    @Override
    public String create(String title, String genre, Integer authorId) {
        Book book = new Book(title, genre, authorId);
        return repo.createBook(book) ? "Book is saved." : "Error occured.";
    }

    @Override
    public String showAll() {
        var books = repo.getAllBooks();
        StringBuilder sb = new StringBuilder();
        for (var b : books) {
            sb.append(b.getId()).append(" | ")
                    .append(b.getTitle()).append(" | ")
                    .append(b.getGenre()).append(" | ")
                    .append(b.getAuthorName()).append(" | ")
                    .append(b.getStatus()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String markRead(int id) {
        return repo.updateStatus(id, "Read") ? "Status is changed." : "Error.";
    }

    @Override
    public String markNotRead(int id) {
        return repo.updateStatus(id, "Not read") ? "Status is changed." : "Error.";
    }

    @Override
    public String getById(int id) {
        Book b = repo.getBookById(id);
        if (b == null) return "Book not found.";

        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(b.getId()).append("\n");
        sb.append("Title: ").append(b.getTitle()).append("\n");
        sb.append("Genre: ").append(b.getGenre()).append("\n");
        sb.append("Author: ").append(b.getAuthorName()).append("\n");
        sb.append("Status: ").append(b.getStatus()).append("\n");

        if (b.getBorrowerName() != null) {
            sb.append("Borrowed by: ").append(b.getBorrowerName()).append("\n");
            sb.append("Due date: ").append(b.getDueDate()).append("\n");
            sb.append("Returned: false\n");
        } else {
            sb.append("Borrowed by: -\n");
            sb.append("Due date: -\n");
            sb.append("Returned: -\n");
        }
        return sb.toString();
    }

    @Override
    public String borrowBook(int bookId, String borrowerName) {
        if (!repo.bookExists(bookId)) return "Book not found.";
        if (repo.isBookBorrowed(bookId)) return "This book is already borrowed.";
        if (borrowerName == null || borrowerName.trim().isEmpty()) return "Name can't be empty.";

        boolean ok = repo.borrowBook(bookId, borrowerName.trim());
        if (!ok) return "Error borrowing book.";

        return "Borrowed successfully!\n" + getById(bookId);
    }

    @Override
    public String returnBook(int bookId) {
        if (!repo.bookExists(bookId)) return "Book not found.";
        boolean ok = repo.returnBook(bookId);
        return ok ? "Returned successfully." : "This book isn't borrowed.";
    }
}
