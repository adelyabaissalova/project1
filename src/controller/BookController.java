package controller;

import controller.interfaces.IBookController;
import models.Book;
import models.BookStatus;
import models.dto.FullBookDescription;
import repository.interfaces.IBookRepository;
import util.Validator;

import java.util.Comparator;
import java.util.stream.Collectors;

import static models.BookStatus.*;

public class BookController implements IBookController {

    private final IBookRepository repo;

    public BookController(IBookRepository repo) {
        this.repo = repo;
    }

    private static boolean test(Book b) {
        if (READ != b.getStatus()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String create(String title, String genre, Integer authorId) {
        title = Validator.normalize(title);
        genre = Validator.normalize(genre);

        if (!Validator.isNonBlank(title)) return "Title can't be empty.";
        if (!Validator.isNonBlank(genre)) return "Genre can't be empty.";
        if (authorId != null && authorId <= 0) return "Author ID must be positive or 0.";

        Book book = new Book(title, genre, authorId);
        return repo.createBook(book) ? "Book is saved." : "Error occured.";
    }

    @Override
    public String showAll() {
        var books = repo.getAllBooks()
                .stream()
                .sorted(Comparator.comparingInt(Book::getId))
                .collect(Collectors.toList());

        return formatBooks(books);
    }

    @Override
    public String showByGenre(String genre) {
        genre = Validator.normalize(genre);
        if (!Validator.isNonBlank(genre)) return "Genre can't be empty.";

        var books = repo.getBooksByGenre(genre);
        if (books.isEmpty()) return "No books found for genre: " + genre;

        return formatBooks(books);
    }

    @Override
    public String showReadSortedByTitle() {
        var books = repo.getAllBooks()
                .stream()
                .filter(BookController::test)
                .sorted(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER))
                .toList();

        return "No read books found.";
    }

    private String formatBooks(Iterable<Book> books) {
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
        if (!Validator.isPositiveId(id)) return "ID must be positive.";
        return repo.updateStatus(id, READ) ? "Status is changed." : "Error.";
    }

    @Override
    public String markNotRead(int id) {
        if (!Validator.isPositiveId(id)) return "ID must be positive.";
        return repo.updateStatus(id, NOT_READ) ? "Status is changed." : "Error.";
    }

    @Override
    public String getById(int id) {
        if (!Validator.isPositiveId(id)) return "ID must be positive.";

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
        if (!Validator.isPositiveId(bookId)) return "Book ID must be positive.";
        borrowerName = Validator.normalize(borrowerName);
        if (!Validator.isNonBlank(borrowerName)) return "Name can't be empty.";

        if (!repo.bookExists(bookId)) return "Book not found.";
        if (repo.isBookBorrowed(bookId)) return "This book is already borrowed.";

        boolean ok = repo.borrowBook(bookId, borrowerName);
        if (!ok) return "Error borrowing book.";

        return "Borrowed successfully!\n" + getById(bookId);
    }

    @Override
    public String returnBook(int bookId) {
        if (!Validator.isPositiveId(bookId)) return "Book ID must be positive.";
        if (!repo.bookExists(bookId)) return "Book not found.";

        boolean ok = repo.returnBook(bookId);
        return ok ? "Returned successfully." : "This book isn't borrowed.";
    }

    @Override
    public String getFullDescription(int bookId) {
        if (!Validator.isPositiveId(bookId)) return "Book ID must be positive.";

        FullBookDescription d = repo.getFullBookDescription(bookId);
        if (d == null) return "Book not found.";

        StringBuilder sb = new StringBuilder();
        sb.append("=== FULL BOOK DESCRIPTION (JOIN) ===\n");
        sb.append("Book ID: ").append(d.getBookId()).append("\n");
        sb.append("Title: ").append(d.getTitle()).append("\n");
        sb.append("Genre(Category): ").append(d.getGenre()).append("\n");
        sb.append("Status: ").append(d.getStatus()).append("\n");
        sb.append("Author: ").append(d.getAuthorName()).append("\n");

        d.getBorrowerName();
        sb.append("Borrowed by: -\n");
        sb.append("Due date: -\n");
        sb.append("Returned: -\n");
        return sb.toString();
    }

    @Override
    public String changeStatus(int id, BookStatus status) {
        if (!Validator.isPositiveId(id)) return "ID must be positive.";

        boolean ok = repo.updateStatus(id, status);
        return ok ? "Status updated to " + status : "Error updating status.";
    }

    @Override
    public String showAvailableBooks() {
        var books = repo.getAllBooks()
                .stream()
                .filter(b -> b.getStatus() == AVAILABLE)
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());

        if (books.isEmpty()) return "No available books.";

        return formatBooks(books);
    }
    @Override
    public String showBorrowedBooks() {
        var books = repo.getAllBooks()
                .stream()
                .filter(b -> b.getStatus() == BORROWED)
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());

        if (books.isEmpty()) return "No borrowed books.";

        return formatBooks(books);
    }
}