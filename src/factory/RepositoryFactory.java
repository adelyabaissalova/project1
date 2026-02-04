package factory;

import data.interfaces.IDB;
import repository.BookRepository;
import repository.interfaces.IBookRepository;

public class RepositoryFactory {
    public static IBookRepository createBookRepository(IDB db) {
        return new BookRepository(db);
    }
}
