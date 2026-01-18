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
    public String create(String title, String genre) {
        Book book = new Book(title, genre);
        boolean created = repo.createBook(book);
        return created ? "Book is saved." : "Error occured.";
    }


    @Override
    public String showAll() {
        var books = repo.getAllBooks();
        StringBuilder sb = new StringBuilder();
        for (var b : books) {
            sb.append(b.getId()).append(" | ")
                    .append(b.getTitle()).append(" | ")
                    .append(b.getGenre()).append(" | ")
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
        return repo.updateStatus(id, "Not read yet") ? "Status is changed." : "Error.";
    }
}
