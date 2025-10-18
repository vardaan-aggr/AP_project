package edu.univ.erp.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    // --- Database Credentials ---
    // Change these to your MySQL root user and password
    private static final String DB_USER = "root"; 
    private static final String DB_PASS = "your_root_password_here"; // <-- IMPORTANT: Change this!

    // --- Connection Strings ---
    // These URLs tell JDBC where to find your databases
    // CORRECT URLS (using the mariadb format)
    private static final String AUTH_DB_URL = "jdbc:mariadb://localhost:3306/auth_db";
    private static final String ERP_DB_URL  = "jdbc:mariadb://localhost:3306/erp_db";

    /**
     * Gets a connection to the Authentication (auth_db) database.
     */
    public static Connection getAuthConnection() throws SQLException {
        try {
            // This line loads the MySQL driver.
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: MySQL JDBC Driver not found!");
            e.printStackTrace();
            return null;
        }
        return DriverManager.getConnection(AUTH_DB_URL, DB_USER, DB_PASS);
    }

    /**
     * Gets a connection to the main ERP (erp_db) database.
     */
    public static Connection getErpConnection() throws SQLException {
         try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: MySQL JDBC Driver not found!");
            e.printStackTrace();
            return null;
        }
        return DriverManager.getConnection(ERP_DB_URL, DB_USER, DB_PASS);
    }

    /*
     * ADD THIS MAIN METHOD TO YOUR DatabaseConnector.java FILE TO TEST IT.
     * Remember to remove it after testing!
     */
    public static void main(String[] args) {
        Connection authConn = null;
        try {
            // 1. Try to connect to the Auth DB
            authConn = getAuthConnection();
            
            // 2. Check if the connection was successful
            if (authConn != null) {
                System.out.println("SUCCESS: Connected to the Auth DB!");
                authConn.close(); // Close the connection
            } else {
                System.out.println("FAILURE: Failed to get Auth DB connection.");
            }
            
        } catch (SQLException e) {
            // This will catch errors like "wrong password" or "database not found"
            System.out.println("FAILURE: SQL Exception occurred!");
            e.printStackTrace();
        }
    }
}