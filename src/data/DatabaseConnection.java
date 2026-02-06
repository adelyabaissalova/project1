package data;

import data.interfaces.IDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection implements IDB {
    private static DatabaseConnection instance;

    private final String url;
    private final String user;
    private final String password;

    private DatabaseConnection(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static synchronized DatabaseConnection getInstance(String url, String user, String password) {
        if (instance == null) instance = new DatabaseConnection(url, user, password);
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
