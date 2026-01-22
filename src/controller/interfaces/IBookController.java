package controller.interfaces;

public interface IBookController {
    String create(String title, String genre, Integer authorId);
    String showAll();
    String markRead(int id);
    String markNotRead(int id);

    String getById(int id);
}
