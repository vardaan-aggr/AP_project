package test.java.edu.univ.erp.service;

import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.data.NotificationCommandRunner;
import edu.univ.erp.access.modeOps;
import edu.univ.erp.domain.Settings;
import edu.univ.erp.service.StudentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class StudentServiceTest {

    private StudentService studentService;
    private static final String ROLL_NO = "101";
    private static final String COURSE_CODE = "CS101";
    private static final String SECTION = "A";

    @BeforeEach
    void setUp() {
        studentService = new StudentService();
    }
    private Settings makeSettings(String value) {
        Settings s = new Settings();
        s.setValue(value);
        return s;
    }



    // ------------------------------
    // 1. isSystemActive
    // ------------------------------

    @Test
    void testIsSystemActive_TrueWhenNotInMaintenanceMode() {
        try (MockedStatic<modeOps> modeOpsMock = mockStatic(modeOps.class)) {

            modeOpsMock.when(() -> modeOps.getSetting("maintain_mode"))
                    .thenReturn(makeSettings("false")); // active

           
           assertTrue(studentService.isSystemActive());
        }
    }
    
    @Test
    void testIsSystemActive_FalseWhenInMaintenanceMode() {
        try (MockedStatic<modeOps> modeOpsMock = mockStatic(modeOps.class)) {
            modeOpsMock.when(() -> modeOps.getSetting("maintain_mode"))
                    .thenReturn(makeSettings("true")); // maintenance mode
            assertFalse(studentService.isSystemActive());
        }
    }

    // ------------------------------
    // 2. dropCourse
    // ------------------------------


    @Test
    void testDropCourse_DeadlinePassed() {
        LocalDate today    = LocalDate.of(2025, 12, 1);
        LocalDate deadline = LocalDate.of(2025, 11, 30);

        Settings mockSetting = mock(Settings.class);
        when(mockSetting.getValue()).thenReturn("2025-11-30");

        try (var modeOpsMock   = mockStatic(modeOps.class);
            var localDateMock = mockStatic(LocalDate.class);
            var erpMock       = mockStatic(ErpCommandRunner.class)) {

            localDateMock.when(LocalDate::now).thenReturn(today);
            localDateMock.when(() -> LocalDate.parse("2025-11-30", DateTimeFormatter.ISO_LOCAL_DATE))
                        .thenReturn(deadline);

            modeOpsMock.when(() -> modeOps.getSetting("drop_deadline"))
                    .thenReturn(mockSetting);

            int result = studentService.dropCourse("12345", "CS101");

            assertEquals(-2, result);
            erpMock.verify(() -> ErpCommandRunner.studentDropCourseHelper(anyString(), anyString()), never());
        }
    }
    
    @Test
    void testDropCourse_SuccessBeforeDeadline() {
        // Define dates BEFORE any mocking of LocalDate (critical!)
        LocalDate todayDate    = LocalDate.of(2025, 11, 29);  // before deadline
        LocalDate deadlineDate = LocalDate.of(2025, 11, 30);  // deadline is tomorrow

        // Constants (make sure these are defined in your test class)
        String ROLL_NO    = "101";
        String COURSE_CODE = "CS101";

        try (var modeOpsMock   = mockStatic(modeOps.class);
            var localDateMock = mockStatic(LocalDate.class);
            var erpMock       = mockStatic(ErpCommandRunner.class);
            var notifMock     = mockStatic(NotificationCommandRunner.class)) {

            // 1. Mock LocalDate.now()
            localDateMock.when(LocalDate::now).thenReturn(todayDate);

            // 2. Mock LocalDate.parse() — this is REQUIRED because your code uses it!
            localDateMock.when(() -> LocalDate.parse(
                    eq("2025-11-30"),
                    eq(DateTimeFormatter.ISO_LOCAL_DATE)
            )).thenReturn(deadlineDate);

            // 3. Mock the settings (cleanest way: return a mock Settings object)
            Settings mockSettings = mock(Settings.class);
            when(mockSettings.getValue()).thenReturn("2025-11-30");

            modeOpsMock.when(() -> modeOps.getSetting("drop_deadline"))
                    .thenReturn(mockSettings);

            // 4. Mock the DB helper to return 1 row deleted
            erpMock.when(() -> ErpCommandRunner.studentDropCourseHelper(ROLL_NO, COURSE_CODE))
                .thenReturn(1);

            // 5. Execute
            int rows = studentService.dropCourse(ROLL_NO, COURSE_CODE);

            // 6. Assert
            assertEquals(1, rows);

            // 7. Verify notification was sent exactly once
            notifMock.verify(() -> NotificationCommandRunner.sendNotification(
                    eq(101),
                    eq("Dropped course: " + COURSE_CODE)
            ), times(1));

            // Optional: verify DB method was called
            erpMock.verify(() -> ErpCommandRunner.studentDropCourseHelper(ROLL_NO, COURSE_CODE), times(1));
        }
    }
   

    @Test
    void testDropCourse_InvalidDateFormat() {
        try (MockedStatic<modeOps> modeOpsMock = mockStatic(modeOps.class);
            MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            modeOpsMock.when(() -> modeOps.getSetting("drop_deadline"))
                    .thenReturn(makeSettings("30/11/2025"));

            erpMock.when(() -> ErpCommandRunner.studentDropCourseHelper(ROLL_NO, COURSE_CODE))
                   .thenReturn(1);

            int rows = studentService.dropCourse(ROLL_NO, COURSE_CODE);

            assertEquals(1, rows);
        }
    }

    // ------------------------------
    // 3. registerCourse
    // ------------------------------

    @Test
    void testRegisterCourse_SuccessWithNotification() {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class);
             MockedStatic<NotificationCommandRunner> notifMock = mockStatic(NotificationCommandRunner.class)) {

            erpMock.when(() -> ErpCommandRunner.studentRegisterHelper(ROLL_NO, COURSE_CODE, SECTION, "enrolled"))
                   .thenReturn(1);

            int rows = studentService.registerCourse(ROLL_NO, COURSE_CODE, SECTION);

            assertEquals(1, rows);

            notifMock.verify(() ->
                    NotificationCommandRunner.sendNotification(101,
                            "Registered for: " + COURSE_CODE), times(1));
        }
    }

    // ------------------------------
    // 4. grade, timetable, transcript
    // ------------------------------

    @Test
    void testGradeData_Success() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {

            String[][] mockData = {{"Math", "A"}, {"Science", "B"}};

            erpMock.when(() -> ErpCommandRunner.studentGradeHelper(ROLL_NO))
                   .thenReturn(mockData);

            String[][] result = studentService.gradeData(ROLL_NO);

            assertEquals(2, result.length);
        }
    }

    @Test
    void testGradeData_SQLExceptionIsPropagated() {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {

            erpMock.when(() -> ErpCommandRunner.studentGradeHelper(ROLL_NO))
                   .thenThrow(new SQLException("DB Read Error"));

            assertThrows(SQLException.class, () -> studentService.gradeData(ROLL_NO));
        }
    }

    @Test
    void testTimetable_Success() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {

            String[][] mock = {{"Mon 9", "CS101"}, {"Tue 10", "MA101"}};

            erpMock.when(() -> ErpCommandRunner.studentTimeTableHelper(ROLL_NO))
                   .thenReturn(mock);

            String[][] result = studentService.timetable(ROLL_NO);

            assertEquals("CS101", result[0][1]);
        }
    }

    @Test
    void testTimetable_SQLExceptionIsPropagated() {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {

            erpMock.when(() -> ErpCommandRunner.studentTimeTableHelper(ROLL_NO))
                   .thenThrow(new SQLException());

            assertThrows(SQLException.class, () -> studentService.timetable(ROLL_NO));
        }
    }

    
    @Test
    void testGetTranscriptValid() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = mockStatic(ErpCommandRunner.class)) {

            String[] transcript = {"CS101 A 3.0", "CS102 B 2.0"};

            mock.when(() -> ErpCommandRunner.studentTranscriptHelper("1")).thenReturn(transcript);

            String[] result = studentService.getTranscript("1");

            assertArrayEquals(transcript, result);
        }
    }
    @Test
    void testGetTranscript_SQLExceptionIsPropagated() {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {

            erpMock.when(() -> ErpCommandRunner.studentTranscriptHelper(ROLL_NO))
                   .thenThrow(new SQLException());

            assertThrows(SQLException.class,
                    () -> studentService.getTranscript(ROLL_NO));
        }
    }
}

/*
// TEST getTranscript()
    // ───────────────────────────────────────────────
    @Test
    void testGetTranscriptValid() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            String[] transcript = {"CS101 A 3.0", "CS102 B 2.0"};

            mock.when(() -> ErpCommandRunner.studentTranscriptHelper("1")).thenReturn(transcript);

            String[] result = service.getTranscript("1");

            assertArrayEquals(transcript, result);
        }
    }

    @Test
    void testGetTranscriptSQLException() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.studentTranscriptHelper("1")).thenThrow(new SQLException("DB error"));

            assertThrows(SQLException.class, () ->
                service.getTranscript("1")
            );
        }
    }
} */