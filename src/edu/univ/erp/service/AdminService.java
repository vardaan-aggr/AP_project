package edu.univ.erp.service;

import edu.univ.erp.data.AuthCommandRunner;
import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.domain.Sections;
import edu.univ.erp.util.modeOps;
import java.sql.SQLException;

public class AdminService {

    public String registerStudent(String username, String hashPass, String program, String year) {
        if ("true".equals(modeOps.getMaintainMode())) {
            return "System is in maintenance mode.";
        }
        try {
            int rollNo = AuthCommandRunner.registerUserAuth(username, "student", hashPass);
            if (rollNo == -1) {
                return "Failed to create student in auth.";
            }

            boolean erpSuccess = ErpCommandRunner.registerStudentErp(rollNo, program, year);
            if (!erpSuccess) {
                return "Created Auth user but failed to create ERP student.";
            }

            return "Success: Student added with Roll No " + rollNo;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    public String registerInstructor(String username, String hashPass, String department) {
        if ("true".equals(modeOps.getMaintainMode())) {
            return "System is in maintenance mode.";
        }
        try {
            int rollNo = AuthCommandRunner.registerUserAuth(username, "instructor", hashPass);
            if (rollNo == -1) {
                return "Failed to create instructor in auth.";
            }

            boolean erpSuccess = ErpCommandRunner.registerInstructorErp(rollNo, department);
            if (!erpSuccess) {
                return "Created Auth user but failed to create ERP student.";
            }

            return "Success: Instructor added with Roll No " + rollNo;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    public String registerAdmin(String username, String hashPass) {
        if ("true".equals(modeOps.getMaintainMode())) {
            return "System is in maintenance mode.";
        }
        try {
            int rollNo = AuthCommandRunner.registerUserAuth(username, "admin", hashPass);
            if (rollNo < 1) {
                return "Failed to create admin in auth.";
            }

            return "Success: Admin added with Roll No " + rollNo;

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    public String unassignInstructor(String courseCode, String section) {
        if ("true".equals(edu.univ.erp.util.modeOps.getMaintainMode())) {
            return "System is in maintenance mode.";
        }

        try {
            int rows = ErpCommandRunner.unassignInstructorHelper(courseCode, section);
            
            if (rows > 0) {
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
        if ("true".equals(edu.univ.erp.util.modeOps.getMaintainMode())) {
            return "System is in maintenance mode.";
        }

        try {
            int result = ErpCommandRunner.sectionUpdater(rollNo, dayTime, room, capacity, semester, year, courseCode, section);
            
            if (result > 0) return "Success";
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

    // In edu.univ.erp.service.AdminService

    public String updateCourse(String title, String credits, String courseCode, String section) {
        // 1. Check Maintenance Mode
        if ("true".equals(edu.univ.erp.util.modeOps.getMaintainMode())) {
            return "System is in maintenance mode.";
        }

        try {
            // 2. Call Data Layer
            int result = ErpCommandRunner.courseUpdater(title, credits, courseCode, section);
            
            // 3. Interpret Result
            if (result > 0) return "Success";
            if (result == -1) return "Error: Credits must be a valid number.";
            return "Error: Course not found or no changes made.";
            
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }
}