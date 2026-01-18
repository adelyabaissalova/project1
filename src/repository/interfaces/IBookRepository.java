package repository.interfaces;

import models.Book;

import java.util.List;

public interface IBookRepository {
    boolean createBook(Book book);

    List<Book> getAllBooks();

    boolean updateStatus(int id, String status);
}
