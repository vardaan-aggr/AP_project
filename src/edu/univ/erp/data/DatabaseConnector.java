package edu.univ.erp.data;

import java.sql.*;

public class DatabaseConnector {

    private static final String DB_USER = "root"; 
    private static final String DB_PASS = "mdo)&&*";
    // --- Connection Strings ---
    // These URLs tell JDBC where to find your databases
    // CORRECT URLS (using the mariadb format)
    private static final String AUTH_DB_URL = "jdbc:mariadb://localhost:3306/auth_db";
    private static final String ERP_DB_URL = "jdbc:mariadb://localhost:3306/erp_db";

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
}
