package test.java.edu.univ.erp.service;

import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.data.NotificationCommandRunner;
import edu.univ.erp.domain.Sections;
import edu.univ.erp.service.InstructorService;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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
    void testComputeStatsValid() throws Exception {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.instructorStatsHelper("CS101", "A"))
                .thenReturn(new String[]{"A", "B", "C"}); // 3+2+1 = 6 → 6/3 = 2.0

            double result = service.computeStats("CS101", "A");

            assertEquals(2.0, result);
        }
    }

    @Test
    void testComputeStatsEmptyArray() throws Exception {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.instructorStatsHelper("CS101", "A"))
                .thenReturn(new String[0]);

            assertEquals(-1.0, service.computeStats("CS101", "A"));
        }
    }

    @Test
    void testComputeStatsSQLException() throws Exception {
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
    void testComputeAssignGradeInvalidInteger() {
        String result = service.computeAndAssignGrade("101", "CS101", "A", "x", "5", "8");
        assertEquals("Error: Marks must be valid integers.", result);
    }

    @Test
    void testComputeAssignGradeMarksTooHigh() {
        String result = service.computeAndAssignGrade("101", "CS101", "A", "11", "5", "8");
        assertEquals("Error: Marks cannot exceed 10.", result);
    }

    @Test
    void testComputeAssignGradeStudentNotEnrolled() {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.isStudentEnrolled("101", "CS101", "A"))
                .thenReturn(false);

            String result = service.computeAndAssignGrade("101", "CS101", "A", "5", "5", "5");

            assertEquals("Error: Student is not enrolled in this course/section.", result);
        }
    }

    @Test
    void testComputeAssignGradeSuccess() throws Exception {
        try (
            MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class);
            MockedStatic<NotificationCommandRunner> notifyMock = Mockito.mockStatic(NotificationCommandRunner.class)
        ) {

            mock.when(() -> ErpCommandRunner.isStudentEnrolled("101", "CS101", "A"))
                .thenReturn(true);

            mock.when(() -> ErpCommandRunner.addGrade("101", "CS101", "A", "B"))
                .thenReturn(true);

            notifyMock.when(() -> NotificationCommandRunner.sendNotification(101, "Grade posted for: CS101"))
                      .thenReturn(true);

            String result = service.computeAndAssignGrade("101", "CS101", "A", "5", "6", "7");

            assertEquals("Success", result);
        }
    }

    @Test
    void testComputeAssignGradeSaveFailed() throws Exception {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.isStudentEnrolled("101", "CS101", "A"))
                .thenReturn(true);

            mock.when(() -> ErpCommandRunner.addGrade("101", "CS101", "A", "B"))
                .thenReturn(false);

            String result = service.computeAndAssignGrade("101", "CS101", "A", "5", "6", "7");

            assertEquals("Error: Failed to save grade to database.", result);
        }
    }

    @Test
    void testComputeAssignGradeNotificationFailsDueToBadId() throws Exception {
        try (
            MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class);
            MockedStatic<NotificationCommandRunner> notifyMock = Mockito.mockStatic(NotificationCommandRunner.class)
        ) {

            mock.when(() -> ErpCommandRunner.isStudentEnrolled("ABC", "CS101", "A"))
                .thenReturn(true);

            mock.when(() -> ErpCommandRunner.addGrade("ABC", "CS101", "A", "B"))
                .thenReturn(true);

            // Notification won't be called because rollNo cannot parse to int

            String result = service.computeAndAssignGrade("ABC", "CS101", "A", "5", "6", "7");

            assertEquals("Success", result);
        }
    }

    @Test
    void testComputeAssignGradeSQLException() throws Exception {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(() -> ErpCommandRunner.isStudentEnrolled("101", "CS101", "A"))
                .thenThrow(new SQLException("DB crash"));

            String result = service.computeAndAssignGrade("101", "CS101", "A", "5", "6", "7");

            assertTrue(result.startsWith("Database Error"));
        }
    }

    // ───────────────────────────────────────────────
    // TEST getMySections()
    // ───────────────────────────────────────────────
    @Test
    void testGetMySections() throws Exception {
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
}
