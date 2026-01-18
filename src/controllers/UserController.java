package controllers;

import models.User;
import repositories.UserRepository;

public class UserController {

    private final UserRepository repository = new UserRepository();

    public void addUser(String username) {
        User user = new User(username);
        boolean created = repository.save(user);

        if (created) {
            System.out.println("User added");
        } else {
            System.out.println("Failed to add user");
        }
    }
}