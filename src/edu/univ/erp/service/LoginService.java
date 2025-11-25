package edu.univ.erp.service;

import edu.univ.erp.auth.HashGenerator;
import edu.univ.erp.data.AuthCommandRunner;
import edu.univ.erp.data.AuthCommandRunner.loginResult;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LoginService {

    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_INVALID = 1;
    public static final int STATUS_LOCKED = 2;
    public static final int STATUS_ERROR = 3;
    public static final int STATUS_MAINTENANCE = 4; // NEW STATUS

    public static class UserDetails {
        public String role;
        public String rollNo;
        public UserDetails(String role, String rollNo) {
            this.role = role;
            this.rollNo = rollNo;
        }
    }

    public static class ServiceLoginResult {
        public int status; 
        public UserDetails userDetails;
        public long lockoutEndsTimestamp;

        public ServiceLoginResult(int status, UserDetails userDetails) {
            this.status = status;
            this.userDetails = userDetails;
        }

        public ServiceLoginResult(int status) {
            this.status = status;
        }
        
        public ServiceLoginResult(int status, long lockoutEndsTimestamp) {
            this.status = status;
            this.lockoutEndsTimestamp = lockoutEndsTimestamp;
        }
    }

    // --- 2. Settings ---
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_TIME_MS = 30 * 1000; 
    
    private static final Map<String, Integer> failedAttempts = new HashMap<>();
    private static final Map<String, Long> activeLockouts = new HashMap<>();

    // --- 3. The Logic ---
    public ServiceLoginResult attemptLogin(String username, String rawPassword) {
        
        // Check Lockout
        if (isLocked(username)) {
            return new ServiceLoginResult(STATUS_LOCKED, activeLockouts.get(username));
        }

        try {
            // Fetch User
            loginResult dbUser = AuthCommandRunner.fetchUser(username);

            // Check if user exists
            if (dbUser == null) {
                recordFailure(username);
                return new ServiceLoginResult(STATUS_INVALID);
            }

            // D. Check Password
            if (HashGenerator.verifyHash(rawPassword, dbUser.hashPass)) {
                resetFailure(username); 

                AuthCommandRunner.updateLastLogin(dbUser.rollNo);
                
                return new ServiceLoginResult( STATUS_SUCCESS, new UserDetails(dbUser.role, dbUser.rollNo));
            } else {
                recordFailure(username);
                return new ServiceLoginResult(STATUS_INVALID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new ServiceLoginResult(STATUS_ERROR);
        }
    }

    // --- 4. Helper Methods ---
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