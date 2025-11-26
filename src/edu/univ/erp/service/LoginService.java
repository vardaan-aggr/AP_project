package edu.univ.erp.service;

import edu.univ.erp.auth.HashGenerator;
import edu.univ.erp.data.AuthCommandRunner;
import edu.univ.erp.data.AuthCommandRunner.loginResult;

import java.sql.SQLException;
import java.util.HashMap;

public class LoginService {

    // Simple status codes
    public static final int SUCCESS = 0;
    public static final int INVALID = 1;
    public static final int LOCKED = 2;
    public static final int DB_ERROR = 3;

    // Simple tracking maps
    private static HashMap<String, Integer> badAttempts = new HashMap<>();
    private static HashMap<String, Long> lockTime = new HashMap<>();

    // Variables to hold user info
    public String loggedInRole = "";
    public String loggedInRollNo = "";

    public int login(String username, String password) {
        
        // 1. Check Lockout
        if (lockTime.containsKey(username)) {
            long unlockAt = lockTime.get(username);
            if (System.currentTimeMillis() < unlockAt) {
                return LOCKED; 
            } else {
                lockTime.remove(username); // Time is up, unlock
                badAttempts.remove(username);
            }
        }

        try {
            // 2. Fetch User
            loginResult user = AuthCommandRunner.fetchUser(username);
            
            if (user == null) {
                return INVALID;
            }

            // 3. Verify Password
            boolean isMatch = HashGenerator.verifyHash(password, user.hashPass);

            if (isMatch) {
                // --- SUCCESS ---
                badAttempts.remove(username);
                lockTime.remove(username);
                
                loggedInRole = user.role;
                loggedInRollNo = user.rollNo;

                // **RESTORED FEATURE: Update DB timestamp**
                AuthCommandRunner.updateLastLogin(user.rollNo);
                
                return SUCCESS;
            } else {
                // --- FAILURE ---
                int count = badAttempts.getOrDefault(username, 0) + 1;
                badAttempts.put(username, count);

                if (count >= 3) {
                    // Lock for 30 seconds
                    lockTime.put(username, System.currentTimeMillis() + (30 * 1000));
                    return LOCKED;
                }
                
                return INVALID;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return DB_ERROR;
        }
    }

    public String changeUserPassword(String username, String newRawPassword) {
        if (username.isEmpty() || newRawPassword.isEmpty()) {
            return "Error: Fields cannot be empty.";
        }

        // 1. Hash the new password
        String newHash = HashGenerator.makeHash(newRawPassword);

        // 2. Update in DB
        try {
            int rows = AuthCommandRunner.updatePasswordHelper(username, newHash);
            if (rows > 0) {
                return "Success";
            } else {
                return "Error: User not found (Check username).";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }
}   