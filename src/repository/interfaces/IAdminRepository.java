package repository.interfaces;

public interface IAdminRepository {
    boolean createUser(String username, String password);
    boolean assignRole(String username, String roleName);
}
