package test.java.edu.univ.erp.service;

import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Sections;
import edu.univ.erp.service.CatalogService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CatalogServiceTest {

    private CatalogService catalogService;

    @BeforeEach
    void setUp() {
        catalogService = new CatalogService();
    }

    // 1. getAllCourses Tests


    @Test
    void testGetAllCourses_Success() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            // Arrange: Mock the DAO to return a list of Course objects
            ArrayList<Course> mockCourses = new ArrayList<>(Arrays.asList(
                mock(Course.class), 
                mock(Course.class)
            ));
            
            erpMock.when(ErpCommandRunner::getAllCoursesHelper).thenReturn(mockCourses);
            ArrayList<Course> result = catalogService.getAllCourses();
            assertNotNull(result);
            assertEquals(2, result.size());
            erpMock.verify(ErpCommandRunner::getAllCoursesHelper, times(1));
        }
    }

    @Test
    void testGetAllCourses_SQLExceptionIsPropagated() {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            String errorMessage = "Course DB Access Failed";
            erpMock.when(ErpCommandRunner::getAllCoursesHelper)
                   .thenThrow(new SQLException(errorMessage));

            SQLException thrown = assertThrows(SQLException.class, () -> {
                catalogService.getAllCourses();
            });

            assertTrue(thrown.getMessage().contains(errorMessage));
            erpMock.verify(ErpCommandRunner::getAllCoursesHelper, times(1));
        }
    }

    // 2. getAllSections Tests

    @Test
    void testGetAllSections_Success() throws SQLException {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            // Arrange: Mock the DAO to return a list of Sections objects
            ArrayList<Sections> mockSections = new ArrayList<>(Arrays.asList(
                mock(Sections.class), 
                mock(Sections.class),
                mock(Sections.class)
            ));
            
            erpMock.when(ErpCommandRunner::getAllSectionsHelper).thenReturn(mockSections);
            ArrayList<Sections> result = catalogService.getAllSections();
            assertNotNull(result);
            assertEquals(3, result.size());
            erpMock.verify(ErpCommandRunner::getAllSectionsHelper, times(1));
        }
    }

    @Test
    void testGetAllSections_SQLExceptionIsPropagated() {
        try (MockedStatic<ErpCommandRunner> erpMock = mockStatic(ErpCommandRunner.class)) {
            String errorMessage = "Section DB Access Failed";
            erpMock.when(ErpCommandRunner::getAllSectionsHelper)
                   .thenThrow(new SQLException(errorMessage));
            SQLException thrown = assertThrows(SQLException.class, () -> {
                catalogService.getAllSections();
            });

            assertTrue(thrown.getMessage().contains(errorMessage));
            erpMock.verify(ErpCommandRunner::getAllSectionsHelper, times(1));
        }
    }
}