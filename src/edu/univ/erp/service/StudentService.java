package edu.univ.erp.service;

import java.sql.SQLException;

import edu.univ.erp.access.modeOps;
import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.data.NotificationCommandRunner;
import edu.univ.erp.domain.Settings;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StudentService {

    // Define error codes for clarity
    public static final int STATUS_MAINTENANCE = -3;
    public static final int STATUS_DEADLINE_PASSED = -2;

    public int dropCourse(String rollNo, String courseCode) {
        // 1. Check Maintenance Mode
        if (!isSystemActive()) {
            System.out.println("Drop failed: Maintenance Mode is ON.");
            return STATUS_MAINTENANCE; 
        }

        // 2. Check Deadline
        Settings deadlineSetting = modeOps.getSetting("drop_deadline");
        
        if (deadlineSetting != null && deadlineSetting.getValue() != null) {
            try {
                LocalDate deadline = LocalDate.parse(deadlineSetting.getValue(), DateTimeFormatter.ISO_LOCAL_DATE);
                LocalDate today = LocalDate.now();

                if (today.isAfter(deadline)) {
                    return STATUS_DEADLINE_PASSED; 
                }
            } catch (DateTimeParseException e) {
                return -11;
            }
        }

        // 3. Proceed with Drop if allowed
        int rowsDeleted = ErpCommandRunner.studentDropCourseHelper(rollNo, courseCode);
        
        if (rowsDeleted > 0) {
            try {
                NotificationCommandRunner.sendNotification(Integer.parseInt(rollNo), "Dropped course: " + courseCode);
            } catch (Exception e) { 
                System.out.println("Notification Error"); 
            }
        }
        return rowsDeleted;
    }

    public int registerCourse (String rollNo, String courseCode, String section) {
        // 1. Check Maintenance Mode
        if (!isSystemActive()) {
            System.out.println("Registration failed: Maintenance Mode is ON.");
            return STATUS_MAINTENANCE;
        }

        // 2. Proceed with Registration
        int rowsInserted = ErpCommandRunner.studentRegisterHelper(rollNo, courseCode, section, "enrolled");
        if (rowsInserted > 0) {
            NotificationCommandRunner.sendNotification(Integer.parseInt(rollNo), "Registered for: " + courseCode);
        }
        return rowsInserted;
    }

    public boolean isSystemActive() {
        Settings mode = modeOps.getSetting("maintain_mode");
        // If mode is found and is "true", system is NOT active
        if (mode != null && mode.isTrue()) {
            return false; 
        }
        return true; 
    }

    public String[][] gradeData (String rollNo) throws SQLException {
        return ErpCommandRunner.studentGradeHelper(rollNo);
    }

    public String[][] timetable (String rollNo) throws SQLException {
        return ErpCommandRunner.studentTimeTableHelper(rollNo);
    }
    
    public String[] getTranscript(String stdRollNo) throws SQLException {
        return ErpCommandRunner.studentTranscriptHelper(stdRollNo);
    }
}