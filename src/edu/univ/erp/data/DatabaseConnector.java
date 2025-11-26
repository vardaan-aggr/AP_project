package edu.univ.erp.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
    private static HikariDataSource AuthDataSource;
    private static HikariDataSource ErpDataSource;

    static {
        try {
            initAuthDatabaseConnectionPool();
            initErpDatabaseConnectionPool();
        } catch (Exception e) {
            System.out.println("Error: Failed to initialize database connection pools.");
            throw new RuntimeException("Error: initialising database connection pools failed.", e);
        }
    }

    private static void initAuthDatabaseConnectionPool() {
        System.out.println("Connecting to auth database connection pool...");
        try {
            Properties props = new Properties();
            // USE STREAM HERE: Looks for file inside src/resources or bin/resources
            try (InputStream input = DatabaseConnector.class.getClassLoader().getResourceAsStream("resources/Authdatabase.properties")) {
                if (input == null) {
                    throw new RuntimeException("Sorry, unable to find resources/Authdatabase.properties");
                }
                props.load(input);
            }

            HikariConfig hikariConfig = new HikariConfig(props);
            AuthDataSource = new HikariDataSource(hikariConfig);
            System.out.println("Connected to auth database pool successfully...");
        } catch (IOException | RuntimeException e) {
            System.out.println("Failed to initialise auth database connection pool");
            throw new RuntimeException("Auth pool initialisation failed.", e);
        }
    }

    private static void initErpDatabaseConnectionPool() {
        System.out.println("Connecting to erp database connection pool...");
        try {
            Properties props = new Properties();
            // USE STREAM HERE
            try (InputStream input = DatabaseConnector.class.getClassLoader().getResourceAsStream("resources/Erpdatabase.properties")) {
                if (input == null) {
                    throw new RuntimeException("Sorry, unable to find resources/Erpdatabase.properties");
                }
                props.load(input);
            }

            HikariConfig hikariConfig = new HikariConfig(props);
            ErpDataSource = new HikariDataSource(hikariConfig);
            System.out.println("Connected to erp database pool successfully...");
        } catch (IOException | RuntimeException e) {
            System.out.println("Failed to initialise erp database connection pool.");
            throw new RuntimeException("Erp pool initialisation failed.", e);
        }
    }

    public static Connection getAuthConnection() throws SQLException {
        return AuthDataSource.getConnection();
    }

    public static Connection getErpConnection() throws SQLException {
        return ErpDataSource.getConnection();
    }
}