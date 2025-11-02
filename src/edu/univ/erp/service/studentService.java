package edu.univ.erp.service;
//studentService to call the new CourseDAO.
import java.util.List;
import edu.univ.erp.data.CourseDAO;
import edu.univ.erp.domain.Course;

public class studentService {
    
    private CourseDAO courseDAO;

    public studentService() {
        this.courseDAO = new CourseDAO();
    }

    /**
     * Gets the full course catalog for the registration frame.
     */
    public List<Course> getCourseCatalog() {
        // The "brain" just calls the data layer
        return courseDAO.getCourseCatalog();
    }

    // You will add more methods here later, like:
    // public boolean registerForSection(int studentId, int sectionId) { ... }
    // public List<Grade> getMyGrades(int studentId) { ... }
}