package controller;

import models.Category;
import repository.interfaces.ICategoryRepository;
import security.User;
import util.Validator;

import java.util.Scanner;

public class CategoryController {
    private final ICategoryRepository categories;
    private final Scanner sc;
    private final User currentUser;

    public CategoryController(ICategoryRepository categories, Scanner sc, User currentUser) {
        this.categories = categories;
        this.sc = sc;
        this.currentUser = currentUser;
    }

    private void requireStaff() {
        if (currentUser == null || !currentUser.isStaff()) throw new SecurityException("Access denied: LIBRARIAN/ADMIN only");
    }

    public void addCategory() {
        requireStaff();
        System.out.print("Category name: ");
        String name = sc.nextLine();
        Validator.requireNotBlank(name, "Category name");
        boolean ok = categories.createCategory(new Category(name));
        System.out.println(ok ? "Added." : "Failed.");
    }

    public void showAllCategories() {
        var list = categories.getAllCategories();
        if (list.isEmpty()) { System.out.println("No categories."); return; }
        list.forEach(c -> System.out.printf("%d) %s%n", c.getId(), c.getName()));
    }
}
