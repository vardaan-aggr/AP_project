package test.java.edu.univ.erp.service;

import edu.univ.erp.auth.HashGenerator;
import edu.univ.erp.data.AuthCommandRunner;
import edu.univ.erp.data.AuthCommandRunner.loginResult;
import edu.univ.erp.service.LoginService;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.*;

class LoginServiceTest {

    private LoginService service;
    private Field badAttemptsField;
    private Field lockTimeField;

    
    @BeforeEach
    void setup() throws Exception {
        service = new LoginService();

        // Reset static maps using reflection
        badAttemptsField = LoginService.class.getDeclaredField("badAttempts");
        badAttemptsField.setAccessible(true);
        @SuppressWarnings("unchecked")  // Suppress unchecked cast warning here
        var badMap = (HashMap<String, Integer>) badAttemptsField.get(null);
        badMap.clear();

        lockTimeField = LoginService.class.getDeclaredField("lockTime");
        lockTimeField.setAccessible(true);
        @SuppressWarnings("unchecked")  // Suppress unchecked cast warning here
        var lockMap = (HashMap<String, Long>) lockTimeField.get(null);
        lockMap.clear();
    }


    @AfterEach
    void tearDown() throws Exception {
        if (badAttemptsField != null) {
            badAttemptsField.set(null, new HashMap<String, Integer>());
        }
        if (lockTimeField != null) {
            lockTimeField.set(null, new HashMap<String, Long>());
        }
    }

    // ───────────────────────────────────────────────
    // TEST login()
    // ───────────────────────────────────────────────
    @Test
    void testLoginLockedStillLocked() throws Exception {
        String username = "testuser";
        // Set lock time to future
        @SuppressWarnings("unchecked")
        var lockMap = (HashMap<String, Long>) lockTimeField.get(null);
        lockMap.put(username, System.currentTimeMillis() + 10000);

        try (MockedStatic<AuthCommandRunner> mockAuth = Mockito.mockStatic(AuthCommandRunner.class)) {
            mockAuth.when(() -> AuthCommandRunner.fetchUser(username)).thenReturn(null);

            int result = service.login(username, "wrongpass");
            
            assertEquals(LoginService.LOCKED, result);
            assertEquals("", service.loggedInRole);
            assertEquals("", service.loggedInRollNo);
        }
    }
    
    @Test
    void testLoginLockedExpired() throws Exception {
        String username = "testuser";
        // Set lock time to past
        @SuppressWarnings("unchecked")
        var lockMap = (HashMap<String, Long>) lockTimeField.get(null);
        lockMap.put(username, System.currentTimeMillis() - 10000);
        
        loginResult user = new loginResult();
        user.hashPass = "dummyhash";
        user.role = "student";
        user.rollNo = "1";
        
        try (
            MockedStatic<AuthCommandRunner> mockAuth = Mockito.mockStatic(AuthCommandRunner.class);
            MockedStatic<HashGenerator> mockHash = Mockito.mockStatic(HashGenerator.class)
        ) {
            mockAuth.when(() -> AuthCommandRunner.fetchUser(username)).thenReturn(user);
            mockHash.when(() -> HashGenerator.verifyHash("correctpass", user.hashPass)).thenReturn(true);

            int result = service.login(username, "correctpass");

            assertEquals(LoginService.SUCCESS, result);
            assertEquals("student", service.loggedInRole);
            assertEquals("1", service.loggedInRollNo);
        }
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        String username = "nonexistent";
        try (MockedStatic<AuthCommandRunner> mockAuth = Mockito.mockStatic(AuthCommandRunner.class)) {
            mockAuth.when(() -> AuthCommandRunner.fetchUser(username)).thenReturn(null);

            int result = service.login(username, "pass");

            assertEquals(LoginService.INVALID, result);
            assertEquals("", service.loggedInRole);
            assertEquals("", service.loggedInRollNo);
        }
    }
    
    @Test
    void testLoginWrongPasswordFirstAttempt() throws Exception {
        String username = "testuser";
        loginResult user = new loginResult();
        user.hashPass = "dummyhash";
        user.role = "student";
        user.rollNo = "1";

        try (
            MockedStatic<AuthCommandRunner> mockAuth = Mockito.mockStatic(AuthCommandRunner.class);
            MockedStatic<HashGenerator> mockHash = Mockito.mockStatic(HashGenerator.class)
        ) {
            mockAuth.when(() -> AuthCommandRunner.fetchUser(username)).thenReturn(user);
            mockHash.when(() -> HashGenerator.verifyHash("wrongpass", user.hashPass)).thenReturn(false);

            int result = service.login(username, "wrongpass");

            assertEquals(LoginService.INVALID, result);
            assertEquals("", service.loggedInRole);
            assertEquals("", service.loggedInRollNo);
            // Check bad attempts = 1
            @SuppressWarnings("unchecked")
            var badMap = (HashMap<String, Integer>) badAttemptsField.get(null);
            assertEquals(1, badMap.get(username));
        }
    }    

