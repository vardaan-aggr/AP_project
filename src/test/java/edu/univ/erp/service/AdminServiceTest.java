package test.java.edu.univ.erp.service;

import edu.univ.erp.data.AuthCommandRunner;
import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.service.AdminService;
import edu.univ.erp.util.modeOps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.sql.SQLException;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    private AdminService adminService;
    private static final String USER = "testuser";
    private static final String HASH = "hashedpass";
    private static final int GENERATED_ROLL_NO = 100;

    @BeforeEach
    void setUp() {
        adminService = new AdminService();
    }

    @Test
    void testRegisterStudent_MaintenanceMode() {
        try (MockedStatic<modeOps> modeOpsMock = mockStatic(modeOps.class)) {
            modeOpsMock.when(modeOps::getMaintainMode).thenReturn("true");
            String result = adminService.registerStudent(USER, HASH, "CS", "2025");
            assertEquals("System is in maintenance mode.", result);
            modeOpsMock.verify(modeOps::getMaintainMode, times(1));
        }
    }

    @Test
    void testRegisterInstructor_MaintenanceMode() {
        try (MockedStatic<modeOps> modeOpsMock = mockStatic(modeOps.class)) {
            modeOpsMock.when(modeOps::getMaintainMode).thenReturn("true");
            String result = adminService.registerInstructor(USER, HASH, "ECE");
            assertEquals("System is in maintenance mode.", result);
        }
    }

    @Test
    void testRegisterStudent_Success() throws SQLException {
        try (MockedStatic<modeOps> modeOpsMock = mockStatic(modeOps.class);
             MockedStatic<AuthCommandRunner> authMock = mockStatic(AuthCommandRunner.class);
             MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            modeOpsMock.when(modeOps::getMaintainMode).thenReturn("false");
            authMock.when(() -> AuthCommandRunner.registerUserAuth(anyString(), eq("student"), anyString()))
                    .thenReturn(GENERATED_ROLL_NO);
            erpMock.when(() -> ErpCommandRunner.registerStudentErp(GENERATED_ROLL_NO, anyString(), anyString()))
                   .thenReturn(true);
            String result = adminService.registerStudent(USER, HASH, "CS", "2025");
            assertEquals("Success: Student added with Roll No " + GENERATED_ROLL_NO, result);
            authMock.verify(() -> AuthCommandRunner.registerUserAuth(USER, "student", HASH), times(1));
            erpMock.verify(() -> ErpCommandRunner.registerStudentErp(GENERATED_ROLL_NO, "CS", "2025"), times(1));
        }
    }

    @Test
    void testRegisterStudent_AuthFailure() throws SQLException {
        try (MockedStatic<modeOps> modeOpsMock = mockStatic(modeOps.class);
             MockedStatic<AuthCommandRunner> authMock = mockStatic(AuthCommandRunner.class);
             MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            modeOpsMock.when(modeOps::getMaintainMode).thenReturn("false");
            authMock.when(() -> AuthCommandRunner.registerUserAuth(anyString(), anyString(), anyString()))
                    .thenReturn(-1); 

            String result = adminService.registerStudent(USER, HASH, "CS", "2025");
            assertEquals("Failed to create student in auth.", result);
            erpMock.verify(() -> ErpCommandRunner.registerStudentErp(anyInt(), anyString(), anyString()), never());
        }
    }

    @Test
    void testRegisterStudent_ErpFailure() throws SQLException {
        try (MockedStatic<modeOps> modeOpsMock = mockStatic(modeOps.class);
             MockedStatic<AuthCommandRunner> authMock = mockStatic(AuthCommandRunner.class);
             MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {

            modeOpsMock.when(modeOps::getMaintainMode).thenReturn("false");
            authMock.when(() -> AuthCommandRunner.registerUserAuth(anyString(), anyString(), anyString()))
                    .thenReturn(GENERATED_ROLL_NO);
            erpMock.when(() -> ErpCommandRunner.registerStudentErp(anyInt(), anyString(), anyString()))
                   .thenReturn(false); // ERP failure

            String result = adminService.registerStudent(USER, HASH, "CS", "2025");

            assertEquals("Created Auth user but failed to create ERP student.", result);
            authMock.verify(() -> AuthCommandRunner.registerUserAuth(anyString(), anyString(), anyString()), times(1));
        }
    }

    @Test
    void testRegisterStudent_SQLException() throws SQLException {
        try (MockedStatic<modeOps> modeOpsMock = mockStatic(modeOps.class);
             MockedStatic<AuthCommandRunner> authMock = mockStatic(AuthCommandRunner.class)) {
            modeOpsMock.when(modeOps::getMaintainMode).thenReturn("false");
            String errorMessage = "Connection refused";
            authMock.when(() -> AuthCommandRunner.registerUserAuth(anyString(), anyString(), anyString()))
                    .thenThrow(new SQLException(errorMessage)); // Simulate DB error
            String result = adminService.registerStudent(USER, HASH, "CS", "2025");

            assertEquals("Database Error: " + errorMessage, result);
        }
    }

    @Test
    void testRegisterInstructor_Success() throws SQLException {
        try (MockedStatic<modeOps> modeOpsMock = mockStatic(modeOps.class);
             MockedStatic<AuthCommandRunner> authMock = mockStatic(AuthCommandRunner.class);
             MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {

            modeOpsMock.when(modeOps::getMaintainMode).thenReturn("false");
            authMock.when(() -> AuthCommandRunner.registerUserAuth(anyString(), eq("instructor"), anyString()))
                    .thenReturn(GENERATED_ROLL_NO);
            erpMock.when(() -> ErpCommandRunner.registerInstructorErp(GENERATED_ROLL_NO, anyString()))
                   .thenReturn(true);

            String result = adminService.registerInstructor(USER, HASH, "ECE");

            assertEquals("Success: Instructor added with Roll No " + GENERATED_ROLL_NO, result);
            erpMock.verify(() -> ErpCommandRunner.registerInstructorErp(GENERATED_ROLL_NO, "ECE"), times(1));
        }
    }
    
    @Test
    void testRegisterInstructor_ErpFailure() throws SQLException {
        try (MockedStatic<modeOps> modeOpsMock = mockStatic(modeOps.class);
             MockedStatic<AuthCommandRunner> authMock = mockStatic(AuthCommandRunner.class);
             MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {

            modeOpsMock.when(modeOps::getMaintainMode).thenReturn("false");
            authMock.when(() -> AuthCommandRunner.registerUserAuth(anyString(), anyString(), anyString()))
                    .thenReturn(GENERATED_ROLL_NO);
            erpMock.when(() -> ErpCommandRunner.registerInstructorErp(anyInt(), anyString()))
                   .thenReturn(false); // ERP failure

            String result = adminService.registerInstructor(USER, HASH, "ECE");

            assertEquals("Created Auth user but failed to create ERP student.", result);
        }
    }

    @Test
    void testRegisterAdmin_Success() throws SQLException {
        try (MockedStatic<modeOps> modeOpsMock = mockStatic(modeOps.class);
             MockedStatic<AuthCommandRunner> authMock = mockStatic(AuthCommandRunner.class)) {
            modeOpsMock.when(modeOps::getMaintainMode).thenReturn("false");
            authMock.when(() -> AuthCommandRunner.registerUserAuth(anyString(), eq("admin"), anyString()))
                    .thenReturn(GENERATED_ROLL_NO);
            String result = adminService.registerAdmin(USER, HASH);
            assertEquals("Success: Admin added with Roll No " + GENERATED_ROLL_NO, result);
            authMock.verify(() -> AuthCommandRunner.registerUserAuth(USER, "admin", HASH), times(1));
        }
    }

    @Test
    void testRegisterAdmin_AuthFailure() throws SQLException {
        try (MockedStatic<modeOps> modeOpsMock = mockStatic(modeOps.class);
             MockedStatic<AuthCommandRunner> authMock = mockStatic(AuthCommandRunner.class)) {
            modeOpsMock.when(modeOps::getMaintainMode).thenReturn("false");
            authMock.when(() -> AuthCommandRunner.registerUserAuth(anyString(), anyString(), anyString()))
                    .thenReturn(0);
            String result = adminService.registerAdmin(USER, HASH);
            assertEquals("Failed to create admin in auth.", result);
        }
    }
}