package edu.univ.erp.service;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.data.NotificationCommandRunner;
import edu.univ.erp.domain.Sections;

public class InstructorService {
    
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

    public String computeAndAssignGrade(String rollNo, String courseCode, String section, String quizStr, String midStr, String endStr) {

        try {
            int quiz = Integer.parseInt(quizStr);
            int mid = Integer.parseInt(midStr);
            int end = Integer.parseInt(endStr);

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

            // Check Enrollment 
            boolean isEnrolled = ErpCommandRunner.isStudentEnrolled(rollNo, courseCode, section);
            if (!isEnrolled) {
                return "Error: Student is not enrolled in this course/section.";
            }

            // Save to DB 
            boolean saved = ErpCommandRunner.addGrade(rollNo, courseCode, section, finalGrade);
            if (saved) {
                try {
                    int studentId = Integer.parseInt(rollNo);
                    NotificationCommandRunner.sendNotification(studentId, "Grade posted for: " + courseCode);
                } catch (NumberFormatException e) {
                    System.out.println("Notification failed: Invalid student ID format.");
                }
                
                return "Success";
            } else {
                return "Error: Failed to save grade to database.";
            }

        } catch (NumberFormatException e) {
            return "Error: Marks must be valid integers.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }

    public ArrayList<Sections> getMySections(String rollNo) throws SQLException {
        return ErpCommandRunner.instructorMySectionsHelper(rollNo);
    }
}