package test.java.edu.univ.erp.service;

import edu.univ.erp.data.AuthCommandRunner;
import edu.univ.erp.data.AuthCommandRunner.loginResult;
import edu.univ.erp.service.LoginService;
import edu.univ.erp.service.LoginService.ServiceLoginResult;
import edu.univ.erp.auth.HashGenerator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.Map;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

public class LoginServiceTest {

    private LoginService service;
    
    // Constants copied from LoginService for local testing reference (MAX_ATTEMPTS is private in service)
    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_TIME_MS = 30 * 1000;

    // Reflection Helper Method to safely clear static state
    private void clearServiceState() {
        try {
            var failedAttemptsField = LoginService.class.getDeclaredField("failedAttempts");
            failedAttemptsField.setAccessible(true);
            ((Map<?, ?>) failedAttemptsField.get(null)).clear();

            var activeLockoutsField = LoginService.class.getDeclaredField("activeLockouts");
            activeLockoutsField.setAccessible(true);
            ((Map<?, ?>) activeLockoutsField.get(null)).clear();
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear static state for LoginService", e);
        }
    }
    
    // Reflection Helper Method to safely set a past timestamp for lockout testing
    private void setLockoutTimePast(String username) throws Exception {
        var activeLockoutsField = LoginService.class.getDeclaredField("activeLockouts");
        activeLockoutsField.setAccessible(true);
        var activeLockouts = (Map<String, Long>) activeLockoutsField.get(null);
        
        // Set the lockout time to 31 seconds ago (31000 ms)
        long oldTimestamp = System.currentTimeMillis() - LOCK_TIME_MS - 1000;
        activeLockouts.put(username, oldTimestamp);
    }

    // Reflection Helper Method to safely set failed attempts for lockout testing
    private void setFailedAttemptsCount(String username) throws Exception {
        var failedAttemptsField = LoginService.class.getDeclaredField("failedAttempts");
        failedAttemptsField.setAccessible(true);
        var failedAttempts = (Map<String, Integer>) failedAttemptsField.get(null);
        
        // Set attempts to maximum, so the next call triggers the lock logic check
        failedAttempts.put(username, MAX_ATTEMPTS);
    }


    @BeforeEach
    void setup() {
        service = new LoginService();
        clearServiceState(); // Ensure a clean state before each test
    }

    @AfterEach
    void clear() {
        clearServiceState(); // Clear state after each test
    }

