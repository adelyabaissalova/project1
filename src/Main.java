import controller.AdminController;
import controller.BookController;
import controller.CategoryController;
import data.DatabaseConnection;
import data.interfaces.IDB;
import factory.RepositoryFactory;
import repository.interfaces.IAdminRepository;
import repository.interfaces.IBookRepository;
import repository.interfaces.ICategoryRepository;
import repository.interfaces.ILoanRepository;
import repository.interfaces.IUserRepository;
import security.User;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        IDB db = DatabaseConnection.getInstance(
                "jdbc:postgresql://localhost:5432/bookdb",
                "postgres",
                "0000"
        );

        RepositoryFactory factory = new RepositoryFactory(db);

        IUserRepository userRepo = factory.createUserRepository();
        IBookRepository bookRepo = factory.createBookRepository();
        ILoanRepository loanRepo = factory.createLoanRepository();
        ICategoryRepository categoryRepo = factory.createCategoryRepository();
        IAdminRepository adminRepo = factory.createAdminRepository();

        try (Scanner sc = new Scanner(System.in)) {
            MyApplication app = new MyApplication(userRepo);

            User user = app.login(sc);
            if (user == null) {
                System.out.println("Exit.");
                return;
            }

            BookController bookController =
                    new BookController(bookRepo, loanRepo, sc, user);

            CategoryController categoryController =
                    new CategoryController(categoryRepo, sc, user);

            AdminController adminController =
                    new AdminController(adminRepo, sc, user);

            app.start(sc, bookController, categoryController, adminController, user);
        }
    }
}
