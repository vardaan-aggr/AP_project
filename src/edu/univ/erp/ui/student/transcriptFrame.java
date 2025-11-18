package edu.univ.erp.ui.student;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import com.opencsv.CSVWriter;
import edu.univ.erp.data.DatabaseConnector;
import java.util.ArrayList;

public class transcriptFrame {
    public transcriptFrame(String username, String role, String in_pass, String rollNo) throws IOException {
        File f = new File("AP_project/src/edu/univ/erp/util/output.csv"); 
        try {
            FileWriter outFile = new FileWriter(f);
            CSVWriter csvWriter = new CSVWriter(outFile);
            String[] header = {"Course ", "Grade"};
            csvWriter.writeNext(header);
            ArrayList<String[]> arrCourseCode = new ArrayList<>();

            try (Connection connection = DatabaseConnector.getErpConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("""
                            Select course_code, section FROM enrollments WHERE status = ?; 
                        """)) {
                    statement.setString(1, "enrolled");
                    try (ResultSet resultSet = statement.executeQuery()) {
                        boolean empty = true;
                        while (resultSet.next()) {
                            empty = false;
                            String courseCode = resultSet.getString("course_code");
                            String section = resultSet.getString("section"); 
                            arrCourseCode.add(new String[]{courseCode, section});
                        } 
                        if (empty) {
                            JOptionPane.showMessageDialog(null, "Error no courses enrolled", "Error", JOptionPane.ERROR_MESSAGE); 
                            System.out.println("\t (no courses enrolled)");
                        }
                    }
                } 
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error getting enrolled courses: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            ArrayList<String> arrGrade = new ArrayList<>();
            for (int i = 0; i < arrCourseCode.size(); i++) {
                try (Connection connection = DatabaseConnector.getErpConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("""
                                Select grade FROM grades WHERE roll_no = ? and course_code = ?; 
                            """)) {
                        statement.setString(1, rollNo);
                        statement.setString(2, arrCourseCode.get(i)[0]);
                        try (ResultSet resultSet = statement.executeQuery()) {
                            boolean empty = true;
                            while (resultSet.next()) {
                                empty = false;
                                String grade = resultSet.getString("grade");
                                arrGrade.add(grade);
                            } 
                            if (empty) {
                                JOptionPane.showMessageDialog(null, "Error no grades for courses enrolled", "Error", JOptionPane.ERROR_MESSAGE); 
                                System.out.println("\t (no grades for courses enrolled)");
                            }
                        }
                    } 
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error getting grades: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
            for (int i = 0; i < arrCourseCode.size(); i++) {
                String[] arr = {arrCourseCode.get(i)[0], arrGrade.get(i)};   
                csvWriter.writeNext(arr);                    
            }
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Downloaded successfully.", "Print transcript", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("\tGoing back to Student Dashboard..");
        new studentDashboard(username, role, in_pass, rollNo);
    }
}
