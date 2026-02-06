package repository.interfaces;

import security.User;

public interface IUserRepository {
    User login(String username, String password);
}
