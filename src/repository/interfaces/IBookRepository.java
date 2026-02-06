package repository.interfaces;

import models.Book;
import models.BookStatus;
import models.dto.FullBookDescription;

import java.util.List;

public interface IBookRepository {
    boolean createBook(Book book);
    boolean updateBook(int id, String title, String genre, Integer categoryId, Integer authorId);

    List<Book> getAllBooks();
    Book getBookById(int id);

    boolean updateStatus(int bookId, BookStatus status);
    boolean setAvailability(int bookId, BookStatus status);

    boolean bookExists(int bookId);

    FullBookDescription getFullBookDescription(int bookId);

    List<Book> getBooksByGenre(String genre);
    List<Book> getBooksByCategory(int categoryId);
    List<Book> getBooksByCategoryName(String categoryName);

    List<Book> getReadBooks();
    List<Book> getNotReadBooks();
}
