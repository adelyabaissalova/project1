

import controller.BookController;
import controller.interfaces.IBookController;
import data.DatabaseConnection;
import data.interfaces.IDB;
import repository.BookRepository;
import repository.interfaces.IBookRepository;

public class Main {

    public static void main(String[] args) {

        IDB db = new DatabaseConnection("jdbc:postgresql://localhost:5432", "postgres", "2506", "bookdb");

        IBookRepository repo = new BookRepository(db);
        IBookController controller = new BookController(repo);
        MyApplication app = new MyApplication(controller);

        app.start();
        db.close();
    }
}