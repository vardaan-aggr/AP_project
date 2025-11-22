package edu.univ.erp.data;  

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

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
            }
        } 
        return -1;
    }
    private static int studentEnrollmentsCapacityHandler(String courseCode_in, String section_in) {
        int capacity = -1;
        int enrolled = -1;
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                    Select capacity FROM sections WHERE course_code = ? and section = ?;
                    """)) {
                statement.setString(1, courseCode_in);
                statement.setString(2, section_in);
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean hasData = false;
                    while (resultSet.next()) {
                        hasData = true;
                        capacity = Integer.parseInt(resultSet.getString("capacity"));
                    }
                    if (!hasData) {
                        JOptionPane.showMessageDialog(null, "Section with given Course code and Section doesn't exists.", "Information", JOptionPane.INFORMATION_MESSAGE);
                        System.out.println("\t (Section with given Course code and Section doesn't exists");
                        return -1;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: Couldn't retrive capacity from sections.", "Error", JOptionPane.ERROR_MESSAGE);
                    return -2;
                }
            }
            try (PreparedStatement statement = connection.prepareStatement("""
                    Select COUNT(*) FROM enrollments WHERE course_code = ? and section = ? and status = ?;
                    """)) {
                statement.setString(1, courseCode_in);
                statement.setString(2, section_in);
                statement.setString(3, "enrolled");
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean hasData = false;
                    while (resultSet.next()) {
                        hasData = true;
                        capacity = Integer.parseInt(resultSet.getString("capacity"));
                    }
                    if (!hasData) {
                        JOptionPane.showMessageDialog(null, "No active enrollments or  given course code and section not found.", "Information", JOptionPane.INFORMATION_MESSAGE);
                        System.out.println("\t (No active enrollments or  given course code and section not found)");
                        return -3;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error couldn't get connection from database.");
            return -4;
        }
        
        if (capacity > enrolled) {
            return capacity;
        } else {
            JOptionPane.showMessageDialog(null, "Course Capacity Full, Cannot add more.", "Information", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("\t (Course Capacity Full, Cannot add more.)");  
            return -5;         
        }
    }
    
    public static int studentDropCourseHelper(String rollNo, String courseCode) throws SQLException  {
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        delete from enrollments where roll_no = ? and course_code = ?;
                    """)) {
                statement.setString(1, rollNo);
                statement.setString(2, courseCode);
                int rowsDeleted = statement.executeUpdate();
                return rowsDeleted;
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }
    
    public static String[][] studentGradeHelper(String rollNo) throws SQLException {
        ArrayList<String[]> data = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select course_code, grade FROM grades WHERE roll_no = ?;
                    """)) {
                statement.setString(1, rollNo);
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String courseCode = resultSet.getString("course_code");
                        String grade = resultSet.getString("grade");
                        data.add(new String[]{courseCode, grade});
                    }
                    if (empty) {
                        JOptionPane.showMessageDialog(null, "Error: no courses in grade table", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("\t (no courses in grade table)");
                    }
                }
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        String[][] strArr = new String[data.size()][2];
        for (int i = 0; i < data.size(); i++) {
            strArr[i][0] = data.get(i)[0];
            strArr[i][1] = data.get(i)[1];
        }
        return strArr;
    }
    
    public static String[][] studentTimeTableHelper(String rollNo) throws SQLException {
        ArrayList<String[]> timetableList = new ArrayList<>();
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
                boolean hasData = false;
                while (resultSet.next()) {
                    hasData = true;
                    String courseCode = resultSet.getString("course_code");
                    String dayTime = resultSet.getString("day_time");
                    String room = resultSet.getString("room");
                    timetableList.add(new String[]{courseCode, dayTime, room});
                }
                if (!hasData) {
                    JOptionPane.showMessageDialog(null, "No active enrollments or section details found.", "Information", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("\t (no data found)");
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }

        String[][] resultArr = new String[timetableList.size()][3];
        timetableList.toArray(resultArr);
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

    public static ArrayList<String[]> studentTranscriptHelper(String rollNo) throws SQLException {
    ArrayList<String[]> gradeList = new ArrayList<>();

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
                System.out.println("\t (no courses found for transcript)");
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        throw new SQLException(ex); 
    }

    return gradeList;
}

    public static String[][] instructorMySectionsHelper(String roll_no) throws SQLException{
        ArrayList<String[]> arrList = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select course_code FROM sections WHERE roll_no = ?; 
                    """)) {
                statement.setString(1, roll_no); 

                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String course_code = resultSet.getString("course_code");
                        arrList.add(new String[]{course_code });
                    } 
                    if (empty) {
                        System.out.println("\t (no data)");
                    }
                }
            } 
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        String[][] strArr = new String[arrList.size()][1];
        arrList.toArray(strArr);
        return strArr;
    }

    public static int instructorGradeComputeHelper(String quizMarks, String midsemField, String endsemField, String userRollno_input, String courseCode_input, String section_input) throws SQLException {
        try {
            char finalGrade = computeGrade(quizMarks, midsemField, endsemField);
            
            try (Connection connection = DatabaseConnector.getErpConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("""
                INSERT INTO grades (roll_no, course_code, section, grade) VALUES
                    (?,?,?,?);     """)) {
                    statement.setString(1, userRollno_input);
                    statement.setString(2, courseCode_input);
                    statement.setString(3, section_input);
                    statement.setString(4, String.valueOf(finalGrade));
                    int rows = statement.executeUpdate(); 
                    return rows;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Marks must be numbers only.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
    private static char computeGrade(String quizMarks, String midsemMarks, String endsemMarks) throws NumberFormatException {
        int quiz = Integer.parseInt(quizMarks);
        int midsem = Integer.parseInt(midsemMarks); 
        int endsem = Integer.parseInt(endsemMarks);

        if (quiz > 10 || midsem > 10 || endsem > 10) {
            throw new NumberFormatException("Marks cannot exceed 10");
        }

        int finalGrade = (int)(0.2 * quiz + 0.3 * midsem + 0.5 * endsem); 
        
        if (finalGrade >= 8) {
            return 'A';
        } else if (finalGrade >= 6) {
            return 'B';
        } else if (finalGrade >= 4) {
            return 'C';
        } else {
            return 'F';
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
            
            return statement.executeUpdate() > 0;
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

    public static int createSectionHelper(String course_code ,String section ,String roll_no ,String day_time ,String room ,String capacity,String semester,String year) throws SQLException {
        int rowsInsreted = -1;
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        INSERT INTO sections(course_code ,section ,roll_no ,day_time ,room ,capacity,semester,year ) VALUES (?,?, ?,?,?, ?, ?, ?);
                    """)) {
                statement.setString(1, course_code);
                statement.setString(2, section);
                statement.setString(3, roll_no);
                statement.setString(4, day_time);
                statement.setString(5, room);
                statement.setString(6, capacity);
                statement.setString(7, semester);
                statement.setString(8, year);

                rowsInsreted = statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        return rowsInsreted;
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

    public static String[] SectionInfoGetter(String course_code, String section) throws SQLException {
        String[] arr = new String[8];
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                Select * FROM sections WHERE course_code = ? AND section = ?; 
            """)) {
            statement.setString(1, course_code); 
            statement.setString(2, section); 

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                    
                        String course_code2 = resultSet.getString("course_code");
                        String section2 = resultSet.getString("section");
                        String roll_no = resultSet.getString("roll_no");
                        String day_time = resultSet.getString("day_time");
                        String room = resultSet.getString("room");
                        String capacity = resultSet.getString("capacity");
                        String semester = resultSet.getString("semester");
                        String year = resultSet.getString("year");

                        arr[0] = course_code2;
                        arr[1] = section2;   
                        arr[2] = roll_no;
                        arr[3] = day_time;
                        arr[4] = room;
                        arr[5] = capacity;
                        arr[6] = semester;
                        arr[7] = year;
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException();
        }
        return arr;
    }

    public static int sectionUpdater(String roll_no, String day_time, String room, String capacity, String semester, String year, String course_code, String section) throws SQLException {
        int rowsUpdated = -1;
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            // Use an UPDATE statement
            try (PreparedStatement statement = connection.prepareStatement(
                    """
                    UPDATE sections 
                    SET roll_no = ?, day_time = ?, room = ?, capacity = ?, semester = ?, year = ?
                    WHERE course_code = ? AND section = ?;
                    """)) {
                
                // Set the new values
                statement.setString(1, roll_no); 
                statement.setString(2, day_time); 
                statement.setString(3, room); 
                statement.setString(4, capacity); 
                statement.setString(5, semester); 
                statement.setString(6, year); 

                // Use the original course_code and section from the text fields for the WHERE clause
                // Note: It's better to make t1 (course_code) and t2 (section) non-editable
                // to prevent users from changing the primary key during an edit.
                statement.setString(7, course_code); 
                statement.setString(8, section); 

                rowsUpdated = statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return rowsUpdated;
    }

    public static ArrayList<String[]> searchStudentErp(ArrayList<String[]> arrList) throws SQLException {
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            for (int i = 0; i < arrList.size(); i++) {
                try (PreparedStatement statement = connection.prepareStatement("""
                            Select program, year FROM students where roll_no = ?;
                        """)) {
                    statement.setString(1, arrList.get(i)[0]);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        // boolean empty = true;
                        if (resultSet.next()) {
                            // empty = false;
                            arrList.get(i)[2] = resultSet.getString("program");
                            arrList.get(i)[3] = resultSet.getString("year");
                        }
                        // if (empty) {
                        //     JOptionPane.showMessageDialog(null, "Error: No student with given inputs exists.", "Error", JOptionPane.ERROR_MESSAGE);
                        //     System.out.println("\t (no student in erp database with given input)");
                        //     return null;
                        else {
                        // Student exists in Auth but not in ERP table. 
                        // DO NOT return null here, or you lose all other students.
                        // Just leave it as "N/A" (set in previous function) and continue.
                        System.out.println("Warning: Missing ERP details for roll: " + arrList.get(i)[0]);
                        }
                    }
                }
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: Couldn't fetch student details: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            throw new SQLException(ex);
        }
        return arrList;
    }

    public static ArrayList<String[]> searchInstructorErp(ArrayList<String[]> arrList) throws SQLException {
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            for (int i = 0; i < arrList.size(); i++) {
                try (PreparedStatement statement = connection.prepareStatement("""
                            Select department FROM instructors where roll_no = ?;
                        """)) {
                        statement.setString(1, arrList.get(i)[0]);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        // boolean empty = true;
                        if (resultSet.next()) {
                            // empty = false;
                            arrList.get(i)[2] = resultSet.getString("department");                    
                        } else {
                        System.out.println("Warning: Missing ERP details for roll: " + arrList.get(i)[0]);
                        }
                        // if (empty) {
                        //     JOptionPane.showMessageDialog(null, "Error: No instructor with given inputs exists.", "Error", JOptionPane.ERROR_MESSAGE);
                        //     System.out.println("\t (no instructor in auth database with given input)");
                        //     return null;
                        // }
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: Couldn't fetch instructor details: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            throw new SQLException(ex);
        }
        return arrList;
    }
}
