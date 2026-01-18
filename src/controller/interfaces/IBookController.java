package controller.interfaces;

public interface IBookController {
    String create(String title, String genre);

    String showAll();
    String markRead(int id);
    String markNotRead(int id);
}
