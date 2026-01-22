import controller.interfaces.IBookController;

import java.util.Scanner;

public class MyApplication {
    private final IBookController controller;
    private final Scanner scanner;

    public MyApplication(IBookController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== Online library ===");
            System.out.println("1. Find book by ID (show author/genre/status)");
            System.out.println("2. Show all books");
            System.out.println("3. Mark as read");
            System.out.println("4. Mark as not read");
            System.out.println("5. Add book (with authorId)");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice = readInt();

            if (choice == 1) {
                System.out.print("Enter book ID: ");
                int id = readInt();
                System.out.println(controller.getById(id));
            } else if (choice == 2) {
                System.out.println(controller.showAll());
            } else if (choice == 3) {
                System.out.print("ID of the book: ");
                int id = readInt();
                System.out.println(controller.markRead(id));
            } else if (choice == 4) {
                System.out.print("ID of the book: ");
                int id = readInt();
                System.out.println(controller.markNotRead(id));
            } else if (choice == 5) {
                System.out.print("Title: ");
                String title = scanner.nextLine();

                System.out.print("Genre: ");
                String genre = scanner.nextLine();

                System.out.print("Author ID (0 if unknown): ");
                int authorId = readInt();
                Integer author = (authorId == 0) ? null : authorId;

                System.out.println(controller.create(title, genre, author));
            } else if (choice == 0) {
                System.out.println("Goodbye.");
                break;
            } else {
                System.out.println("Error: choose 0-5.");
            }
        }
    }

    private int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Enter a number: ");
            scanner.nextLine();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}
