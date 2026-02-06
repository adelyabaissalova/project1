package controller;

import controller.interfaces.ICategoryController;
import models.Category;
import repository.interfaces.ICategoryRepository;

import java.util.List;

public class CategoryController implements ICategoryController {

    private final ICategoryRepository repo;

    public CategoryController(ICategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public String create(String name) {
        boolean created = repo.create(new Category(name));
        return created ? "Category added." : "Failed to add category.";
    }

    @Override
    public String showAll() {
        List<Category> categories = repo.getAll();
        if (categories.isEmpty()) return "No categories found.";

        StringBuilder sb = new StringBuilder();
        for (Category c : categories) {
            sb.append(c).append("\n");
        }
        return sb.toString();
    }
}
