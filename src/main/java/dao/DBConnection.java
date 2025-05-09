package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection = null;

    static {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Include it in your library path");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        final String URL = "jdbc:mysql://localhost:3306/restaurant_management";
        final String USER = "root";
        final String PASSWORD = ""; // Empty password for XAMPP
        
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connection established successfully");
            } catch (SQLException e) {
                System.err.println("Error establishing database connection: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed successfully");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}