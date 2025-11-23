package edu.univ.erp.service;

import edu.univ.erp.data.AuthCommandRunner;
import edu.univ.erp.data.AuthCommandRunner.loginResult;
import edu.univ.erp.util.HashGenerator;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LoginService {

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_INVALID = 1;
    public static final int STATUS_LOCKED = 2;
    public static final int STATUS_ERROR = 3;

    public static class UserDetails {
        public String role;
        public String rollNo;
        public UserDetails(String role, String rollNo) {
            this.role = role;
            this.rollNo = rollNo;
        }
    }

    // The Result "Box" we send back to the UI
    public static class ServiceLoginResult {
        public int status; // Now it's just an int (0, 1, 2, or 3)
        public UserDetails userDetails;
        public long lockoutEndsTimestamp;

        // Constructor for Success
        public ServiceLoginResult(int status, UserDetails userDetails) {
            this.status = status;
            this.userDetails = userDetails;
        }

        // Constructor for standard Errors
        public ServiceLoginResult(int status) {
            this.status = status;
        }
        
        // Constructor specifically for the Timer (Locked state)
        public ServiceLoginResult(int status, long lockoutEndsTimestamp) {
            this.status = status;
            this.lockoutEndsTimestamp = lockoutEndsTimestamp;
        }
    }

    // --- 2. Settings ---
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_TIME_MS = 30 * 1000; // 30 Seconds
    
    private static final Map<String, Integer> failedAttempts = new HashMap<>();
    private static final Map<String, Long> activeLockouts = new HashMap<>();

    // --- 3. The Logic ---
    public ServiceLoginResult attemptLogin(String username, String rawPassword) {
        
        // A. Check Lockout
        if (isLocked(username)) {
            // Return STATUS_LOCKED (which is just the number 2)
            return new ServiceLoginResult(STATUS_LOCKED, activeLockouts.get(username));
        }

        try {
            // B. Fetch User
            loginResult dbUser = AuthCommandRunner.fetchUser(username);

            // C. Check if user exists
            if (dbUser == null) {
                recordFailure(username);
                return new ServiceLoginResult(STATUS_INVALID);
            }

            // D. Check Password
            String inputHash = HashGenerator.makeHash(rawPassword);
            
            if (inputHash.equals(dbUser.hashPass)) {
                // SUCCESS!
                resetFailure(username); 
                return new ServiceLoginResult(
                    STATUS_SUCCESS, 
                    new UserDetails(dbUser.role, dbUser.rollNo)
                );
            } else {
                // WRONG PASSWORD
                recordFailure(username);
                return new ServiceLoginResult(STATUS_INVALID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new ServiceLoginResult(STATUS_ERROR);
        }
    }

    // --- 4. Helper Methods (Same as before) ---
    private boolean isLocked(String username) {
        if (activeLockouts.containsKey(username)) {
            long unlockTime = activeLockouts.get(username);
            if (System.currentTimeMillis() > unlockTime) {
                activeLockouts.remove(username);
                failedAttempts.remove(username);
                return false;
            }
            return true;
        }
        return false;
    }

    private void recordFailure(String username) {
        int attempts = failedAttempts.getOrDefault(username, 0) + 1;
        failedAttempts.put(username, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            activeLockouts.put(username, System.currentTimeMillis() + LOCK_TIME_MS);
        }
    }

    private void resetFailure(String username) {
        failedAttempts.remove(username);
        activeLockouts.remove(username);
    }
}