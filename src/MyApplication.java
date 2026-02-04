import controller.interfaces.IBookController;
import security.AuthService;
import security.Role;
import security.SessionContext;
import security.User;

import java.util.Scanner;

public class MyApplication {
    private final IBookController controller;
    private final Scanner scanner;
    private final AuthService authService;

    public MyApplication(IBookController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
        this.authService = new AuthService();
    }

    public void start() {
        login();

        while (true) {
            Role role = SessionContext.getRole();

            int choice;
            if (role == Role.ADMIN) {
                choice = adminMenu();
                if (handleAdmin(choice)) break;

            } else if (role == Role.LIBRARIAN) {
                choice = librarianMenu();
                if (handleLibrarian(choice)) break;

            } else {
                choice = userMenu();
                if (handleUser(choice)) break;
            }
        }
    }

    private int adminMenu() {;
        System.out.println("1. Find book by ID");
        System.out.println("2. Show all books");
        System.out.println("3. Mark as read");
        System.out.println("4. Mark as not read");
        System.out.println("5. Add book");
        System.out.println("6. Borrow book");
        System.out.println("7. Return book");
        System.out.println("8. Show books by genre ");
        System.out.println("9. Show READ books sorted by title ");
        System.out.println("10. Get FULL book description ");
        System.out.println("11. Logout");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
        return readInt();
    }

    private int librarianMenu() {
        System.out.println("1. Find book by ID");
        System.out.println("2. Show all books");
        System.out.println("3. Mark as read");
        System.out.println("4. Mark as not read");
        System.out.println("6. Borrow book");
        System.out.println("7. Return book");
        System.out.println("8. Show books by genre ");
        System.out.println("10. Get FULL book description ");
        System.out.println("11. Logout");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
        return readInt();
    }

    private int userMenu() {
        System.out.println("1. Find book by ID");
        System.out.println("2. Show all books");
        System.out.println("6. Borrow book");
        System.out.println("7. Return book");
        System.out.println("8. Show books by genre ");
        System.out.println("10. Get FULL book description ");
        System.out.println("11. Logout");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
        return readInt();
    }

    private boolean handleAdmin(int choice) {
        switch (choice) {
            case 1 -> findById();
            case 2 -> System.out.println(controller.showAll());
            case 3 -> markRead();
            case 4 -> markNotRead();
            case 5 -> addBook();
            case 6 -> borrowBook();
            case 7 -> returnBook();
            case 8 -> showByGenre();
            case 9 -> System.out.println(controller.showReadSortedByTitle());
            case 10 -> fullDescription();
            case 11 -> login();
            case 0 -> { System.out.println("Goodbye."); return true; }
            default -> System.out.println("Unknown option.");
        }
        return false;
    }

    private boolean handleLibrarian(int choice) {
        switch (choice) {
            case 1 -> findById();
            case 2 -> System.out.println(controller.showAll());
            case 3 -> markRead();
            case 4 -> markNotRead();
            case 6 -> borrowBook();
            case 7 -> returnBook();
            case 8 -> showByGenre();
            case 10 -> fullDescription();
            case 11 -> login();
            case 0 -> { System.out.println("Goodbye."); return true; }
            default -> System.out.println("Unknown option.");
        }
        return false;
    }

    private boolean handleUser(int choice) {
        switch (choice) {
            case 1 -> findById();
            case 2 -> System.out.println(controller.showAll());
            case 6 -> borrowBook();
            case 7 -> returnBook();
            case 8 -> showByGenre();
            case 10 -> fullDescription();
            case 11 -> login();
            case 0 -> { System.out.println("Goodbye."); return true; }
            default -> System.out.println("Unknown option.");
        }
        return false;
    }

    private void findById() {
        System.out.print("Enter book ID: ");
        int id = readInt();
        System.out.println(controller.getById(id));
    }

    private void markRead() {
        System.out.print("ID of the book: ");
        int id = readInt();
        System.out.println(controller.markRead(id));
    }

    private void markNotRead() {
        System.out.print("ID of the book: ");
        int id = readInt();
        System.out.println(controller.markNotRead(id));
    }

    private void addBook() {
        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Genre : ");
        String genre = scanner.nextLine();

        System.out.print("Author ID (or 0 if none): ");
        int a = readInt();
        Integer authorId = (a == 0) ? null : a;

        System.out.println(controller.create(title, genre, authorId));
    }

    private void borrowBook() {
        System.out.print("Book ID: ");
        int bookId = readInt();

        System.out.print("Your name: ");
        String name = scanner.nextLine();

        System.out.println(controller.borrowBook(bookId, name));
    }

    private void returnBook() {
        System.out.print("Book ID: ");
        int bookId = readInt();
        System.out.println(controller.returnBook(bookId));
    }

    private void showByGenre() {
        System.out.print("Genre (Category): ");
        String genre = scanner.nextLine();
        System.out.println(controller.showByGenre(genre));
    }

    private void fullDescription() {
        System.out.print("Book ID: ");
        int bookId = readInt();
        System.out.println(controller.getFullDescription(bookId));
    }

    private void login() {
        while (true) {
            System.out.println("\n=== LOGIN ===");
            System.out.println("Demo users:");
            System.out.println("admin/admin123 | librarian/lib123 | user/user123");
            System.out.print("Username: ");
            String u = scanner.nextLine().trim();
            System.out.print("Password: ");
            String p = scanner.nextLine().trim();

            User user = authService.login(u, p);
            if (user == null) {
                System.out.println("Wrong credentials.\n");
                continue;
            }
            SessionContext.setCurrentUser(user);
            System.out.println("Logged in as " + user.getUsername() + " (" + user.getRole() + ")");
            break;
        }
    }

    private int readInt() {
        while (true) {
            try {
                String s = scanner.nextLine();
                return Integer.parseInt(s.trim());
            } catch (Exception e) {
                System.out.print("Enter a number: ");
            }
        }
    }
}
