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
            System.out.println("\n Online library ");
            System.out.println("1. Find book by ID");
            System.out.println("2. Show all books");
            System.out.println("3. Mark as read");
            System.out.println("4. Mark as not read");
            System.out.println("5. Add book");
            System.out.println("6. Borrow book");
            System.out.println("7. Return book");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.print("Enter book ID: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.println(controller.getById(id));

            } else if (choice == 2) {
                System.out.println(controller.showAll());

            } else if (choice == 3) {
                System.out.print("ID of the book: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.println(controller.markRead(id));

            } else if (choice == 4) {
                System.out.print("ID of the book: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                System.out.println(controller.markNotRead(id));

            } else if (choice == 5) {
                System.out.print("Title: ");
                String title = scanner.nextLine();

                System.out.print("Genre: ");
                String genre = scanner.nextLine();

                System.out.print("Author ID (or 0 if none): ");
                int a = scanner.nextInt();
                scanner.nextLine();
                Integer authorId = (a == 0) ? null : a;

                System.out.println(controller.create(title, genre, authorId));

            } else if (choice == 6) {
                System.out.print("Book ID: ");
                int bookId = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Your name: ");
                String name = scanner.nextLine();

                System.out.println(controller.borrowBook(bookId, name));

            } else if (choice == 7) {
                System.out.print("Book ID: ");
                int bookId = scanner.nextInt();
                scanner.nextLine();

                System.out.println(controller.returnBook(bookId));

            } else if (choice == 0) {
                System.out.println("Goodbye.");
                break;

            } else {
                System.out.println("Error.");
            }
        }
    }
}
