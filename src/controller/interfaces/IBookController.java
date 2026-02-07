package controller.interfaces;

import models.BookStatus;

public interface IBookController {
    String create(String title, String genre, Integer authorId);
    String showAll();
    String markRead(int id);
    String markNotRead(int id);

    String getById(int id);

    String borrowBook(int bookId, String borrowerName);
    String returnBook(int bookId);

    String getFullDescription(int bookId);

    String showByGenre(String genre);

    String showReadSortedByTitle();

    String changeStatus(int id, BookStatus status);

    String showAvailableBooks();

    String showBorrowedBooks();
}