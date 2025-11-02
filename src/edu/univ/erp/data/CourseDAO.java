package edu.univ.erp.data;

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
     */
    public List<Course> getCourseCatalog() {
        List<Course> courses = new ArrayList<>();
        // This query just gets the main course list
        String sql = "SELECT code, title, credits FROM courses";
                     
        try (Connection conn = DatabaseConnector.getErpConnection();
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