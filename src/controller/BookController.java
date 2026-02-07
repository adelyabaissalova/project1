package controller;

import models.Book;
import models.BookStatus;
import models.dto.FullBookDescription;
import models.dto.LoanInfo;
import repository.interfaces.IBookRepository;
import repository.interfaces.ILoanRepository;
import security.User;
import util.Validator;

import java.util.List;
import java.util.Scanner;

public class BookController {
    private final IBookRepository books;
    private final ILoanRepository loans;
    private final Scanner sc;
    private final User currentUser;

    public BookController(IBookRepository books, ILoanRepository loans, Scanner sc, User currentUser) {
        this.books = books;
        this.loans = loans;
        this.sc = sc;
        this.currentUser = currentUser;
    }

    private void requireLoggedIn() {
        if (currentUser == null) throw new SecurityException("Access denied");
    }

    private void requireStaff() {
        requireLoggedIn();
        if (!currentUser.isStaff()) throw new SecurityException("Access denied: LIBRARIAN/ADMIN only");
    }

    public void showAllBooks() {
        List<Book> list = books.getAllBooks();
        if (list.isEmpty()) { System.out.println("No books."); return; }
        list.forEach(b -> System.out.printf("%d) %s | %s | %s%n", b.getId(), b.getTitle(), b.getGenre(), b.getStatus()));
    }

    public void showBookById() {
        System.out.print("Book id: ");
        int id = Validator.requirePositiveInt(sc.nextLine(), "Book id");
        Book b = books.getBookById(id);
        if (b == null) { System.out.println("Not found."); return; }
        System.out.printf("%d) %s | %s | %s%n", b.getId(), b.getTitle(), b.getGenre(), b.getStatus());
    }

    public void showByGenre() {
        System.out.print("Genre: ");
        String genre = sc.nextLine();
        Validator.requireNotBlank(genre, "Genre");
        List<Book> list = books.getBooksByGenre(genre);
        if (list.isEmpty()) { System.out.println("No books."); return; }
        list.forEach(b -> System.out.printf("%d) %s | %s | %s%n", b.getId(), b.getTitle(), b.getGenre(), b.getStatus()));
    }

    public void showByCategoryName() {
        System.out.print("Category name: ");
        String name = sc.nextLine();
        Validator.requireNotBlank(name, "Category name");
        List<Book> list = books.getBooksByCategoryName(name);
        if (list.isEmpty()) { System.out.println("No books."); return; }
        list.forEach(b -> System.out.printf("%d) %s | %s | %s%n", b.getId(), b.getTitle(), b.getGenre(), b.getStatus()));
    }

    public void fullDescription() {
        System.out.print("Book id: ");
        int id = Validator.requirePositiveInt(sc.nextLine(), "Book id");
        FullBookDescription dto = books.getFullBookDescription(id);
        if (dto == null) { System.out.println("Not found."); return; }
        System.out.println(dto);
    }

    public void showReadSortedByTitle() {
        List<Book> list = books.getReadBooks();
        if (list.isEmpty()) { System.out.println("No read books."); return; }
        list.forEach(b -> System.out.printf("%d) %s | %s%n", b.getId(), b.getTitle(), b.getStatus()));
    }

    public void borrowBook() {
        requireLoggedIn();
        System.out.print("Book id: ");
        int id = Validator.requirePositiveInt(sc.nextLine(), "Book id");
        if (!books.bookExists(id)) { System.out.println("Not found."); return; }
        boolean ok = loans.borrowBook(id, currentUser.getId());
        System.out.println(ok ? "Borrowed." : "Cannot borrow (already borrowed).");
    }

    public void returnBook() {
        requireLoggedIn();
        System.out.print("Book id: ");
        int id = Validator.requirePositiveInt(sc.nextLine(), "Book id");
        boolean ok = loans.returnBookAsUser(id, currentUser.getId());
        System.out.println(ok ? "Returned." : "You have no active loan for this book.");
    }

    public void myLoans() {
        requireLoggedIn();
        List<LoanInfo> list = loans.getLoansByUser(currentUser.getId());
        if (list.isEmpty()) { System.out.println("No loans."); return; }
        list.forEach(System.out::println);
    }

    public void addBook() {
        requireStaff();

        System.out.print("Title: ");
        String title = sc.nextLine();
        Validator.requireNotBlank(title, "Title");

        System.out.print("Genre: ");
        String genre = sc.nextLine();
        Validator.requireNotBlank(genre, "Genre");

        System.out.print("Status (AVAILABLE/BORROWED/READ/NOT_READ): ");
        String s = sc.nextLine();
        Validator.requireNotBlank(s, "Status");
        BookStatus status = BookStatus.valueOf(s.trim().toUpperCase());

        System.out.print("Category id (blank if none): ");
        String cat = sc.nextLine().trim();
        Integer categoryId = cat.isEmpty() ? null : Validator.requirePositiveInt(cat, "Category id");

        System.out.print("Author id (blank if none): ");
        String auth = sc.nextLine().trim();
        Integer authorId = auth.isEmpty() ? null : Validator.requirePositiveInt(auth, "Author id");

        boolean ok = books.createBook(new Book(title, genre, status, categoryId, authorId));
        System.out.println(ok ? "Added." : "Failed.");
    }

    public void editBook() {
        requireStaff();

        System.out.print("Book id: ");
        int id = Validator.requirePositiveInt(sc.nextLine(), "Book id");
        if (books.getBookById(id) == null) { System.out.println("Not found."); return; }

        System.out.print("New title: ");
        String title = sc.nextLine();
        Validator.requireNotBlank(title, "Title");

        System.out.print("New genre: ");
        String genre = sc.nextLine();
        Validator.requireNotBlank(genre, "Genre");

        System.out.print("Category id (blank if none): ");
        String cat = sc.nextLine().trim();
        Integer categoryId = cat.isEmpty() ? null : Validator.requirePositiveInt(cat, "Category id");

        System.out.print("Author id (blank if none): ");
        String auth = sc.nextLine().trim();
        Integer authorId = auth.isEmpty() ? null : Validator.requirePositiveInt(auth, "Author id");

        boolean ok = books.updateBook(id, title, genre, categoryId, authorId);
        System.out.println(ok ? "Updated." : "Failed.");
    }

    public void setAvailability() {
        requireStaff();

        System.out.print("Book id: ");
        int id = Validator.requirePositiveInt(sc.nextLine(), "Book id");

        System.out.print("Status (AVAILABLE/BORROWED): ");
        String s = sc.nextLine();
        Validator.requireNotBlank(s, "Status");

        BookStatus st = BookStatus.valueOf(s.trim().toUpperCase());
        boolean ok = books.setAvailability(id, st);
        System.out.println(ok ? "Updated." : "Failed.");
    }

    public void whoBorrowed() {
        requireStaff();

        System.out.print("Book id: ");
        int id = Validator.requirePositiveInt(sc.nextLine(), "Book id");

        LoanInfo loan = loans.getActiveLoanByBook(id);
        System.out.println(loan == null ? "No active loan." : loan.toString());
    }

    public void forceReturn() {
        requireStaff();

        System.out.print("Book id: ");
        int id = Validator.requirePositiveInt(sc.nextLine(), "Book id");

        boolean ok = loans.returnBookAsStaff(id);
        System.out.println(ok ? "Returned." : "No active loan.");
    }
}
