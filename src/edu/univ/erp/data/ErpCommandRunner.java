package edu.univ.erp.data;  

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Grades;
import edu.univ.erp.domain.Instructor;
import edu.univ.erp.domain.Sections;
import edu.univ.erp.domain.Student;

public class ErpCommandRunner {
    
    public static int studentRegisterHelper(String rollNo_in, String courseCode_in, String section_in, String status_in) {
        int resultCode = studentEnrollmentsCapacityHandler(courseCode_in, section_in);
        if (resultCode > 0) {
            try (Connection connection = DatabaseConnector.getErpConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("""
                            INSERT INTO enrollments(roll_no, course_code , section, status) VALUES (?, ?, ?, ?);
                        """)) {
                    statement.setString(1, rollNo_in);
                    statement.setString(2, courseCode_in);
                    statement.setString(3, section_in);
                    statement.setString(4, status_in);
                    int rowsInsreted = statement.executeUpdate();
                    return rowsInsreted;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                resultCode = - 4; // Database error: Couldn't insert in enrollments database.
            }
        } 
        return resultCode;  // success
    }
    private static int studentEnrollmentsCapacityHandler(String courseCode, String section) {
        int capacity = -1;
        int enrolled = -1;

        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                    SELECT capacity FROM sections WHERE course_code = ? AND section = ?;
            """)) {
                statement.setString(1, courseCode);
                statement.setString(2, section);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        capacity = rs.getInt("capacity");
                    } else {
                        JOptionPane.showMessageDialog(null, "Section not found.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return -1; // -1 Section not found
                    }
                }
            }

            // 2. Get count of enrolled students
            try (PreparedStatement statement = connection.prepareStatement("""
                    SELECT COUNT(*) AS enrolled_count FROM enrollments WHERE course_code = ? AND section = ? AND status = 'enrolled';
            """)) {

                statement.setString(1, courseCode);
                statement.setString(2, section);

                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        enrolled = rs.getInt("enrolled_count");
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -4;  // Database error.
        }

        if (enrolled < capacity) {
            return 1; // 1 success
        } else {
            JOptionPane.showMessageDialog(null, "Course Capacity Full.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return -11; // -11 code for overflow enrollments 
        }
    }

    
    public static int studentDropCourseHelper(String rollNo, String courseCode) {
        int rowsDeleted = -1;
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        delete from enrollments where roll_no = ? and course_code = ?;
                    """)) {
                statement.setString(1, rollNo);
                statement.setString(2, courseCode);
                rowsDeleted = statement.executeUpdate();
            }
            try (PreparedStatement statement = connection.prepareStatement("""
                        delete from grades where roll_no = ? and course_code = ?;
                    """)) {
                statement.setString(1, rollNo);
                statement.setString(2, courseCode);
                statement.executeUpdate();
                return rowsDeleted;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
    
    public static String[][] studentGradeHelper(String rollNo) throws SQLException {
        ArrayList<Grades> gradeList = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select course_code, grade FROM grades WHERE roll_no = ?;
                    """)) {
                statement.setString(1, rollNo);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Grades g = new Grades();
                        g.setCourseCode(resultSet.getString("course_code"));
                        g.setGrade(resultSet.getString("grade"));
                        gradeList.add(g);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        String[][] strArr = new String[gradeList.size()][2];
        for (int i = 0; i < gradeList.size(); i++) {
            Grades g = gradeList.get(i);
            strArr[i][0] = g.getCourseCode();
            strArr[i][1] = g.getGrade();
        }
        return strArr;
    }
    
    public static String[][] studentTimeTableHelper(String rollNo) throws SQLException {
        ArrayList<Sections> sectionList = new ArrayList<>();
        String query = """
            SELECT e.course_code, s.day_time, s.room 
            FROM enrollments e
            JOIN sections s ON e.course_code = s.course_code AND e.section = s.section
            WHERE e.roll_no = ? AND e.status = ?;
        """;
        try (Connection connection = DatabaseConnector.getErpConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, rollNo);
            statement.setString(2, "enrolled");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Sections s = new Sections();
                    s.setCourseCode(resultSet.getString("course_code"));
                    s.setDayTime(resultSet.getString("day_time"));
                    s.setRoom(resultSet.getString("room"));
                    sectionList.add(s);
                }
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }

        String[][] resultArr = new String[sectionList.size()][3];
        for (int i = 0; i < sectionList.size(); i++) {
            Sections s = sectionList.get(i);
            resultArr[i][0] = s.getCourseCode();
            resultArr[i][1] = s.getDayTime();
            resultArr[i][2] = s.getRoom();
        }
        return resultArr;
    }
    
    public static ArrayList<String[]> studentTrrowsUpdatedcriptHelper(String rollNo) throws SQLException {
        ArrayList<String[]> gradeList = new ArrayList<>();
        String query = """
            SELECT e.course_code, e.section, g.grade 
            FROM enrollments e
            LEFT JOIN grades g ON e.course_code = g.course_code AND e.roll_no = g.roll_no
            WHERE e.roll_no = ?;
        """;
        try (Connection connection = DatabaseConnector.getErpConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, rollNo);
            try (ResultSet resultSet = statement.executeQuery()) {
                boolean hasEnrollments = false;
                while (resultSet.next()) {
                    hasEnrollments = true;
                    String courseCode = resultSet.getString("course_code");
                    String section = resultSet.getString("section");
                    String grade = resultSet.getString("grade");
                    if (grade == null) {
                        grade = "N/A"; 
                    }
                    gradeList.add(new String[]{courseCode, section, grade});
                }
                if (!hasEnrollments) {
                    System.out.println("\t (no courses found for trrowsUpdatedcript)");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException(ex); 
        }

        return gradeList;
    }

    public static ArrayList<Grades> studentTranscriptHelper(String rollNo) throws SQLException {
        ArrayList<Grades> gradeList = new ArrayList<>();

        // Corrected Query: Selects all courses associated with the student, regardless of current status
        String query = """
            SELECT e.course_code, e.section, g.grade 
            FROM enrollments e
            LEFT JOIN grades g ON e.course_code = g.course_code AND e.roll_no = g.roll_no
            WHERE e.roll_no = ?;
        """;

        try (Connection connection = DatabaseConnector.getErpConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, rollNo);

            try (ResultSet resultSet = statement.executeQuery()) {
                // boolean hasEnrollments = false;
                while (resultSet.next()) {
                    Grades g = new Grades();
                    // hasEnrollments = true;
                    g.setCourseCode(resultSet.getString("course_code"));
                    g.setSection(resultSet.getString("section"));
                    g.setGrade(resultSet.getString("grade"));
                    
                    if (g.getGrade() == null) {
                        g.setGrade("N/A"); 
                    }
                    gradeList.add(g);
                }
                // if (!hasEnrollments) {
                //     System.out.println("\t (no courses found for transcript)");
                // }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException(ex); 
        }

        return gradeList;
    }

    public static ArrayList<Sections> instructorMySectionsHelper(String roll_no) throws SQLException {
        ArrayList<Sections> sectionList = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select course_code, section FROM sections WHERE roll_no = ?; 
                    """)) {
                statement.setString(1, roll_no); 

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Sections s = new Sections();
                        s.setCourseCode(resultSet.getString("course_code"));
                        s.setSection(resultSet.getString("section"));
                        sectionList.add(s);
                    } 
                }
            } 
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        return sectionList;
    }

    public static boolean isStudentEnrolled(String rollNo, String courseCode, String section) throws SQLException {
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "SELECT 1 FROM enrollments WHERE roll_no = ? AND course_code = ? AND section = ? AND status = 'enrolled'")) {
                stmt.setString(1, rollNo);
                stmt.setString(2, courseCode);
                stmt.setString(3, section);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next(); 
                }
            }
        }
    }

    public static boolean addGrade(String rollNo, String courseCode, String section, String grade) throws SQLException {
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO grades (roll_no, course_code, section, grade) VALUES (?, ?, ?, ?)")) {
                stmt.setString(1, rollNo);
                stmt.setString(2, courseCode);
                stmt.setString(3, section);
                stmt.setString(4, grade);
                return stmt.executeUpdate() > 0;
            }
        }
    }

    public static String[] instructorStatsHelper(String courseCode, String section) throws SQLException {
        ArrayList<String> arrList = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select grade FROM grades WHERE course_code = ? AND section = ?; 
                    """)) {
                statement.setString(1, courseCode);
                statement.setString(2, section);
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String grade = resultSet.getString("grade");
                        if(grade != null) {
                            arrList.add(grade);
                        }
                    } 
                    if (empty) {
                        System.out.println("\t (no grades found)");
                        JOptionPane.showMessageDialog(null, "No grades found for this course/section.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return null;
                    }
                }
            } 
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        String[] strArr = new String[arrList.size()];
        arrList.toArray(strArr);
        return strArr;
    }

    public static boolean registerStudentErp(int rollNo, String program, String year) throws SQLException {
        String query = "INSERT INTO students (roll_no, program, year) VALUES (?, ?, ?)";
        
        try (Connection connection = DatabaseConnector.getErpConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, rollNo);
            statement.setString(2, program);
            statement.setString(3, year);
            
            if (statement.executeUpdate() > 0) { return true; }
            else { return false; }
        }
    }

    public static boolean registerInstructorErp(int rollNo, String department) throws SQLException {
        String query = "INSERT INTO instructors (roll_no, department) VALUES (?, ?)";
        
        try (Connection connection = DatabaseConnector.getErpConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, rollNo);
            statement.setString(2, department);
            
            return statement.executeUpdate() > 0;
        }
    }

    public static int createSectionHelper(String course_code, String section, String roll_no, String day_time, String room, int capacity, String semester, int year) throws SQLException {
        int rowsInserted = -1;
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        INSERT INTO sections(course_code, section, roll_no, day_time, room, capacity, semester, year) 
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                    """)) {
                statement.setString(1, course_code);
                statement.setString(2, section);
                statement.setString(3, roll_no);
                statement.setString(4, day_time);
                statement.setString(5, room);
                statement.setInt(6, capacity); // Use setInt
                statement.setString(7, semester);
                statement.setInt(8, year);     // Use setInt

                rowsInserted = statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        return rowsInserted;
    }

    public static int deleteSecCourseHelper(String courseCode , String section) {
        int sectionRowsAffected = 0;
        int courseRowsAffected = 0;
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            // 1. Delete from Sections table
            try (PreparedStatement stmtSection = connection.prepareStatement(
                    "DELETE FROM sections WHERE course_code = ? AND section = ?")) {
                stmtSection.setString(1, courseCode);
                stmtSection.setString(2, section);
                sectionRowsAffected = stmtSection.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return -1;
            }

            // 2. Delete from Courses table
            try (PreparedStatement stmtCourse = connection.prepareStatement(
                    "DELETE FROM courses WHERE course_code = ? AND section = ?")) {
                stmtCourse.setString(1, courseCode);
                stmtCourse.setString(2, section);
                courseRowsAffected = stmtCourse.executeUpdate();
                if (sectionRowsAffected == 0 && courseRowsAffected > 0) return 2;
                else if (sectionRowsAffected > 0 && courseRowsAffected == 0) return 3;
                else if (sectionRowsAffected == 0 && courseRowsAffected == 0) return 4;
            } catch (SQLException e) { 
                e.printStackTrace();
                return -2;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -3;
        }
        return 1;
    }

    public static String[] courseSectionEditHelper(String courseCode, String section) throws SQLException {
        String[] arr = new String[4];
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                Select * FROM courses WHERE course_code = ? AND section = ?; 
            """)) {
            statement.setString(1, courseCode); 
            statement.setString(2, section); 

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                    
                        String course_code = resultSet.getString("course_code");
                        String title = resultSet.getString("title");
                        String section2 = resultSet.getString("section");
                        String credits = resultSet.getString("credits");

                        arr[0] = course_code;
                        arr[1] = title;   
                        arr[2] = section2;
                        arr[3] = credits;
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException();   
        }
        return arr;
    }

    public static int courseUpdater(String title, String credits, String course_code, String section) throws SQLException {
        int rowsUpdated = -1;
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    """
                    UPDATE courses 
                    SET title = ?, credits = ?
                    WHERE course_code = ? AND section = ?;
                    """)) {
                
                statement.setString(1, title); 
                statement.setString(2, credits); 
                statement.setString(3, course_code); 
                statement.setString(4, section); 
                rowsUpdated = statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return rowsUpdated;
    }

    public static Sections SectionInfoGetter(String course_code, String section) throws SQLException {
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                Select * FROM sections WHERE course_code = ? AND section = ?; 
            """)) {
            statement.setString(1, course_code); 
            statement.setString(2, section); 

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Sections s = new Sections();
                        s.setCourseCode(resultSet.getString("course_code"));
                        s.setSection(resultSet.getString("section"));
                        s.setRollNo(resultSet.getString("roll_no"));
                        s.setDayTime(resultSet.getString("day_time"));
                        s.setRoom(resultSet.getString("room"));
                        s.setCapacity(resultSet.getString("capacity"));
                        s.setSemester(resultSet.getString("semester"));
                        s.setYear(resultSet.getString("year"));
                        return s;
                    }
                }
            }
        }
        return null;
    }

    public static int sectionUpdater(String roll_no, String day_time, String room, String capacity, String semester, String year, String course_code, String section) throws SQLException {
        int rowsUpdated = 0;
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            String sql = """
                UPDATE sections 
                SET roll_no = ?, day_time = ?, room = ?, capacity = ?, semester = ?, year = ?
                WHERE course_code = ? AND section = ?;
                """;
            
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, roll_no); 
                statement.setString(2, day_time); 
                statement.setString(3, room); 
                statement.setInt(4, Integer.parseInt(capacity)); 
                statement.setString(5, semester); 
                statement.setInt(6, Integer.parseInt(year));
                statement.setString(7, course_code); 
                statement.setString(8, section); 
                
                rowsUpdated = statement.executeUpdate();
            }
        } catch (NumberFormatException e) {
            // Handle case where capacity/year are not numbers
            System.out.println("Error: Capacity and Year must be numbers.");
            return -1; 
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return rowsUpdated;
    }

    public static ArrayList<Student> searchStudentErp(ArrayList<Student> stdList) throws SQLException {
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            for (Student s : stdList) {
                try (PreparedStatement statement = connection.prepareStatement("""
                            Select program, year FROM students where roll_no = ?;
                        """)) {
                    statement.setString(1, s.getRollNo());
                    try (ResultSet resultSet = statement.executeQuery()) {
                        // boolean empty = true;
                        if (resultSet.next()) {
                            // empty = false;
                            s.setProgram(resultSet.getString("program"));
                            s.setYear(resultSet.getString("year"));
                        }
                        else {
                        System.out.println("Warning: Missing ERP details for roll: " + s.getRollNo());
                        }
                    }
                }
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: Couldn't fetch student details: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            throw new SQLException(ex);
        }
        return stdList;
    }

    public static ArrayList<Instructor> searchInstructorErp(ArrayList<Instructor> insList) throws SQLException {
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            for (Instructor i : insList) {
                try (PreparedStatement statement = connection.prepareStatement("""
                            Select department FROM instructors where roll_no = ?;
                        """)) {
                        statement.setString(1, i.getRollNo());
                    try (ResultSet resultSet = statement.executeQuery()) {
                        // boolean empty = true;
                        if (resultSet.next()) {
                            // empty = false;
                            i.setDepartment(resultSet.getString("department"));
                        } else {
                            System.out.println("Warning: Missing ERP details for roll: " + i.getRollNo());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: Couldn't fetch instructor details: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            throw new SQLException(ex);
        }
        return insList;
    }

    public static int unassignInstructorHelper(String courseCode, String section) throws SQLException {
        // Set the instructor (roll_no) to 'N/A' effectively unassigning them
        String query = "UPDATE sections SET roll_no = 'N/A' WHERE course_code = ? AND section = ?";
        
        try (Connection connection = DatabaseConnector.getErpConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, courseCode);
            statement.setString(2, section);
            
            return statement.executeUpdate();
        }
    }

    public static Course getCourseHelper(String courseCode, String section) throws SQLException {
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                SELECT * FROM courses WHERE course_code = ? AND section = ?; 
            """)) {
                statement.setString(1, courseCode); 
                statement.setString(2, section); 

                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        Course c = new Course();
                        c.setCourseCode(rs.getString("course_code"));
                        c.setTitle(rs.getString("title"));
                        c.setSection(rs.getString("section"));                            
                        c.setCredits(rs.getString("credits"));
                        return c;
                    }
                }
            }
        }
        return null; 
    }

    public static int createCourseHelper(String courseCode, String title, String section, int credits) throws SQLException {
        String sql = "INSERT INTO courses (course_code, title, section, credits) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnector.getErpConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, courseCode);
            statement.setString(2, title);
            statement.setString(3, section);
            statement.setInt(4, credits);
            
            return statement.executeUpdate();
        }
    }

    public static ArrayList<Course> getAllCourses() throws SQLException {
        ArrayList<Course> courseList = new ArrayList<>();
        
        try (Connection connection = DatabaseConnector.getErpConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT course_code, title, section, credits FROM courses")) {
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Course c = new Course();
                    c.setCourseCode(rs.getString("course_code"));
                    c.setTitle(rs.getString("title"));
                    c.setSection(rs.getString("section"));
                    c.setCredits(rs.getString("credits"));

                    courseList.add(c);
                }
            }
        }
        return courseList;
    }

    public static ArrayList<Sections> getAllSections() throws SQLException {
        ArrayList<Sections> sectionList = new ArrayList<>();
        
        try (Connection connection = DatabaseConnector.getErpConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM sections")) {
            
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Sections s = new Sections();
                    s.setCourseCode(rs.getString("course_code"));
                    s.setSection(rs.getString("section"));
                    s.setRollNo(rs.getString("roll_no"));
                    s.setDayTime(rs.getString("day_time"));
                    s.setRoom(rs.getString("room"));
                    s.setCapacity(rs.getString("capacity")); 
                    s.setSemester(rs.getString("semester"));
                    s.setYear(rs.getString("year"));
                    
                    sectionList.add(s);
                }
            }
        }
        return sectionList;
    }

    public static boolean checkInstructorSection(String insRollno, String courseCode, String section) throws SQLException {
        String query = "SELECT 1 FROM sections WHERE course_code = ? AND section = ? AND roll_no = ?";
        
        try (Connection connection = DatabaseConnector.getErpConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, courseCode);
            statement.setString(2, section);
            statement.setString(3, insRollno); 
            
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next(); 
            }
        }
    }
}
