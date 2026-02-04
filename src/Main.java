import controller.BookController;
import controller.interfaces.IBookController;
import data.DatabaseConnection;
import data.interfaces.IDB;
import factory.RepositoryFactory;
import repository.interfaces.IBookRepository;

public class Main {
    public static void main(String[] args) {

        IDB db = DatabaseConnection.getInstance(
                "jdbc:postgresql://localhost:5432",
                "postgres",
                "0000",
                "bookdb"
        );

        IBookRepository repo = RepositoryFactory.createBookRepository(db);
        IBookController controller = new BookController(repo);

        MyApplication app = new MyApplication(controller);
        app.start();

        db.close();
    }
}
