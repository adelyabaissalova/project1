package data;

import data.interfaces.IDB;
import java.sql.*;

public class DatabaseConnection implements IDB {
    private String host;
    private String username;
    private String password;
    private String dbName;
    private Connection connection;

    public DatabaseConnection(String host, String username, String password, String dbName) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.dbName = dbName;
    }

    @Override
    public Connection getConnection() {
        String connectionUrl = host + "/" + dbName;
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(connectionUrl, username, password);
            return connection;

        } catch (Exception e) {
            System.out.println("failed to connect to postgres: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Connection close error: " + ex.getMessage());
            }
        }
    }
}