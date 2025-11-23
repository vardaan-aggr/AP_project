package test.java.edu.univ.erp.data;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import edu.univ.erp.data.DatabaseConnector;
public class DatabaseConnectorTest {

    @Test
    void testGetAuthConnection_Success() throws SQLException {
        // Arrange & Act: Attempt to get a connection using try-with-resources for auto-closing
        try (Connection connection = DatabaseConnector.getAuthConnection()) {
            assertNotNull(connection, "Auth connection should not be null.");
            assertTrue(connection.isValid(1), "Auth connection should be valid.");
        }
    }

    @Test
    void testGetErpConnection_Success() throws SQLException {
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            assertNotNull(connection, "ERP connection should not be null.");
            assertTrue(connection.isValid(1), "ERP connection should be valid.");
        }
    }

}