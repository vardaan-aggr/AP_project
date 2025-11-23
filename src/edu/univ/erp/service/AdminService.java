package edu.univ.erp.service;

import edu.univ.erp.data.AuthCommandRunner;
import edu.univ.erp.data.ErpCommandRunner;
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
}