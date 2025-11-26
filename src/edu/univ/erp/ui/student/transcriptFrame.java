package edu.univ.erp.ui.student;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;

import com.opencsv.CSVWriter;

import edu.univ.erp.domain.Grades;
import edu.univ.erp.service.StudentService;

public class transcriptFrame {
    
    public transcriptFrame(String username, String role, String in_pass, String rollNo) {
        
        // 1. Let user choose where to save
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Transcript");
        fileChooser.setSelectedFile(new File("transcript_" + rollNo + ".csv"));
        
        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".csv")) {
                filePath += ".csv";
            }

            boolean success = false;
            try {
                StudentService service = new StudentService();
                ArrayList<Grades> transcriptList = service.getTranscript(rollNo);

                if (transcriptList == null || transcriptList.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No academic records found to export.", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    try (FileWriter outFile = new FileWriter(filePath);
                         CSVWriter csvWriter = new CSVWriter(outFile)) {
                        
                        // Header
                        csvWriter.writeNext(new String[]{username + " Grades"});
                        csvWriter.writeNext(new String[]{"Course Code", "Section", "Grade"});
                        
                        // Data
                        for (Grades g : transcriptList) {
                            csvWriter.writeNext(new String[]{ g.getCourseCode(), g.getSection(),  g.getGrade()});
                        }
                        success = true;
                    }
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (IOException ioex) {
                JOptionPane.showMessageDialog(null, "File Write Error (Is the file open?): " + ioex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (success) {
                JOptionPane.showMessageDialog(null, "Transcript saved successfully to:\n" + filePath, "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        // Return to dashboard regardless of whether they saved or cancelled
        System.out.println("\tGoing back to Student Dashboard..");
        new studentDashboard(username, role, in_pass, rollNo);
    }
}