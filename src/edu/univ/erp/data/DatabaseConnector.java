package edu.univ.erp.data;

import java.sql.*;

public class DatabaseConnector {

    private static final String DB_USER = "root"; 
    private static final String DB_PASS = "mdo)&&*"; 
    private static final String AUTH_DB_URL = "jdbc:mariadb://localhost:3306/auth_db";

    public static Connection getAuthConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: MySQL JDBC Driver not found!");
            e.printStackTrace();
            return null;
        }
        return DriverManager.getConnection(AUTH_DB_URL, DB_USER, DB_PASS);
    }

    public static void main(String[] args) {
        Connection authConn = null;
        try {
            // 1. connect to the Auth DB
            authConn = getAuthConnection();
            
            // 2. Check if connection was successful
            if (authConn != null) {
                System.out.println("SUCCESS: Connected to the Auth DB!");
                authConn.close(); // Close the connection
            } else {
                System.out.println("FAILURE: Failed to get Auth DB connection.");
            }
            
        } catch (SQLException e) {
            // catch errors like "wrong password" or "database not found"
            System.out.println("FAILURE: SQL Exception occurred!");
            e.printStackTrace();
        }
    }
}