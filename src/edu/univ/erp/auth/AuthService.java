package edu.univ.erp.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;
import edu.univ.erp.data.DatabaseConnector; 

public class AuthService {

    /**
     * Attempts to log a user in by checking their username and password.
     *
     * @param username The username entered by the user.
     * @param password The plain-text password entered by the user.
     * @return true if login is successful, false otherwise.
     */
    // ... inside AuthService.java
    public String login(String username, String password) {
        String storedHash = null;
        String userRole = null;
        
        // SQL query now gets both hash and role
        String sql = "SELECT password_hash, role FROM users_auth WHERE username = ?";

        try (Connection conn = DatabaseConnector.getAuthConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    storedHash = rs.getString("password_hash");
                    userRole = rs.getString("role");
                } else {
                    return "Login Error: No user found."; // Return error
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Login Error: Database connection failed."; // Return error
        }

        if (storedHash == null || storedHash.isEmpty()) {
            return "Login Error: Password hash is empty."; // Return error
        }

        // Check password
        if (BCrypt.checkpw(password, storedHash)) {
            return userRole; // <-- SUCCESS! Return the role.
        } else {
            return "Login Error: Passwords do not match."; // Return error
        }
    }

    /**
     * Registers a new user in the database.
     *
     * @param username The username for the new account.
     * @param password The plain-text password for the new account.
     * @param role     The role of the new user (e.g., "student", "admin").
     * @return true if registration is successful, false otherwise (e.g., user already exists).
     */
    public boolean register(String username, String password, String role) {
        // 1. Hash the password using BCrypt
        // The "12" is the work factor (how complex the hash is). 10-12 is standard.
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        // 2. Define the SQL to insert the new user
        // We assume your table has columns: username, password_hash, and role
        String sql = "INSERT INTO users_auth (username, password_hash, role) VALUES (?, ?, ?)";

        // 3. Use try-with-resources to automatically manage the connection
        try (Connection conn = DatabaseConnector.getAuthConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 4. Set the values for the prepared statement
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, role);

            // 5. Execute the insert query
            // executeUpdate() returns the number of rows affected
            int rowsAffected = stmt.executeUpdate();

            // 6. Return true if exactly 1 row was inserted, false otherwise
            return (rowsAffected == 1);

        } catch (SQLException e) {
            // This error will trigger if the username is already taken (due to a UNIQUE constraint)
            // or if there's any other database problem.
            System.out.println("Registration Error: " + e.getMessage());
            // e.printStackTrace(); // Uncomment this for detailed debugging
            return false;
        }
    }


}