package edu.univ.erp.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnector {
	//get connection from pool rather than from driver
	private static HikariDataSource AuthDataSource;
	private static HikariDataSource ErpDataSource;

	static {
		try {
			initAuthDatabaseConnectionPool();
			initErpDatabaseConnectionPool();
		} catch (Exception e) {
            System.out.println("Error: Failed to initialize database connection pools.");
			throw new RuntimeException("Error: intialising database connection pools failed.", e);
        }
	}

	private static void initAuthDatabaseConnectionPool() {
		System.out.println("Connecting to auth database connection pool...");
		try {
			// Load database configuration from properties file
			HikariConfig hikariConfig = new HikariConfig("resources/Authdatabase.properties");
			// Initialize connection pool with the loaded configuration
			AuthDataSource = new HikariDataSource(hikariConfig);
			System.out.println("Connected to auth database pool successfully...");
		} catch (Exception e) {
			System.out.println("Failed to initialise auth database connection pool");
			throw new RuntimeException("Auth pool initialistion failed.", e);
		}
	}

	private static void initErpDatabaseConnectionPool() {
		System.out.println("Connecting to erp databse connection pool...");
		try {
			HikariConfig hikariConfig = new HikariConfig("resources/Erpdatabase.properties");
			ErpDataSource = new HikariDataSource(hikariConfig);
			System.out.println("Connected to erp database pool successfully...");		
		} catch (Exception e) {
			System.out.println("Failed to initialise erp database connection pool.");
			throw new RuntimeException("Erp pool initialisation failed.", e);
		}
	}

	public static Connection getAuthConnection() throws SQLException {
        return AuthDataSource.getConnection(); // Borrows from the pool
    }
	public static Connection getErpConnection() throws SQLException {
        return ErpDataSource.getConnection(); 
    }
}
