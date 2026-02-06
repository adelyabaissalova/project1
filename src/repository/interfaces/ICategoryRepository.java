package repository.interfaces;

import models.Category;
import java.util.List;

public interface ICategoryRepository {

    boolean addCategory(Category category);

    // Create
    boolean createCategory(Category category);

    // Read
    List<Category> getAllCategories();

    Category getCategoryById(int id);

    // Check existence
    boolean categoryExists(int id);
}
