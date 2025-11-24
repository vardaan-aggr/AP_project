package test.java.edu.univ.erp.service;

import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.util.modeOps;
import edu.univ.erp.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudentServiceTest {

    private StudentService studentService;
    private static final String ROLL_NO = "R101";
    private static final String COURSE_CODE = "CS101";
    private static final String SECTION = "A";

    @BeforeEach
    void setUp() {
        studentService = new StudentService();
    }

    @Test
    void testIsSystemActive_TrueWhenNotInMaintenanceMode() {
        try (MockedStatic<modeOps> modeOpsMock = mockStatic(modeOps.class)) {
            // Arrange: Maintenance mode is false/off
            modeOpsMock.when(modeOps::getMaintainMode).thenReturn("false");

            // Act & Assert
            assertTrue(studentService.isSystemActive(), "System should be active.");
        }
    }

    @Test
    void testIsSystemActive_FalseWhenInMaintenanceMode() {
        try (MockedStatic<modeOps> modeOpsMock = mockStatic(modeOps.class)) {
            modeOpsMock.when(modeOps::getMaintainMode).thenReturn("true");
            assertFalse(studentService.isSystemActive(), "System should be inactive.");
        }
    }

    @Test
    void testDropCourse_Success() {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            // Arrange: Simulate 1 row deleted
            erpMock.when(() -> ErpCommandRunner.studentDropCourseHelper(ROLL_NO, COURSE_CODE))
                    .thenReturn(1);

            int rowsDeleted = studentService.dropCourse(ROLL_NO, COURSE_CODE);
            assertEquals(1, rowsDeleted);
            erpMock.verify(() -> ErpCommandRunner.studentDropCourseHelper(ROLL_NO, COURSE_CODE), times(1));
        }
    }


    @Test
    void testRegisterCourse_Success() {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            erpMock.when(() -> ErpCommandRunner.studentRegisterHelper(ROLL_NO, COURSE_CODE, SECTION, "enrolled"))
                    .thenReturn(1);

            int rowsInserted = studentService.registerCourse(ROLL_NO, COURSE_CODE, SECTION);

            assertEquals(1, rowsInserted);
            erpMock.verify(() -> ErpCommandRunner.studentRegisterHelper(ROLL_NO, COURSE_CODE, SECTION, "enrolled"), times(1));
        }
    }

    @Test
    void testGradeData_Success() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            // Arrange: Mock the 2D array data returned from the database
            String[][] mockData = {{"Math", "A"}, {"Science", "B"}};
            erpMock.when(() -> ErpCommandRunner.studentGradeHelper(ROLL_NO)).thenReturn(mockData);

            String[][] result = studentService.gradeData(ROLL_NO);
            assertNotNull(result);
            assertEquals(2, result.length);
            assertEquals("Math", result[0][0]);
            erpMock.verify(() -> ErpCommandRunner.studentGradeHelper(ROLL_NO), times(1));
        }
    }

    @Test
    void testGradeData_SQLExceptionIsPropagated() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            erpMock.when(() -> ErpCommandRunner.studentGradeHelper(ROLL_NO))
                    .thenThrow(new SQLException("DB Read Error"));

            SQLException thrown = assertThrows(SQLException.class, () -> {
                studentService.gradeData(ROLL_NO);
            });
            assertTrue(thrown.getMessage().contains("DB Read Error"));
        }
    }

    @Test
    void testTimetable_Success() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            String[][] mockData = {{"Mon 9:00", "CS101"}, {"Tue 10:00", "MA101"}};
            erpMock.when(() -> ErpCommandRunner.studentTimeTableHelper(ROLL_NO)).thenReturn(mockData);

            String[][] result = studentService.timetable(ROLL_NO);
            assertNotNull(result);
            assertEquals("CS101", result[0][1]);
        }
    }

    @Test
    void testTimetable_SQLExceptionIsPropagated() {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            erpMock.when(() -> ErpCommandRunner.studentTimeTableHelper(ROLL_NO))
                    .thenThrow(new SQLException("Timeout Error"));
            SQLException thrown = assertThrows(SQLException.class, () -> {
                studentService.timetable(ROLL_NO);
            });

            assertTrue(thrown.getMessage().contains("Timeout Error"));
        }
    }
  
    @Test
    void testGetTranscript_SQLExceptionIsPropagated() {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            erpMock.when(() -> ErpCommandRunner.studentTranscriptHelper(ROLL_NO))
                    .thenThrow(new SQLException("Transcript fetch failed"));

            SQLException thrown = assertThrows(SQLException.class, () -> {
                studentService.getTranscript(ROLL_NO);
            });

            assertTrue(thrown.getMessage().contains("Transcript fetch failed"));
        }
    }
}