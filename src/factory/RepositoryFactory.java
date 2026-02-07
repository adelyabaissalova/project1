package factory;

import data.interfaces.IDB;
import repository.*;
import repository.interfaces.*;

public class RepositoryFactory {
    private final IDB db;

    public RepositoryFactory(IDB db) {
        this.db = db;
    }

    public IBookRepository createBookRepository() { return new BookRepository(db); }
    public ICategoryRepository createCategoryRepository() { return new CategoryRepository(db); }
    public ILoanRepository createLoanRepository() { return new LoanRepository(db); }
    public IUserRepository createUserRepository() { return new UserRepository(db); }
    public IAdminRepository createAdminRepository() { return new AdminRepository(db); }
}
