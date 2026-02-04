package data;

import data.interfaces.IDB;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection implements IDB {
    private static DatabaseConnection instance;

    private final String url;
    private final String user;
    private final String password;
    private final String dbName;

    private Connection connection;

    private DatabaseConnection(String url, String user, String password, String dbName) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.dbName = dbName;
    }

    public static synchronized DatabaseConnection getInstance(
            String url, String user, String password, String dbName
    ) {
        if (instance == null) {
            instance = new DatabaseConnection(url, user, password, dbName);
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url + "/" + dbName, user, password);
            }
            return connection;
        } catch (Exception e) {
            System.out.println("DB connection error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (Exception e) {
            System.out.println("DB close error: " + e.getMessage());
        }
    }
}
