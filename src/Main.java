package service;

import controllers.LibraryController;
import controllers.UserController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        LibraryController libraryController = new LibraryController();
        UserController userController = new UserController();

        System.out.println("Enter username:");
        String username = scanner.nextLine();
        userController.addUser(username);

        System.out.println("Enter book title:");
        String title = scanner.nextLine();

        System.out.println("Enter book genre:");
        String genre = scanner.nextLine();

        libraryController.addBook(title, genre);
    }
}