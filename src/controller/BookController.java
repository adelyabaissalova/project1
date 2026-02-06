package controller;

import controller.interfaces.IBookController;
import models.Book;
import repository.interfaces.IBookRepository;

import java.util.List;

public class BookController implements IBookController {

    private final IBookRepository repo;

    public BookController(IBookRepository repo) {
        this.repo = repo;
    }

    @Override
    public String getById(int id) {
        Book book = repo.getById(id);
        return book == null ? "Book not found." : book.toString();
    }

    @Override
    public String create(String title, String genre, int authorId) {
        boolean created = repo.create(new Book(title, genre, authorId));
        return created ? "Book added." : "Failed to add book.";
    }

    @Override
    public String showAll() {
        List<Book> books = repo.getAll();
        if (books.isEmpty()) return "No books found.";

        StringBuilder sb = new StringBuilder();
        for (Book b : books) {
            sb.append(b).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String showByGenre(String genre) {
        List<Book> books = repo.getByGenre(genre);
        if (books.isEmpty()) return "No books in this genre.";

        StringBuilder sb = new StringBuilder();
        for (Book b : books) {
            sb.append(b).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String showReadSortedByTitle() {
        List<Book> books = repo.getReadSortedByTitle();
        if (books.isEmpty()) return "No READ books.";

        StringBuilder sb = new StringBuilder();
        for (Book b : books) {
            sb.append(b).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String getFullDescription(int id) {
        Book book = repo.getById(id);
        return book == null ? "Book not found." : book.fullDescription();
    }
}
