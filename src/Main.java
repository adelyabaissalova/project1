import controller.BookController;
import controller.CategoryController;
import controller.interfaces.IBookController;
import controller.interfaces.ICategoryController;
import data.DatabaseConnection;
import data.interfaces.IDB;
import factory.RepositoryFactory;
import repository.interfaces.IBookRepository;
import repository.interfaces.ICategoryRepository;

public class Main {
    public static void main(String[] args) {

        IDB db = DatabaseConnection.getInstance(
                "jdbc:postgresql://localhost:5432",
                "postgres",
                "0000",
                "bookdb"
        );
        if (db == null) {
            System.out.println("DB is null! Fix DatabaseConnection.");
            return;
        } else {
            System.out.println("DB object created successfully: " + db);
        }

        IBookRepository bookRepo = RepositoryFactory.createBookRepository(db);
        ICategoryRepository categoryRepo = RepositoryFactory.createCategoryRepository(db);


        IBookController bookController = new BookController(bookRepo);
        ICategoryController categoryController = new CategoryController(categoryRepo);


        MyApplication app = new MyApplication(bookController, categoryController);
        app.start();


        db.close();
    }
}
