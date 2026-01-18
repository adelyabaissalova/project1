package service;

import models.Book;
import repositories.BookRepository;

public class LibraryService {

    private final BookRepository bookRepository = new BookRepository();

    public boolean addBook(String title, String genre) {
        Book book = new Book(title, genre);
        return bookRepository.save(book);
    }

    public String recommendGenre(int readBooksCount) {
        if (readBooksCount >= 5) {
            return "Fantasy";
        } else {
            return "Romance";
        }
    }
}