    @Test
    void testLoginWrongPasswordThirdAttemptLocked() throws Exception {
        String username = "testuser";
        loginResult user = new loginResult();
        user.hashPass = "dummyhash";
        user.role = "student";
        user.rollNo = "1";

        try (
            MockedStatic<AuthCommandRunner> mockAuth = Mockito.mockStatic(AuthCommandRunner.class);
            MockedStatic<HashGenerator> mockHash = Mockito.mockStatic(HashGenerator.class)
        ) {
            mockAuth.when(() -> AuthCommandRunner.fetchUser(username)).thenReturn(user);
            mockHash.when(() -> HashGenerator.verifyHash(anyString(), eq(user.hashPass))).thenReturn(false);

            // First two attempts
            service.login(username, "wrong1");
            service.login(username, "wrong2");
            // Third attempt
            int result = service.login(username, "wrong3");

            assertEquals(LoginService.LOCKED, result);
            assertEquals("", service.loggedInRole);
            assertEquals("", service.loggedInRollNo);
            // Check lock time set (with suppressed cast)
            @SuppressWarnings("unchecked")
            var lockMap = (HashMap<String, Long>) lockTimeField.get(null);
            Long unlockTime = lockMap.get(username);
            assertNotNull(unlockTime);
            assertTrue(unlockTime > System.currentTimeMillis());
        }
    }
  

   @Test
    void testLoginSuccess() throws Exception {
        String username = "testuser";
        loginResult user = new loginResult();
        user.hashPass = "dummyhash";
        user.role = "instructor";
        user.rollNo = "101";

        try (
            MockedStatic<AuthCommandRunner> mockAuth = Mockito.mockStatic(AuthCommandRunner.class);
            MockedStatic<HashGenerator> mockHash = Mockito.mockStatic(HashGenerator.class)
        ) {
            mockAuth.when(() -> AuthCommandRunner.fetchUser(username)).thenReturn(user);
            mockHash.when(() -> HashGenerator.verifyHash("correctpass", user.hashPass)).thenReturn(true);

            int result = service.login(username, "correctpass");

            assertEquals(LoginService.SUCCESS, result);
            assertEquals("instructor", service.loggedInRole);
            assertEquals("101", service.loggedInRollNo);
            // Check attempts cleared
            @SuppressWarnings("unchecked")
            var badMap = (HashMap<String, Integer>) badAttemptsField.get(null);
            assertNull(badMap.get(username));
            @SuppressWarnings("unchecked")
            var lockMap = (HashMap<String, Long>) lockTimeField.get(null);
            assertNull(lockMap.get(username));
        }
    }

    @Test
    void testLoginSQLException() throws Exception {
        String username = "testuser";
        try (MockedStatic<AuthCommandRunner> mockAuth = Mockito.mockStatic(AuthCommandRunner.class)) {
            mockAuth.when(() -> AuthCommandRunner.fetchUser(username)).thenThrow(new SQLException("DB error"));

            int result = service.login(username, "pass");

            assertEquals(LoginService.DB_ERROR, result);
            assertEquals("", service.loggedInRole);
            assertEquals("", service.loggedInRollNo);
        }
    }

    // ───────────────────────────────────────────────
    // TEST changeUserPassword()
    // ───────────────────────────────────────────────
    @Test
    void testChangeUserPasswordEmptyFields() {
        String result1 = service.changeUserPassword("", "newpass");
        assertEquals("Error: Fields cannot be empty.", result1);

        String result2 = service.changeUserPassword("user", "");
        assertEquals("Error: Fields cannot be empty.", result2);
    }

    @Test
    void testChangeUserPasswordSuccess() throws Exception {
        String username = "testuser";
        String newHash = "newhashedpass";
        try (
            MockedStatic<HashGenerator> mockHash = Mockito.mockStatic(HashGenerator.class);
            MockedStatic<AuthCommandRunner> mockAuth = Mockito.mockStatic(AuthCommandRunner.class)
        ) {
            mockHash.when(() -> HashGenerator.makeHash("newpass")).thenReturn(newHash);
            mockAuth.when(() -> AuthCommandRunner.updatePasswordHelper(username, newHash)).thenReturn(1);

            String result = service.changeUserPassword(username, "newpass");

            assertEquals("Success", result);
        }
    }

    @Test
    void testChangeUserPasswordUserNotFound() throws Exception {
        String username = "nonexistent";
        String newHash = "newhashedpass";
        try (
            MockedStatic<HashGenerator> mockHash = Mockito.mockStatic(HashGenerator.class);
            MockedStatic<AuthCommandRunner> mockAuth = Mockito.mockStatic(AuthCommandRunner.class)
        ) {
            mockHash.when(() -> HashGenerator.makeHash("newpass")).thenReturn(newHash);
            mockAuth.when(() -> AuthCommandRunner.updatePasswordHelper(username, newHash)).thenReturn(0);

            String result = service.changeUserPassword(username, "newpass");

            assertEquals("Error: User not found (Check username).", result);
        }
    }

    @Test
    void testChangeUserPasswordSQLException() throws Exception {
        String username = "testuser";
        try (
            MockedStatic<HashGenerator> mockHash = Mockito.mockStatic(HashGenerator.class);
            MockedStatic<AuthCommandRunner> mockAuth = Mockito.mockStatic(AuthCommandRunner.class)
        ) {
            mockHash.when(() -> HashGenerator.makeHash("newpass")).thenReturn("hash");
            mockAuth.when(() -> AuthCommandRunner.updatePasswordHelper(username, "hash"))
                    .thenThrow(new SQLException("Update failed"));

            String result = service.changeUserPassword(username, "newpass");

            assertEquals("Database Error: Update failed", result);
        }
    }
}