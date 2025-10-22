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
    public boolean login(String username, String password) {
        String storedHash = null; // Variable to hold the hash from the DB
        
        // This SQL query finds the user and gets their hashed password
        String sql = "SELECT password_hash FROM users_auth WHERE username = ?";

        // Get a connection from your DatabaseConnector
        // This is a "try-with-resources" block, which automatically closes the connection
        try (Connection conn = DatabaseConnector.getAuthConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the username in the SQL query
            stmt.setString(1, username);

            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                
                // Check if a user was found
                if (rs.next()) {
                    // A user was found, get their stored password hash
                    storedHash = rs.getString("password_hash");
                } else {
                    // No user with that username exists
                    System.out.println("Login Error: No user found.");
                    return false; 
                }
            }
        } catch (SQLException e) {
            System.out.println("Login Error: Database connection failed.");
            e.printStackTrace();
            return false; // Database error
        }

        // --- This is the most important part ---
        
        // If storedHash is null (shouldn't happen if user was found) or empty
        if (storedHash == null || storedHash.isEmpty()) {
            System.out.println("Login Error: Password hash is empty.");
            return false;
        }

        // Now, check if the entered password matches the stored hash
        if (BCrypt.checkpw(password, storedHash)) {
            // Passwords match!
            return true;
        } else {
            // Passwords do not match.
            System.out.println("Login Error: Passwords do not match.");
            return false;
        }
    }
}