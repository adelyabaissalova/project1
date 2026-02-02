package repository.interfaces;

import models.Book;
import java.util.List;

public interface IBookRepository {
    boolean createBook(Book book);
    List<Book> getAllBooks();
    boolean updateStatus(int id, String status);

    Book getBookById(int id);

    boolean borrowBook(int bookId, String borrowerName);
    boolean returnBook(int bookId);

    boolean isBookBorrowed(int bookId);
    boolean bookExists(int bookId);
}
