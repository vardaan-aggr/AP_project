package edu.univ.erp.ui.student;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.*;

import com.opencsv.CSVWriter;

import edu.univ.erp.service.StudentService;

public class transcriptFrame {
    
    public transcriptFrame(String username, String role, String in_pass, String stdRollno) {
        
        // 1. Let user choose where to save
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Transcript");
        fileChooser.setSelectedFile(new File("transcript_" + stdRollno + ".csv"));
        
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
                String[] row = service.getTranscript(stdRollno);

                if (row == null) {
                    JOptionPane.showMessageDialog(null, "No academic records found to export.", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    try (FileWriter outFile = new FileWriter(filePath);
                        CSVWriter csvWriter = new CSVWriter(outFile)) {

                        // Title line
                        csvWriter.writeNext(new String[]{username + "'s result for semester " + row[4] + " and year " + row[5]});

                        // Header
                        csvWriter.writeNext(new String[]{"Course Code", "Section", "Title", "Credits", "Grade"});

                        // Data (single row)
                        csvWriter.writeNext(new String[] {row[0], row[1], row[2], row[3], row[6]});

                        success = true;
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (IOException ioex) {
                JOptionPane.showMessageDialog(null, "File Write Error (Is the file open?): " + ioex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ioex.printStackTrace();
            }

            if (success) {
                JOptionPane.showMessageDialog(null, "Transcript saved successfully to:\n" + filePath, "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        // Return to dashboard regardless of whether they saved or cancelled
        System.out.println("\tGoing back to Student Dashboard..");
        new studentDashboard(username, role, in_pass, stdRollno);
    }
}