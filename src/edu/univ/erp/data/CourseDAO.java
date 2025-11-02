package edu.univ.erp.data;
//This class will connect to the erp_db and get the data.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import edu.univ.erp.domain.Course; // Import your new Course object

public class CourseDAO {

    /**
     * Fetches all courses from the erp_db.
     * In a real app, this would get sections, instructors, etc.
     * For now, we'll just get the main course list.
     */
    public List<Course> getCourseCatalog() {
        List<Course> courses = new ArrayList<>();
        // This query joins courses and sections to get all info
        String sql = "SELECT c.code, c.title, c.credits FROM courses c";
                     
        try (Connection conn = DatabaseConnector.getErpConnection(); // <-- Note: getERPConnection()!
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String code = rs.getString("code");
                String title = rs.getString("title");
                int credits = rs.getInt("credits");
                
                Course course = new Course(code, title, credits);
                courses.add(course);
            }

        } catch (SQLException e) {
            System.out.println("Database error fetching course catalog.");
            e.printStackTrace();
        }
        
        return courses; // Return the list of courses
    }
}