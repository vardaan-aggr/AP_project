package edu.univ.erp.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnector {
	// Create logger for this class
	private static final Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);
	//get connection from pool rather than from driver
	private static HikariDataSource AuthDataSource;
	private static HikariDataSource ErpDataSource;
 
	static {
		try {
			initAuthDatabaseConnectionPool();
			initErpDatabaseConnectionPool();
		} catch (Exception e) {
            logger.error("Error: Failed to initialize database connection pools.", e);
            throw new RuntimeException("Database pool initialization failed", e);
        }
	}

	private static void initAuthDatabaseConnectionPool() {
		logger.info("Connecting to auth database connection pool...");
		// Load database configuration from properties file
		HikariConfig hikariConfig = new HikariConfig("/Authdatabase.properties");
		// Initialize connection pool with the loaded configuration
		AuthDataSource = new HikariDataSource(hikariConfig);
		logger.info("Connected to auth database pool successfully...");
	}
	private static void initErpDatabaseConnectionPool() {
		logger.info("Connecting to erp databse connection pool...");
		HikariConfig hikariConfig = new HikariConfig("/Erpdatabase.properties");
		ErpDataSource = new HikariDataSource(hikariConfig);
		logger.info("Connected to erp database pool successfully...");
	}
	public static Connection getAuthConnection() throws SQLException {
        return AuthDataSource.getConnection(); // Borrows from the pool
    }
	public static Connection getErpConnection() throws SQLException {
        return ErpDataSource.getConnection(); 
    }
}