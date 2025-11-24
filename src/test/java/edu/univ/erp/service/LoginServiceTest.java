package test.java.edu.univ.erp.service;
import edu.univ.erp.data.AuthCommandRunner;
import edu.univ.erp.data.AuthCommandRunner.loginResult;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mindrot.jbcrypt.BCrypt;
import edu.univ.erp.service.LoginService;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    private LoginService service;

    @BeforeEach
    void setup() {
        service = new LoginService();
    }

    @AfterEach
    void clear() {
        // Clear static map inside LoginService between tests
        try {
            var field = LoginService.class.getDeclaredField("loginAttempts");
            field.setAccessible(true);
            ((java.util.Map<?, ?>) field.get(null)).clear();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 1. Successful Login
    @Test
    void testSuccessfulLogin() {
        try (
                MockedStatic<AuthCommandRunner> mockACR = Mockito.mockStatic(AuthCommandRunner.class);
                MockedStatic<BCrypt> mockBCrypt = Mockito.mockStatic(BCrypt.class)
        ) {
            loginResult mockUser = new loginResult();
            mockUser.hashPass = "$2a$10$abcdefg";

            mockACR.when(() -> AuthCommandRunner.fetchUser("john"))
                    .thenReturn(mockUser);

            mockBCrypt.when(() -> BCrypt.checkpw("password123", mockUser.hashPass))
                    .thenReturn(true);

            LoginService.ServiceLoginResult result = service.attemptLogin("john", "password123");

            assertEquals(LoginService.LoginStatus.SUCCESS, result.status);
            assertNotNull(result.userDetails);
        }
    }

    
    // 2. Wrong Password
    @Test
    void testWrongPassword() {
        try (
                MockedStatic<AuthCommandRunner> mockACR = Mockito.mockStatic(AuthCommandRunner.class);
                MockedStatic<BCrypt> mockBCrypt = Mockito.mockStatic(BCrypt.class)
        ) {
            loginResult mockUser = new loginResult();
            mockUser.hashPass = "$2a$10$abcdefg";

            mockACR.when(() -> AuthCommandRunner.fetchUser("john"))
                    .thenReturn(mockUser);

            mockBCrypt.when(() -> BCrypt.checkpw("wrong", mockUser.hashPass))
                    .thenReturn(false);

            LoginService.ServiceLoginResult result = service.attemptLogin("john", "wrong");

            assertEquals(LoginService.LoginStatus.INVALID_CREDENTIALS, result.status);
            assertNull(result.userDetails);
        }
    }

    
    // 3. User Not Found
    
    @Test
    void testUserNotFound() {
        try (MockedStatic<AuthCommandRunner> mockACR = Mockito.mockStatic(AuthCommandRunner.class)) {

            mockACR.when(() -> AuthCommandRunner.fetchUser("ghost"))
                    .thenReturn(null);

            LoginService.ServiceLoginResult result = service.attemptLogin("ghost", "pass");

            assertEquals(LoginService.LoginStatus.INVALID_CREDENTIALS, result.status);
            assertNull(result.userDetails);
        }
    }

    
    // 4. Locked out before 30 seconds
    
    @Test
    void testLockoutBeforeExpiry() throws Exception {
        try (
                MockedStatic<AuthCommandRunner> mockACR = Mockito.mockStatic(AuthCommandRunner.class);
        ) {
            mockACR.when(() -> AuthCommandRunner.fetchUser("john"))
                    .thenReturn(null);  // failed login

            // Fail 5 times → lockout
            for (int i = 0; i < 5; i++) {
                service.attemptLogin("john", "x");
            }

            LoginService.ServiceLoginResult lockedResult = service.attemptLogin("john", "x");

            assertEquals(LoginService.LoginStatus.ACCOUNT_LOCKED, lockedResult.status);
            assertTrue(lockedResult.lockoutEndsTimestamp > System.currentTimeMillis());
        }
    }

    @Test
    void testLockoutExpires() throws Exception {
        try (
                MockedStatic<AuthCommandRunner> mockACR = Mockito.mockStatic(AuthCommandRunner.class);
        ) {
            mockACR.when(() -> AuthCommandRunner.fetchUser("john"))
                    .thenReturn(null); // fail

            // Fail 5 times → locked out
            for (int i = 0; i < 5; i++) service.attemptLogin("john", "x");

            // Manually modify lastFailedAttemptTimestamp to simulate passage of 31 seconds
            var field = LoginService.class.getDeclaredField("loginAttempts");
            field.setAccessible(true);
            var map = (java.util.Map<String, ?>) field.get(null);
            var info = map.get("john");

            var tsField = info.getClass().getDeclaredField("lastFailedAttemptTimestamp");
            tsField.setAccessible(true);

            long old = System.currentTimeMillis() - 31000;
            tsField.setLong(info, old);

            // Now a new attempt should reset attempts
            mockACR.when(() -> AuthCommandRunner.fetchUser("john")).thenReturn(null);

            LoginService.ServiceLoginResult result = service.attemptLogin("john", "x");

            // Should be invalid credentials again instead of locked
            assertEquals(LoginService.LoginStatus.INVALID_CREDENTIALS, result.status);
        }
    }

    
    // 6. Database Error
    
    @Test
    void testDatabaseError() {
        try (MockedStatic<AuthCommandRunner> mockACR = Mockito.mockStatic(AuthCommandRunner.class)) {

            mockACR.when(() -> AuthCommandRunner.fetchUser("john"))
                    .thenThrow(new SQLException("DB down"));

            LoginService.ServiceLoginResult result = service.attemptLogin("john", "pass");

            assertEquals(LoginService.LoginStatus.DATABASE_ERROR, result.status);
        }
    }
}
