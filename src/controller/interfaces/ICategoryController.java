package controller.interfaces;

public interface ICategoryController {

    String create(String name);
    String showAll() throws InterruptedException;
    String getById(int id);
    boolean categoryExists(int id);
    Object getAllCategories();
}
