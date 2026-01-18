package controllers;

import controllers.interfaces.LibraryController;
import service.LibraryService;

public class LibraryController implements LibraryController {

    private final LibraryService service = new LibraryService();

    @Override
    public void addBook(String title, String genre) {
        boolean result = service.addBook(title, genre);

        if (result) {
            System.out.println("Book added successfully");
        } else {
            System.out.println("Failed to add book");
        }
    }
}
