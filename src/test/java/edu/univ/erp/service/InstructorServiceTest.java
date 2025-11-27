package test.java.edu.univ.erp.service;

import edu.univ.erp.access.modeOps;
import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.data.NotificationCommandRunner;
import edu.univ.erp.domain.Sections;
import edu.univ.erp.domain.Settings;
import edu.univ.erp.service.InstructorService;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructorServiceTest {

    private InstructorService service;

    @BeforeEach
    void setup() {
        service = new InstructorService();
    }

    // ───────────────────────────────────────────────
    // TEST computeStats()
    // ───────────────────────────────────────────────
    @Test
    void testComputeStatsValid() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.instructorStatsHelper("CS101", "A"))
                .thenReturn(new String[]{"A", "B", "C"}); // 3+2+1 = 6 → 6/3 = 2.0

            double result = service.computeStats("CS101", "A");

            assertEquals(2.0, result);
        }
    }

    @Test
    void testComputeStatsNullArray() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.instructorStatsHelper("CS101", "A"))
                .thenReturn(null);

            assertEquals(-1.0, service.computeStats("CS101", "A"));
        }
    }

    @Test
    void testComputeStatsEmptyArray() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.instructorStatsHelper("CS101", "A"))
                .thenReturn(new String[0]);

            assertEquals(-1.0, service.computeStats("CS101", "A"));
        }
    }

    @Test
    void testComputeStatsSQLException() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.instructorStatsHelper("CS101", "A"))
                .thenThrow(new SQLException("DB down"));

            assertThrows(SQLException.class, () ->
                service.computeStats("CS101", "A")
            );
        }
    }

    // ───────────────────────────────────────────────
    // TEST computeAndAssignGrade()
    // ───────────────────────────────────────────────
    @Test
    void testComputeAssignGradeInvalidInteger() throws SQLException {
        String result = service.computeAndAssignGrade("101","1", "CS101", "A", "x", "5", "8");
        assertEquals("Error: Marks must be valid integers.", result);
    }

    @Test
    void testComputeAssignGradeMarksTooHigh() throws SQLException {
        String result = service.computeAndAssignGrade("101","1", "CS101", "A", "11", "5", "8");
        assertEquals("Error: Marks cannot exceed 10.", result);
    }

    @Test
    void testComputeAssignGradeStudentNotEnrolled() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.isStudentEnrolled("1", "CS101", "A"))
                .thenReturn(false);

            String result = service.computeAndAssignGrade("101","1", "CS101", "A", "5", "5", "5");

            assertEquals("Error: Student is not enrolled.", result);
        }
    }

    @Test
    void testComputeAssignGradeNotMySection() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.isStudentEnrolled("1", "CS101", "A"))
                .thenReturn(true);

            mock.when(() -> ErpCommandRunner.isMySection("101", "CS101", "A"))
                .thenReturn(false);

            String result = service.computeAndAssignGrade("101","1", "CS101", "A", "5", "5", "5");

            assertEquals("Error: This is not your section.", result);
        }
    }

    @Test
    void testComputeAssignGradeSuccess() throws SQLException {
        try (
            MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class);
            MockedStatic<NotificationCommandRunner> notifyMock = Mockito.mockStatic(NotificationCommandRunner.class)
        ) {

            mock.when(() -> ErpCommandRunner.isStudentEnrolled("1", "CS101", "A"))
                .thenReturn(true);

            mock.when(() -> ErpCommandRunner.isMySection("101", "CS101", "A"))
                .thenReturn(true);

            mock.when(() -> ErpCommandRunner.addGrade("1", "CS101", "A", "B", 5, 6, 7))
                .thenReturn(true);

            notifyMock.when(() -> NotificationCommandRunner.sendNotification(1, "Grades assigned for course code: CS101"))
                      .thenAnswer(invocation -> null);

            String result = service.computeAndAssignGrade("101","1", "CS101", "A", "5", "6", "7");

            assertEquals("Success", result);
        }
    }

    @Test
    void testComputeAssignGradeSaveFailed() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.isStudentEnrolled("1", "CS101", "A"))
                .thenReturn(true);

            mock.when(() -> ErpCommandRunner.isMySection("101", "CS101", "A"))
                .thenReturn(true);

            mock.when(() -> ErpCommandRunner.addGrade("1", "CS101", "A", "B", 5, 6, 7))
                .thenReturn(false);

            String result = service.computeAndAssignGrade("101","1", "CS101", "A", "5", "6", "7");

            assertEquals("Error: Failed to save the results in grades.", result);
        }
    }

    @Test
    void testComputeAssignGradeNotificationFailsDueToBadId() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.isStudentEnrolled("ABC", "CS101", "A"))
                .thenReturn(true);

            mock.when(() -> ErpCommandRunner.isMySection("101", "CS101", "A"))
                .thenReturn(true);

            mock.when(() -> ErpCommandRunner.addGrade("ABC", "CS101", "A", "B", 5, 6, 7))
                .thenReturn(true);

            // Notification attempt fails due to parseInt("ABC") throwing NumberFormatException, caught internally

            String result = service.computeAndAssignGrade("101","ABC", "CS101", "A", "5", "6", "7");

            assertEquals("Success", result);
        }
    }

    @Test
    void testComputeAssignGradeSQLException() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.isStudentEnrolled("1", "CS101", "A"))
                .thenThrow(new SQLException("DB crash"));

            assertThrows(SQLException.class, () ->
                service.computeAndAssignGrade("101","1", "CS101", "A", "5", "6", "7")
            );
        }
    }

    // ───────────────────────────────────────────────
    // TEST isSystemActive()
    // ───────────────────────────────────────────────
    @Test
    void testIsSystemActiveNoMaintenanceNull() {
        try (MockedStatic<modeOps> mockMode = Mockito.mockStatic(modeOps.class)) {
            mockMode.when(() -> modeOps.getSetting("maintain_mode")).thenReturn(null);
            assertTrue(service.isSystemActive());
        }
    }

    @Test
    void testIsSystemActiveNoMaintenanceFalse() {
        try (MockedStatic<modeOps> mockMode = Mockito.mockStatic(modeOps.class)) {
            Settings settings = Mockito.mock(Settings.class);
            when(settings.isTrue()).thenReturn(false);
            mockMode.when(() -> modeOps.getSetting("maintain_mode")).thenReturn(settings);
            assertTrue(service.isSystemActive());
        }
    }

    @Test
    void testIsSystemActiveMaintenanceTrue() {
        try (MockedStatic<modeOps> mockMode = Mockito.mockStatic(modeOps.class)) {
            Settings settings = Mockito.mock(Settings.class);
            when(settings.isTrue()).thenReturn(true);
            mockMode.when(() -> modeOps.getSetting("maintain_mode")).thenReturn(settings);
            assertFalse(service.isSystemActive());
        }
    }

    // ───────────────────────────────────────────────
    // TEST getMySections()
    // ───────────────────────────────────────────────
    @Test
    void testGetMySectionsValid() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            ArrayList<Sections> list = new ArrayList<>();

            Sections s1 = new Sections();
            s1.setCourseCode("CS101");
            s1.setSection("A");

            Sections s2 = new Sections();
            s2.setCourseCode("CS102");
            s2.setSection("B");

            list.add(s1);
            list.add(s2);

            mock.when(() -> ErpCommandRunner.instructorMySectionsHelper("101"))
                .thenReturn(list);

            ArrayList<Sections> result = service.getMySections("101");

            assertEquals(2, result.size());
            assertEquals("CS101", result.get(0).getCourseCode());        
        }
    }

    @Test
    void testGetMySectionsSQLException() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.instructorMySectionsHelper("101"))
                .thenThrow(new SQLException("DB error"));

            assertThrows(SQLException.class, () ->
                service.getMySections("101")
            );
        }
    }
}