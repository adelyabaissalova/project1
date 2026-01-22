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
        boolean created = repo.createBook(book);
        return created ? "Book is saved." : "Error occurred.";
    }

    @Override
    public String showAll() {
        var books = repo.getAllBooks();
        if (books.isEmpty()) return "No books.\n";

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
        if (b == null) return "Book not found.\n";


        return "ID: " + b.getId() + "\n" +
                "Title: " + b.getTitle() + "\n" +
                "Genre: " + b.getGenre() + "\n" +
                "Author: " + b.getAuthorName() + "\n" +
                "Status: " + b.getStatus() + "\n";
    }
}
