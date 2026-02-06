import controller.AdminController;
import controller.BookController;
import controller.CategoryController;
import repository.interfaces.IUserRepository;
import security.User;
import util.Validator;

import java.util.Scanner;

public class MyApplication {
    private final IUserRepository userRepo;

    public MyApplication(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void start(
            Scanner sc,
            BookController bookController,
            CategoryController categoryController,
            AdminController adminController,
            User user
    ) {
        while (true) {
            String roleLabel = user.isAdmin() ? "ADMIN" : (user.isLibrarian() ? "LIBRARIAN" : "USER");
            System.out.println("\n=== ONLINE LIBRARY (" + roleLabel + ") ===");
            System.out.println("Logged as: " + user.getUsername());

            System.out.println("1. List books");
            System.out.println("2. Book details");
            System.out.println("3. Filter by genre");
            System.out.println("4. Filter by category name");
            System.out.println("5. Full book description (JOIN)");
            System.out.println("6. READ books sorted by title");
            System.out.println("7. Borrow book");
            System.out.println("8. Return book");
            System.out.println("9. My loans");
            System.out.println("10. Show all categories");

            if (user.isStaff()) {
                System.out.println("11. Add book");
                System.out.println("12. Edit book");
                System.out.println("13. Set status AVAILABLE/BORROWED");
                System.out.println("14. Who borrowed a book");
                System.out.println("15. Force return book");
                System.out.println("16. Add category");
            }

            if (user.isAdmin()) {
                System.out.println("17. Create user");
                System.out.println("18. Assign role");
            }

            System.out.println("0. Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> bookController.showAllBooks();
                    case "2" -> bookController.showBookById();
                    case "3" -> bookController.showByGenre();
                    case "4" -> bookController.showByCategoryName();
                    case "5" -> bookController.fullDescription();
                    case "6" -> bookController.showReadSortedByTitle();
                    case "7" -> bookController.borrowBook();
                    case "8" -> bookController.returnBook();
                    case "9" -> bookController.myLoans();
                    case "10" -> categoryController.showAllCategories();

                    case "11" -> bookController.addBook();
                    case "12" -> bookController.editBook();
                    case "13" -> bookController.setAvailability();
                    case "14" -> bookController.whoBorrowed();
                    case "15" -> bookController.forceReturn();
                    case "16" -> categoryController.addCategory();

                    case "17" -> adminController.createUser();
                    case "18" -> adminController.assignRole();

                    case "0" -> { return; }
                    default -> System.out.println("Unknown option.");
                }
            } catch (IllegalArgumentException iae) {
                System.out.println("Validation error: " + iae.getMessage());
            } catch (SecurityException se) {
                System.out.println(se.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public User login(Scanner sc) {
        System.out.println("=== LOGIN ===");
        System.out.print("Username: ");
        String u = sc.nextLine().trim();
        System.out.print("Password: ");
        String p = sc.nextLine().trim();

        Validator.requireNotBlank(u, "Username");
        Validator.requireNotBlank(p, "Password");

        User user = userRepo.login(u, p);
        if (user == null) System.out.println("Invalid credentials.");
        return user;
    }
}
