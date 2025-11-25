package edu.univ.erp.service;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.univ.erp.access.modeOps;
import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.data.NotificationCommandRunner;
import edu.univ.erp.domain.Grades;
import edu.univ.erp.domain.Settings;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StudentService {
   public int dropCourse(String rollNo, String courseCode) {
        Settings deadlineSetting = modeOps.getSetting("drop_deadline");
        
        if (deadlineSetting != null && deadlineSetting.getValue() != null) {
            try {
                LocalDate deadline = LocalDate.parse(deadlineSetting.getValue(), DateTimeFormatter.ISO_LOCAL_DATE);
                LocalDate today = LocalDate.now();

                if (today.isAfter(deadline)) {
                    System.out.println("Drop failed: Deadline passed (" + deadline + ")");
                    return -2; // Return a specific error code for "Deadline Passed"
                }
            } catch (DateTimeParseException e) {
                System.err.println("Error: Invalid deadline date format in DB. Expected YYYY-MM-DD.");
            }
        }

        // 2. Proceed with Drop if deadline is okay
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

    public String[][] gradeData (String rollNo) throws SQLException {
        try {
            return ErpCommandRunner.studentGradeHelper(rollNo);
        }  catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public int registerCourse (String rollNo, String courseCode, String section) {
        int rowsInserted = ErpCommandRunner.studentRegisterHelper(rollNo, courseCode, section, "enrolled");
        if (rowsInserted > 0) {
            NotificationCommandRunner.sendNotification(Integer.parseInt(rollNo), "Registered for: " + courseCode);
        }
        return rowsInserted;
    }

    public boolean isSystemActive() {
        Settings mode = modeOps.getSetting("maintain_mode");
        if (mode != null && mode.isTrue()) {
            return false; 
        }
        return true; 
    }

    public String[][] timetable (String rollNo) throws SQLException {
        try {
            return ErpCommandRunner.studentTimeTableHelper(rollNo);
        }  catch (SQLException e) {
            throw new SQLException(e);
        }
    }
    public ArrayList<Grades> getTranscript(String rollNo) throws SQLException {
        return ErpCommandRunner.studentTranscriptHelper(rollNo);
    }
}