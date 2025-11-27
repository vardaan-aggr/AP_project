package test.java.edu.univ.erp.service;

import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Sections;
import edu.univ.erp.service.CatalogService;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CatalogServiceTest {

    private CatalogService service;

    @BeforeEach
    void setup() {
        service = new CatalogService();
    }

    // ───────────────────────────────────────────────
    // TEST getCatalogTableData()
    // ───────────────────────────────────────────────
    @Test
    void testGetCatalogTableDataValid() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            ArrayList<Course> courseList = new ArrayList<>();

            Course c1 = mock(Course.class);
            when(c1.getCourseCode()).thenReturn("CS101");
            when(c1.getTitle()).thenReturn("Intro to CS");
            when(c1.getSection()).thenReturn("A");
            when(c1.getCredits()).thenReturn("3");

            Course c2 = mock(Course.class);
            when(c2.getCourseCode()).thenReturn("CS102");
            when(c2.getTitle()).thenReturn("Data Structures");
            when(c2.getSection()).thenReturn("B");
            when(c2.getCredits()).thenReturn("4");

            courseList.add(c1);
            courseList.add(c2);

            mock.when(ErpCommandRunner::getAllCourses).thenReturn(courseList);

            String[][] result = service.getCatalogTableData();

            assertEquals(2, result.length);
            assertEquals(4, result[0].length);
            assertEquals("CS101", result[0][0]);
            assertEquals("Intro to CS", result[0][1]);
            assertEquals("A", result[0][2]);
            assertEquals("3", result[0][3]);
            assertEquals("CS102", result[1][0]);
            assertEquals("Data Structures", result[1][1]);
            assertEquals("B", result[1][2]);
            assertEquals("4", result[1][3]);
        }
    }

    @Test
    void testGetCatalogTableDataEmptyList() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(ErpCommandRunner::getAllCourses).thenReturn(new ArrayList<>());

            String[][] result = service.getCatalogTableData();

            assertEquals(0, result.length);
        }
    }

    @Test
    void testGetCatalogTableDataNullList() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(ErpCommandRunner::getAllCourses).thenReturn(null);

            String[][] result = service.getCatalogTableData();

            assertEquals(0, result.length);
        }
    }

    @Test
    void testGetCatalogTableDataSQLException() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(ErpCommandRunner::getAllCourses).thenThrow(new SQLException("DB error"));

            assertThrows(SQLException.class, () ->
                service.getCatalogTableData()
            );
        }
    }

    // ───────────────────────────────────────────────
    // TEST getSectionsTableData()
    // ───────────────────────────────────────────────
    @Test
    void testGetSectionsTableDataValid() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            ArrayList<Sections> sectionList = new ArrayList<>();

            Sections s1 = mock(Sections.class);
            when(s1.getCourseCode()).thenReturn("CS101");
            when(s1.getSection()).thenReturn("A");
            when(s1.getRollNo()).thenReturn("101");
            when(s1.getDayTime()).thenReturn("MWF 9-10");
            when(s1.getRoom()).thenReturn("Room 101");
            when(s1.getCapacity()).thenReturn("30"); // Assuming String return
            when(s1.getSemester()).thenReturn("Fall");
            when(s1.getYear()).thenReturn("2025");

            Sections s2 = mock(Sections.class);
            when(s2.getCourseCode()).thenReturn("CS102");
            when(s2.getSection()).thenReturn("B");
            when(s2.getRollNo()).thenReturn("102");
            when(s2.getDayTime()).thenReturn("TTh 2-3");
            when(s2.getRoom()).thenReturn("Room 202");
            when(s2.getCapacity()).thenReturn("25");
            when(s2.getSemester()).thenReturn("Spring");
            when(s2.getYear()).thenReturn("2026");

            sectionList.add(s1);
            sectionList.add(s2);

            mock.when(ErpCommandRunner::getAllSections).thenReturn(sectionList);

            String[][] result = service.getSectionsTableData();

            assertEquals(2, result.length);
            assertEquals(8, result[0].length);
            assertEquals("CS101", result[0][0]);
            assertEquals("A", result[0][1]);
            assertEquals("101", result[0][2]);
            assertEquals("MWF 9-10", result[0][3]);
            assertEquals("Room 101", result[0][4]);
            assertEquals("30", result[0][5]);
            assertEquals("Fall", result[0][6]);
            assertEquals("2025", result[0][7]);
            assertEquals("CS102", result[1][0]);
            assertEquals("B", result[1][1]);
            assertEquals("102", result[1][2]);
            assertEquals("TTh 2-3", result[1][3]);
            assertEquals("Room 202", result[1][4]);
            assertEquals("25", result[1][5]);
            assertEquals("Spring", result[1][6]);
            assertEquals("2026", result[1][7]);
        }
    }

    @Test
    void testGetSectionsTableDataEmptyList() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(ErpCommandRunner::getAllSections).thenReturn(new ArrayList<>());

            String[][] result = service.getSectionsTableData();

            assertEquals(0, result.length);
        }
    }

    @Test
    void testGetSectionsTableDataNullList() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(ErpCommandRunner::getAllSections).thenReturn(null);

            String[][] result = service.getSectionsTableData();

            assertEquals(0, result.length);
        }
    }

    @Test
    void testGetSectionsTableDataSQLException() throws SQLException {
        try (MockedStatic<ErpCommandRunner> mock = Mockito.mockStatic(ErpCommandRunner.class)) {

            mock.when(ErpCommandRunner::getAllSections).thenThrow(new SQLException("DB error"));

            assertThrows(SQLException.class, () ->
                service.getSectionsTableData()
            );
        }
    }
}