    // 1. Successful Login
    @Test
    void testSuccessfulLogin() throws SQLException {
        try (
            MockedStatic<AuthCommandRunner> mockACR = Mockito.mockStatic(AuthCommandRunner.class);
            MockedStatic<HashGenerator> mockHashGen = Mockito.mockStatic(HashGenerator.class)
        ) {
            loginResult mockUser = new loginResult();
            mockUser.hashPass = "$2a$10$abcdefg";
            mockUser.role = "student";
            mockUser.rollNo = "R123";

            // Mock DAO fetch success
            mockACR.when(() -> AuthCommandRunner.fetchUser("john")).thenReturn(mockUser);
            // Mock Hash verification success
            mockHashGen.when(() -> HashGenerator.verifyHash("password123", mockUser.hashPass)).thenReturn(true);
            ServiceLoginResult result = service.attemptLogin("john", "password123");

            assertEquals(LoginService.STATUS_SUCCESS, result.status);
            assertNotNull(result.userDetails);
            assertEquals("student", result.userDetails.role);
            
            // Verify resetFailure logic was called implicitly
            assertTrue(result.lockoutEndsTimestamp == 0);
        }
    }

    
    // 2. Wrong Password
    @Test
    void testWrongPassword() throws SQLException {
        try (
            MockedStatic<AuthCommandRunner> mockACR = Mockito.mockStatic(AuthCommandRunner.class);
            MockedStatic<HashGenerator> mockHashGen = Mockito.mockStatic(HashGenerator.class)
        ) {
            loginResult mockUser = new loginResult();
            mockUser.hashPass = "$2a$10$abcdefg";

            // Mock DAO fetch success
            mockACR.when(() -> AuthCommandRunner.fetchUser("john")).thenReturn(mockUser);
            // Mock Hash verification failure
            mockHashGen.when(() -> HashGenerator.verifyHash("wrong", mockUser.hashPass)).thenReturn(false);

            ServiceLoginResult result = service.attemptLogin("john", "wrong");

            assertEquals(LoginService.STATUS_INVALID, result.status);
            assertNull(result.userDetails);
        }
    }

    
    // 3. User Not Found
    @Test
    void testUserNotFound() throws SQLException {
        try (MockedStatic<AuthCommandRunner> mockACR = Mockito.mockStatic(AuthCommandRunner.class)) {

            // Mock DAO returns null
            mockACR.when(() -> AuthCommandRunner.fetchUser("ghost")).thenReturn(null);

            ServiceLoginResult result = service.attemptLogin("ghost", "pass");

            assertEquals(LoginService.STATUS_INVALID, result.status);
            assertNull(result.userDetails);
        }
    }

    
    // 4. Locked out before 30 seconds
    @Test
    void testLockoutBeforeExpiry() throws Exception {
        try (
            MockedStatic<AuthCommandRunner> mockACR = Mockito.mockStatic(AuthCommandRunner.class);
            MockedStatic<HashGenerator> mockHashGen = Mockito.mockStatic(HashGenerator.class)
        ) {
            // Arrange: Setup mock to always fail login
            mockACR.when(() -> AuthCommandRunner.fetchUser("john")).thenReturn(null);
            mockHashGen.when(() -> HashGenerator.verifyHash(anyString(), anyString())).thenReturn(false); 

            // 1. Fail 3 times -> lockout (MAX_ATTEMPTS = 3)
            for (int i = 0; i < MAX_ATTEMPTS; i++) {
                service.attemptLogin("john", "x");
            }
            
            // 2. 4th attempt immediately after lock
            ServiceLoginResult lockedResult = service.attemptLogin("john", "x");

            assertEquals(LoginService.STATUS_LOCKED, lockedResult.status);
            
            // Lockout timestamp should be in the future
            assertTrue(lockedResult.lockoutEndsTimestamp > System.currentTimeMillis());
            
            // Verify DAO was only called 3 times (the lock should prevent the 4th call)
            mockACR.verify(() -> AuthCommandRunner.fetchUser(anyString()), times(MAX_ATTEMPTS));
        }
    }

    @Test
    void testLockoutExpires() throws Exception {
        try (
            MockedStatic<AuthCommandRunner> mockACR = Mockito.mockStatic(AuthCommandRunner.class);
            MockedStatic<HashGenerator> mockHashGen = Mockito.mockStatic(HashGenerator.class)
        ) {
            // Arrange: Mock failure for the attempt
            mockACR.when(() -> AuthCommandRunner.fetchUser("john")).thenReturn(null);
            mockHashGen.when(() -> HashGenerator.verifyHash(anyString(), anyString())).thenReturn(false);

            // 1. Manually set state to be locked in the past
            setFailedAttemptsCount("john");
            setLockoutTimePast("john");

            // 2. Now a new attempt should check the lock, reset, and fail normally
            ServiceLoginResult result = service.attemptLogin("john", "x");

            // Should be invalid credentials again (normal failure)
            assertEquals(LoginService.STATUS_INVALID, result.status);
            
            // Lockout timestamp should be 0
            assertEquals(0, result.lockoutEndsTimestamp);

            // Verify DAO was called once (the attempt was not blocked)
            mockACR.verify(() -> AuthCommandRunner.fetchUser(anyString()), times(1));
        }
    }

    
    // 6. Database Error
    @Test
    void testDatabaseError() throws SQLException {
        try (MockedStatic<AuthCommandRunner> mockACR = Mockito.mockStatic(AuthCommandRunner.class)) {

            mockACR.when(() -> AuthCommandRunner.fetchUser("john"))
                    .thenThrow(new SQLException("DB down"));

            ServiceLoginResult result = service.attemptLogin("john", "pass");

            assertEquals(LoginService.STATUS_ERROR, result.status);
            assertNull(result.userDetails);
        }
    }
}