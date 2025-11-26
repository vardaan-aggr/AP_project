package test.java.edu.univ.erp.service;

import edu.univ.erp.data.AuthCommandRunner;
import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.data.NotificationCommandRunner;
import edu.univ.erp.domain.Sections;
import edu.univ.erp.service.AdminService;
import edu.univ.erp.domain.Student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdminServiceTest {

    private AdminService adminService;
    private static final String USER = "testuser";
    private static final String HASH = "hashedpass";
    private static final int GENERATED_ROLL_NO = 100;
    private static final String COURSE_CODE = "CS101";
    private static final String SECTION_CODE = "A";
    private static final String INSTRUCTOR_ROLL = "1001";

    @BeforeEach
    void setUp() {
        adminService = new AdminService();
    }
    
    // 1. User Registration Tests (Refined to include Notification mocking)

    @Test
    void testRegisterStudent_Success() throws SQLException {
        try (MockedStatic<AuthCommandRunner> authMock = mockStatic(AuthCommandRunner.class);
            MockedStatic<ErpCommandRunner> erpMock     = mockStatic(ErpCommandRunner.class);
            MockedStatic<NotificationCommandRunner> notifMock = mockStatic(NotificationCommandRunner.class)) {

            authMock.when(() -> AuthCommandRunner.registerUserAuth(
                        anyString(), eq("student"), anyString()))
                    .thenReturn(GENERATED_ROLL_NO);

            erpMock.when(() -> ErpCommandRunner.registerStudentErp(
                        eq(GENERATED_ROLL_NO), anyString(), anyString()))
                    .thenReturn(true);

            String result = adminService.registerStudent(USER, HASH, "CS", "2025");

            assertEquals("Success: Student added with Roll No " + GENERATED_ROLL_NO, result);

            notifMock.verify(() -> NotificationCommandRunner.sendNotification(
                    GENERATED_ROLL_NO, "Student added in ERP!"), times(1));
        }
    }
    
    @Test
    void testRegisterStudent_ErpFailure() throws SQLException {
        try (MockedStatic<AuthCommandRunner> authMock = mockStatic(AuthCommandRunner.class);
             MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class);
             MockedStatic<NotificationCommandRunner> notifMock = mockStatic(NotificationCommandRunner.class)) {

            authMock.when(() -> AuthCommandRunner.registerUserAuth(anyString(), anyString(), anyString()))
                    .thenReturn(GENERATED_ROLL_NO);
            erpMock.when(() -> ErpCommandRunner.registerStudentErp(anyInt(), anyString(), anyString()))
                   .thenReturn(false); // ERP failure

            String result = adminService.registerStudent(USER, HASH, "CS", "2025");

            assertEquals("Created Auth user but failed to create ERP student.", result);
            // Verify notification was NOT sent
            notifMock.verify(() -> NotificationCommandRunner.sendNotification(anyInt(), anyString()), never());
        }
    }

    // 2. unassignInstructor Tests

    @Test
    void testUnassignInstructor_SuccessWithNotification() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class);
             MockedStatic<NotificationCommandRunner> notifMock = mockStatic(NotificationCommandRunner.class)) {
            
            Sections mockSection = mock(Sections.class);
            when(mockSection.getRollNo()).thenReturn(INSTRUCTOR_ROLL);

            erpMock.when(() -> ErpCommandRunner.SectionInfoGetter(COURSE_CODE, SECTION_CODE)).thenReturn(mockSection);
            erpMock.when(() -> ErpCommandRunner.unassignInstructorHelper(COURSE_CODE, SECTION_CODE)).thenReturn(1);

            String result = adminService.unassignInstructor(COURSE_CODE, SECTION_CODE);

            assertEquals("Success", result);
            notifMock.verify(() -> NotificationCommandRunner.sendNotification(
                eq(Integer.parseInt(INSTRUCTOR_ROLL)), 
                eq("You have been unassigned from: " + COURSE_CODE)
            ), times(1));
        }
    }

    @Test
    void testUnassignInstructor_NotFound() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            
            // Arrange: SectionInfoGetter returns null (no section found to get rollNo)
            erpMock.when(() -> ErpCommandRunner.SectionInfoGetter(COURSE_CODE, SECTION_CODE)).thenReturn(null);
            
            // Arrange: Unassign helper returns 0 rows updated
            erpMock.when(() -> ErpCommandRunner.unassignInstructorHelper(COURSE_CODE, SECTION_CODE)).thenReturn(0);

            String result = adminService.unassignInstructor(COURSE_CODE, SECTION_CODE);

            assertEquals("Course or Section not found.", result);
        }
    }

    @Test
    void testUnassignInstructor_SQLException() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            
            // Arrange: Mock DAO to throw SQL exception on info getter
            erpMock.when(() -> ErpCommandRunner.SectionInfoGetter(anyString(), anyString())).thenThrow(new SQLException("Timeout"));

            String result = adminService.unassignInstructor(COURSE_CODE, SECTION_CODE);

            assertEquals("Database Error: Timeout", result);
        }
    }

    // 3. updateSection Tests

    @Test
    void testUpdateSection_SuccessWithNotification() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class);
             MockedStatic<NotificationCommandRunner> notifMock = mockStatic(NotificationCommandRunner.class)) {
            
            // Arrange: Mock successful update (result > 0)
            erpMock.when(() -> ErpCommandRunner.sectionUpdater(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                   .thenReturn(1);
            
            String result = adminService.updateSection(INSTRUCTOR_ROLL, "MW 10-11", "R101", "30", "Fall", "2025", COURSE_CODE, SECTION_CODE);
            assertEquals("Success", result);
            notifMock.verify(() -> NotificationCommandRunner.sendNotification(
                eq(Integer.parseInt(INSTRUCTOR_ROLL)), 
                contains("Update: You are assigned to " + COURSE_CODE)
            ), times(1));
        }
    }

    @Test
    void testUpdateSection_InvalidNumberFormat() {
        String result = adminService.updateSection(INSTRUCTOR_ROLL, "MW 10-11", "R101", "bad_capacity", "Fall", "2025", COURSE_CODE, SECTION_CODE);

        assertEquals("Error: Capacity and Year must be valid numbers.", result); // DAO result -1 check is missed
    }
    

    @Test
    void testUpdateSection_ErpReturnsNegativeOne() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            
            erpMock.when(() -> ErpCommandRunner.sectionUpdater(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                   .thenReturn(-1);
            String result = adminService.updateSection(INSTRUCTOR_ROLL, "MW 10-11", "R101", "30", "Fall", "2025", COURSE_CODE, SECTION_CODE);
            assertEquals("Error: Capacity and Year must be valid numbers.", result);
        }
    }

    // 4. updateCourse Tests
    
    @Test
    void testUpdateCourse_Success() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            
            // Arrange: Mock successful update
            erpMock.when(() -> ErpCommandRunner.courseUpdater(anyString(), eq("3"), anyString(), anyString())).thenReturn(1);

            String result = adminService.updateCourse("New Title", "3", COURSE_CODE, SECTION_CODE);

            assertEquals("Success", result);
        }
    }

    @Test
    void testUpdateCourse_InvalidCredits() {
        // Arrange: Invalid credits causes NumberFormatException inside the service
        String result = adminService.updateCourse("New Title", "three", COURSE_CODE, SECTION_CODE);
        
        assertEquals("Error: Course not found or no changes made.", result);
    }
    //             int result = ErpCommandRunner.courseUpdater(title, credits, courseCode, section);

    // 5. getSectionDetails & getCourseDetails (Simple Data Retrieval)

    @Test
    void testGetSectionDetails_Success() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            Sections mockSection = mock(Sections.class);
            erpMock.when(() -> ErpCommandRunner.SectionInfoGetter(COURSE_CODE, SECTION_CODE)).thenReturn(mockSection);

            Sections result = adminService.getSectionDetails(COURSE_CODE, SECTION_CODE);

            assertNotNull(result);
            erpMock.verify(() -> ErpCommandRunner.SectionInfoGetter(COURSE_CODE, SECTION_CODE), times(1));
        }
    }
    
    @Test
    void testGetCourseDetails_SQLException() {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            erpMock.when(() -> ErpCommandRunner.getCourseHelper(anyString(), anyString())).thenThrow(new SQLException("Read Failed"));
            
            assertThrows(SQLException.class, () -> adminService.getCourseDetails(COURSE_CODE, SECTION_CODE));
        }
    }

    // 6. deleteCourseAndSection Tests


    @Test
    void testDeleteCourseAndSection_SuccessWithNotification() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class);
             MockedStatic<NotificationCommandRunner> notifMock = mockStatic(NotificationCommandRunner.class)) {
            
            Sections mockSection = mock(Sections.class);
            when(mockSection.getRollNo()).thenReturn(INSTRUCTOR_ROLL);
            erpMock.when(() -> ErpCommandRunner.SectionInfoGetter(COURSE_CODE, SECTION_CODE)).thenReturn(mockSection);
            erpMock.when(() -> ErpCommandRunner.deleteSecCourseHelper(COURSE_CODE, SECTION_CODE)).thenReturn(1);
            String result = adminService.deleteCourseAndSection(COURSE_CODE, SECTION_CODE);
            assertEquals("Success: Course and Section deleted successfully.", result);
            notifMock.verify(() -> NotificationCommandRunner.sendNotification(
                eq(Integer.parseInt(INSTRUCTOR_ROLL)), 
                contains("Section cancelled/deleted: " + COURSE_CODE)
            ), times(1));
        }
    }
    
    @Test
    void testDeleteCourseAndSection_PartialSuccess() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            
            erpMock.when(() -> ErpCommandRunner.deleteSecCourseHelper(anyString(), anyString())).thenReturn(3);
            String result = adminService.deleteCourseAndSection(COURSE_CODE, SECTION_CODE);
            assertEquals("Warning: Course deleted, but Section not found in database.", result);
        }
    }
    
    // 7. createSection Tests

    @Test
    void testCreateSection_SuccessWithNotification() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class);
             MockedStatic<NotificationCommandRunner> notifMock = mockStatic(NotificationCommandRunner.class)) {
            
            erpMock.when(() -> ErpCommandRunner.createSectionHelper(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyString(), anyInt()))
                   .thenReturn(1);
            
            String result = adminService.createSection(COURSE_CODE, SECTION_CODE, INSTRUCTOR_ROLL, "MWF 10:00", "R101", "30", "Fall", "2025");

            assertEquals("Success", result);
            notifMock.verify(() -> NotificationCommandRunner.sendNotification(
                eq(Integer.parseInt(INSTRUCTOR_ROLL)), 
                contains("Assigned to new course: " + COURSE_CODE)
            ), times(1));
        }
    }
    
    @Test
    void testCreateSection_InvalidCapacity() {
        // Arrange: Capacity is non-positive
        String result = adminService.createSection(COURSE_CODE, SECTION_CODE, INSTRUCTOR_ROLL, "MWF 10:00", "R101", "0", "Fall", "2025");
        assertEquals("Error: Capacity must be a positive number.", result);
    }
    
    // 8. createCourse Tests

    @Test
    void testCreateCourse_InvalidCreditsFormat() {
        String result = adminService.createCourse(COURSE_CODE, "Title", SECTION_CODE, "three");
        assertEquals("Error: Credits must be a valid integer.", result);
    }
    
    @Test
    void testCreateCourse_Success() throws SQLException {
         try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            erpMock.when(() -> ErpCommandRunner.createCourseHelper(anyString(), anyString(), anyString(), anyInt())).thenReturn(1);
            String result = adminService.createCourse(COURSE_CODE, "Title", SECTION_CODE, "3");
            assertEquals("Success", result);
        }
    }

    // 9. Data Retrieval Tests (getAllStudents, getAllInstructors, getAllAdmins)

    @Test
    void testGetAllStudents_Success() throws SQLException {
        try (MockedStatic<AuthCommandRunner> authMock = mockStatic(AuthCommandRunner.class);
             MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            
            ArrayList<Student> mockStudents = new ArrayList<>(Arrays.asList(mock(Student.class)));
            authMock.when(() -> AuthCommandRunner.searchStudentAuth()).thenReturn(mockStudents);

            adminService.getAllStudents();
            erpMock.verify(() -> ErpCommandRunner.searchStudentErp(mockStudents), times(1));
        }
    }

    @Test
    void testGetAllInstructors_AuthFailure() throws SQLException {
        try (MockedStatic<AuthCommandRunner> authMock = mockStatic(AuthCommandRunner.class)) {
            authMock.when(() -> AuthCommandRunner.searchInstructorAuth()).thenThrow(new SQLException("Auth Down"));
            assertThrows(SQLException.class, () -> adminService.getAllInstructors());
        }
    }
}