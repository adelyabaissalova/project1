package repository.interfaces;

import models.Book;
import models.BookStatus;
import models.dto.FullBookDescription;

import java.util.List;

public interface IBookRepository {

    // CRUD
    boolean createBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(int id);

    // Filtering
    List<Book> getBooksByGenre(String genre);
    List<Book> getBooksByCategory(int categoryId);
    List<Book> getBooksByCategoryName(String categoryName);
    List<Book> getReadBooks();
    List<Book> getNotReadBooks();

    // Status
    boolean updateStatus(int bookId, BookStatus status);

    // Borrow/return
    boolean borrowBook(int bookId, String borrowerName);
    boolean returnBook(int bookId);
    boolean isBookBorrowed(int bookId);
    boolean bookExists(int bookId);

    // Full description (JOIN)
    FullBookDescription getFullBookDescription(int bookId);
}