package factory;

import data.interfaces.IDB;
import repository.BookRepository;
import repository.CategoryRepository;
import repository.interfaces.IBookRepository;
import repository.interfaces.ICategoryRepository;

public class RepositoryFactory {

    public static IBookRepository createBookRepository(IDB db) {
        return new BookRepository(db);
    }

    public static ICategoryRepository createCategoryRepository(IDB db) {
        return new CategoryRepository(db);
    }
}
