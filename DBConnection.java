// DBConnection.java
// Handles the single JDBC connection to MySQL.
// All other classes call DBConnection.getConnection() to get the connection.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/expense_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root";

    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASS);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL Driver not found. Put mysql-connector-j.jar in the lib/ folder.");
            }
        }
        return conn;
    }

    public static void init() {
        String baseURL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";
        try (Connection c = DriverManager.getConnection(baseURL, USER, PASS);
             Statement s = c.createStatement()) {
            s.executeUpdate("CREATE DATABASE IF NOT EXISTS expense_db");
        } catch (SQLException e) {
            System.err.println("Could not create database: " + e.getMessage());
        }

        String sql = "CREATE TABLE IF NOT EXISTS expenses (" +
                "  id          INT AUTO_INCREMENT PRIMARY KEY," +
                "  amount      DOUBLE       NOT NULL," +
                "  category    VARCHAR(100) NOT NULL," +
                "  date        DATE         NOT NULL," +
                "  description VARCHAR(255)" +
                ")";
        try (Statement s = getConnection().createStatement()) {
            s.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Could not create table: " + e.getMessage());
        }
    }

    public static void close() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            // ignore on exit
        }
    }
}