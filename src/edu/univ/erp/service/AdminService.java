package edu.univ.erp.service;

import edu.univ.erp.data.AuthCommandRunner;
import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.data.NotificationCommandRunner;
import edu.univ.erp.domain.Admin;
import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Instructor;
import edu.univ.erp.domain.Sections;
import edu.univ.erp.domain.Student;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class AdminService {

    public String registerStudent(String username, String hashPass, String program, String year) {
        try {
            int rollNo = AuthCommandRunner.registerUserAuth(username, "student", hashPass);
            if (rollNo == -1) return "Failed to create student in auth.";

            boolean erpSuccess = ErpCommandRunner.registerStudentErp(rollNo, program, year);
            if (!erpSuccess) return "Created Auth user but failed to create ERP student.";

            NotificationCommandRunner.sendNotification(rollNo, "Student added in ERP!");

            return "Success: Student added with Roll No " + rollNo;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    public String registerInstructor(String username, String hashPass, String department) {
        try {
            int rollNo = AuthCommandRunner.registerUserAuth(username, "instructor", hashPass);
            if (rollNo == -1) return "Failed to create instructor in auth.";

            boolean erpSuccess = ErpCommandRunner.registerInstructorErp(rollNo, department);
            if (!erpSuccess) return "Created Auth user but failed to create ERP student.";

            NotificationCommandRunner.sendNotification(rollNo, "Instructor added.");


            return "Success: Instructor added with Roll No " + rollNo;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    public String registerAdmin(String username, String hashPass) {
        try {
            int rollNo = AuthCommandRunner.registerUserAuth(username, "admin", hashPass);
            if (rollNo < 1) return "Failed to create admin in auth.";

            return "Success: Admin added with Roll No " + rollNo;

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    public String unassignInstructor(String courseCode, String section) {
        try {
             // 1. Find out who is currently assigned
            Sections s = ErpCommandRunner.SectionInfoGetter(courseCode, section);
            String oldInstructor = (s != null) ? s.getRollNo() : "N/A";

            // 2. Perform the unassign action
            int rows = ErpCommandRunner.unassignInstructorHelper(courseCode, section);
            
            if (rows > 0) {
                // 3. Notify the instructor who was removed
                if (!oldInstructor.equals("N/A")) {
                    try {
                        int insId = Integer.parseInt(oldInstructor);
                        NotificationCommandRunner.sendNotification(insId, "You have been unassigned from: " + courseCode);
                    } catch (NumberFormatException ignored) {}
                }
                return "Success";
            } else {
                return "Course or Section not found.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    public String updateSection(String rollNo, String dayTime, String room, String capacity, String semester, String year, String courseCode, String section) {
        try {
            int result = ErpCommandRunner.sectionUpdater(rollNo, dayTime, room, capacity, semester, year, courseCode, section);
            if (result > 0) {
                if (!rollNo.equals("N/A")) {
                     try {
                        int insId = Integer.parseInt(rollNo);
                        NotificationCommandRunner.sendNotification(insId, "Update: You are assigned to " + courseCode + " (" + section + ")");
                    } catch (NumberFormatException ignored) {}
                }
                return "Success";
            }
            
            if (result == -1) return "Error: Capacity and Year must be valid numbers.";
            return "Error: Section not found or no changes made.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    public Sections getSectionDetails(String courseCode, String section) throws SQLException {
        return ErpCommandRunner.SectionInfoGetter(courseCode, section);
    }

    public String updateCourse(String title, String credits, String courseCode, String section) {
        try {
            int result = ErpCommandRunner.courseUpdater(title, credits, courseCode, section);
            if (result > 0) return "Success";
            if (result == -1) return "Error: Credits must be a valid number.";
            return "Error: Course not found or no changes made.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    public Course getCourseDetails(String courseCode, String section) throws SQLException {
        return ErpCommandRunner.getCourseHelper(courseCode, section);
    }

    public String deleteCourseAndSection(String courseCode, String section) {
        String assignedInstructor = "N/A";
        try {
            Sections s = ErpCommandRunner.SectionInfoGetter(courseCode, section);
            if (s != null) assignedInstructor = s.getRollNo();
        } catch (SQLException e) {
            e.printStackTrace(); // Just log, don't stop deletion
        }

        int result = ErpCommandRunner.deleteSecCourseHelper(courseCode, section);

        // 2. If deletion succeeded (result 1, 2, or 3 implies some success), notify
        if (result > 0 && !assignedInstructor.equals("N/A")) {
            try {
                int insId = Integer.parseInt(assignedInstructor);
                NotificationCommandRunner.sendNotification(insId, "Section cancelled/deleted: " + courseCode);
            } catch (NumberFormatException ignored) {}
        }

        switch (result) {
            case 1: return "Success: Course and Section deleted successfully.";
            case 2: return "Warning: Section deleted, but Course not found in database.";
            case 3: return "Warning: Course deleted, but Section not found in database.";
            case 4: return "Error: Neither the Course nor the Section existed.";
            case -1: return "Error: Database failed to delete Section.";
            case -2: return "Error: Database failed to delete Course.";
            case -3: return "Error: Could not connect to Database.";
            default: return "Error: Unknown error occurred.";
        }
    }

    public String createSection(String courseCode, String section, String instructorRoll, String dayTime, String room, String capacityStr, String semester, String yearStr) {
        if (courseCode.isEmpty() || section.isEmpty() || instructorRoll.isEmpty() || 
            dayTime.isEmpty() || room.isEmpty() || capacityStr.isEmpty() || 
            semester.isEmpty() || yearStr.isEmpty()) {
            return "Error: All fields must be filled out.";
        }

        try {
            int capacity = Integer.parseInt(capacityStr);
            int year = Integer.parseInt(yearStr);

            if (capacity <= 0) return "Error: Capacity must be a positive number.";
            if (year <= 0) return "Error: Year must be a positive number.";

            int result = ErpCommandRunner.createSectionHelper(courseCode, section, instructorRoll, dayTime, room, capacity, semester, year);
            if (result > 0)  {
                try {
                    int insId = Integer.parseInt(instructorRoll);
                    NotificationCommandRunner.sendNotification(insId, "Assigned to new course: " + courseCode + " (" + section + ")");
                } catch (NumberFormatException e) {
                    System.out.println("Could not notify instructor: Invalid Roll No");
                }
                return "Success";
            }
            return "Error: Section creation failed (Check if Course Code exists).";

        } catch (NumberFormatException e) {
            return "Error: Capacity and Year must be valid integers.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    public String createCourse(String courseCode, String title, String section, String creditsStr) {
        if (courseCode.isEmpty() || title.isEmpty() || section.isEmpty() || creditsStr.isEmpty()) {
            return "Error: All fields are required.";
        }

        try {
            int credits = Integer.parseInt(creditsStr);
            if (credits <= 0) return "Error: Credits must be a positive number.";

            int result = ErpCommandRunner.createCourseHelper(courseCode, title, section, credits);
            if (result > 0) return "Success";
            return "Error: Failed to create course (Code/Section might already exist).";

        } catch (NumberFormatException e) {
            return "Error: Credits must be a valid integer.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    private String[] getDbCredentials() {
        Properties prop = new Properties();
        String path = "AP_project/src/resources/Erpdatabase.properties";
        try (FileInputStream input = new FileInputStream(path)) {
            prop.load(input);
            String user = prop.getProperty("username");
            String pass = prop.getProperty("password");
            String url = prop.getProperty("jdbcUrl");
            String name = url.substring(url.lastIndexOf("/") + 1);
            if (name.contains("?")) name = name.substring(0, name.indexOf("?"));
            
            return new String[]{user, pass, name};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String performBackup(String savePath) {
        String[] creds = getDbCredentials();
        if (creds == null) return "Error: Could not load DB properties.";
        List<String> command = Arrays.asList("mariadb-dump", "-u" + creds[0], "-p" + creds[1], "--add-drop-table", creds[2]);
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectOutput(new File(savePath));
        try {
            Process p = pb.start();
            return (p.waitFor() == 0) ? "Success" : "Error: Backup failed";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String performRestore(String loadPath) {
        String[] creds = getDbCredentials();
        if (creds == null) return "Error: Could not load DB properties.";
        List<String> command = Arrays.asList("mariadb", "-u" + creds[0], "-p" + creds[1], creds[2]);
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectInput(new File(loadPath));
        try {
            Process p = pb.start();
            return (p.waitFor() == 0) ? "Success" : "Error: Restore failed";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public ArrayList<Student> getAllStudents() throws SQLException {
        ArrayList<Student> students = AuthCommandRunner.searchStudentAuth();
        if (!students.isEmpty()) { ErpCommandRunner.searchStudentErp(students); }
        return students;
    }

    public ArrayList<Instructor> getAllInstructors() throws SQLException {
        ArrayList<Instructor> instructors = AuthCommandRunner.searchInstructorAuth();
        if (!instructors.isEmpty()) { ErpCommandRunner.searchInstructorErp(instructors); }
        return instructors;
    }

    public ArrayList<Admin> getAllAdmins() throws SQLException {
        return AuthCommandRunner.searchAuthAuth();
    }
}