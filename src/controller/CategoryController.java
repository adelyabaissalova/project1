package controller;

import controller.interfaces.ICategoryController;
import models.Category;
import repository.interfaces.ICategoryRepository;
import util.Validator;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryController implements ICategoryController {

    private final ICategoryRepository repo;

    public CategoryController(ICategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public String create(String name) {
        if (name == null || name.isBlank()) {
            return "Category name can't be empty.";
        }

        Category category = new Category(name);
        boolean ok = repo.createCategory(category);
        return ok ? "Category saved successfully." : "Error creating category.";
    }

    @Override
    public String showAll() {
        List<Category> categories = repo.getAllCategories();

        if (categories == null || categories.isEmpty()) {
            return "No categories found.";
        }

        // Sort by ID
        categories = categories.stream()
                .sorted(Comparator.comparingInt(Category::getId))
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        for (Category c : categories) {
            sb.append(c.getId())
                    .append(" | ")
                    .append(c.getName())
                    .append("\n");
        }

        return sb.toString();
    }

    @Override
    public String getById(int id) {
        if (!Validator.isPositiveId(id)) {
            return "ID must be positive.";
        }

        Category category = repo.getCategoryById(id);
        if (category == null) {
            return "Category not found.";
        }

        return "ID: " + category.getId() + "\n" +
                "Name: " + category.getName();
    }

    @Override
    public boolean categoryExists(int id) {
        return repo.categoryExists(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return repo.getAllCategories();
    }
}
