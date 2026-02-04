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

    private DatabaseConnection(String baseUrl, String user, String password, String dbName) {
        this.url = baseUrl + "/" + dbName;
        this.user = user;
        this.password = password;
    }

    public static IDB getInstance(String baseUrl, String user, String password, String dbName) {
        if (instance == null) {
            instance = new DatabaseConnection(baseUrl, user, password, dbName);
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("DB connection error: " + e.getMessage());
        }
    }

    @Override
    public void close() {

    }
}