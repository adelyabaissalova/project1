package security;

import repository.interfaces.IUserRepository;

public class AuthService {
    private final IUserRepository users;
    private final SessionContext session;

    public AuthService(IUserRepository users, SessionContext session) {
        this.users = users;
        this.session = session;
    }

    public boolean login(String username, String password) {
        User user = users.login(username, password);
        if (user == null) return false;
        session.setCurrentUser(user);
        return true;
    }

    public void logout() {
        session.clear();
    }
}