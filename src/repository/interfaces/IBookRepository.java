package repository.interfaces;

import models.Book;
import models.BookStatus;
import models.dto.FullBookDescription;

import java.util.List;

public interface IBookRepository {


    boolean createBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(int id);


    List<Book> getBooksByGenre(String genre);
    List<Book> getBooksByCategory(int categoryId);
    List<Book> getBooksByCategoryName(String categoryName);
    List<Book> getReadBooks();
    List<Book> getNotReadBooks();


    boolean updateStatus(int bookId, BookStatus status);


    boolean borrowBook(int bookId, String borrowerName);
    boolean returnBook(int bookId);
    boolean isBookBorrowed(int bookId);
    boolean bookExists(int bookId);


    FullBookDescription getFullBookDescription(int bookId);
}