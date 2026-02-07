package repository.interfaces;

import models.Category;

import java.util.List;

public interface ICategoryRepository {
    boolean createCategory(Category category);
    List<Category> getAllCategories();
    Category getCategoryByName(String name);
}
