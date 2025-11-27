package edu.univ.erp.service;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.univ.erp.access.modeOps;
import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.data.NotificationCommandRunner;
import edu.univ.erp.domain.Sections;
import edu.univ.erp.domain.Settings;

public class InstructorService {

    // Define error codes for clarity
    public static final int STATUS_MAINTENANCE = -3;
    
    public double computeStats(String courseCode, String section) throws SQLException {
        String[] grades = ErpCommandRunner.instructorStatsHelper(courseCode, section);
        if (grades == null || grades.length == 0) { return -1.0; }
        int totalPoints = 0;
        for (String grade : grades) {
            switch (grade.toUpperCase()) {
                case "A": totalPoints += 3; break;
                case "B": totalPoints += 2; break;
                case "C": totalPoints += 1; break;
                case "F": totalPoints += 0; break;
                default: break;
            }
        }
        return (double) totalPoints / grades.length;
    }

    public String computeAndAssignGrade(String insRollno, String stdRollno, String courseCode, String section, String quizMarks, String midMarks, String endMarks) throws SQLException {
        try {
            int quiz = Integer.parseInt(quizMarks);
            int mid = Integer.parseInt(midMarks);
            int end = Integer.parseInt(endMarks);

            // Marks Validation
            if (quiz > 10 || mid > 10 || end > 10) {
                return "Error: Marks cannot exceed 10.";
            }

            // Calculate Grade
            int total = (int) (0.2 * quiz + 0.3 * mid + 0.5 * end);
            String finalGrade;
            if (total >= 8) finalGrade = "A";
            else if (total >= 6) finalGrade = "B";
            else if (total >= 4) finalGrade = "C";
            else finalGrade = "F";
            // ------------------------

            boolean isEnrolled = ErpCommandRunner.isStudentEnrolled(stdRollno, courseCode, section);
            if (!isEnrolled) return "Error: Student is not enrolled.";

            boolean isMySection = ErpCommandRunner.isMySection(insRollno, courseCode, section);
            if (!isMySection) return "Error: This is not your section.";

            boolean saved = ErpCommandRunner.addGrade(stdRollno, courseCode, section, finalGrade, quiz, mid, end);
            if (saved) {
            try {
                NotificationCommandRunner.sendNotification(Integer.parseInt(stdRollno), "Grades assigned for course code: " + courseCode);
            } catch (Exception e) { 
                System.out.println("Notification Error"); 
            }
            return "Success";
            }
            else
                return "Error: Failed to save the results in grades.";

        } catch (NumberFormatException e) {
            return "Error: Marks must be valid integers.";
        }
    }
    
    public boolean isSystemActive() {
        Settings mode = modeOps.getSetting("maintain_mode");
        // If mode is found and is "true", system is NOT active
        if (mode != null && mode.isTrue()) {
            return false; 
        }
        return true; 
    }


    public ArrayList<Sections> getMySections(String rollNo) throws SQLException {
        return ErpCommandRunner.instructorMySectionsHelper(rollNo);
    }
    
}