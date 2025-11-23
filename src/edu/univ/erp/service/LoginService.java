package edu.univ.erp.service;

import edu.univ.erp.data.AuthCommandRunner;
import edu.univ.erp.data.AuthCommandRunner.loginResult;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LoginService {

    private static class LoginAttemptInfo {
        int failedAttemptCount = 0;
        long lastFailedAttemptTimestamp = 0; // in milliseconds

        public void increment() {
            failedAttemptCount++;
            lastFailedAttemptTimestamp = System.currentTimeMillis();
        }

        public void reset() {
            failedAttemptCount = 0;
            lastFailedAttemptTimestamp = 0;
        }
    }

    private static final Map<String, LoginAttemptInfo> loginAttempts = new HashMap<>();
    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCKOUT_TIME_MS = 30000; // 30 seconds

    public enum LoginStatus {
        SUCCESS,
        INVALID_CREDENTIALS,
        ACCOUNT_LOCKED,
        DATABASE_ERROR
    }

    public static class ServiceLoginResult {
        public LoginStatus status;
        public loginResult userDetails;
        public long lockoutEndsTimestamp = 0; 

        public ServiceLoginResult(LoginStatus status, loginResult userDetails, long lockoutEndsTimestamp) {
            this.status = status;
            this.userDetails = userDetails;
            this.lockoutEndsTimestamp = lockoutEndsTimestamp;
        }
        
        public ServiceLoginResult(LoginStatus status, loginResult userDetails) {
            this(status, userDetails, 0); 
        }
    }

    public ServiceLoginResult attemptLogin(String username_in, String password_in) {
        
        LoginAttemptInfo attemptInfo = loginAttempts.getOrDefault(username_in, new LoginAttemptInfo());
        loginAttempts.put(username_in, attemptInfo);

        // 1. Check for lockout
        if (attemptInfo.failedAttemptCount >= MAX_ATTEMPTS) {
            long timeElapsed = System.currentTimeMillis() - attemptInfo.lastFailedAttemptTimestamp;
            if (timeElapsed < LOCKOUT_TIME_MS) {
                long endsTimestamp = attemptInfo.lastFailedAttemptTimestamp + LOCKOUT_TIME_MS;
                // Return the calculated end timestamp
                return new ServiceLoginResult(LoginStatus.ACCOUNT_LOCKED, null, endsTimestamp);
            } else {
                attemptInfo.reset();
            }
        }

        loginResult lr = null;
        boolean loginSuccessful = false;

        try {
            lr = AuthCommandRunner.fetchUser(username_in);

            if (lr != null && BCrypt.checkpw(password_in, lr.hashPass)) {
                loginSuccessful = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ServiceLoginResult(LoginStatus.DATABASE_ERROR, null);
        }

        if (loginSuccessful) {
            attemptInfo.reset();
            return new ServiceLoginResult(LoginStatus.SUCCESS, lr);
        } else {
            attemptInfo.increment();
            return new ServiceLoginResult(LoginStatus.INVALID_CREDENTIALS, null);
        }
    }
}