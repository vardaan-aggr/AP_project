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

        String filePath = System.getProperty("user.home") + "/Documents/JavaProject/AP_project/src/edu/univ/erp/util/transcript_" + rollNo + ".csv";
        boolean success = false;
        try {
            StudentService service = new StudentService();
            ArrayList<Grades> transcriptList = service.getTranscript(rollNo);

            if (transcriptList == null || transcriptList.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No academic records found to export.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                File f = new File(filePath);
                try (FileWriter outFile = new FileWriter(f);
                    CSVWriter csvWriter = new CSVWriter(outFile)) {
                    csvWriter.writeNext(new String[]{"Course Code", "Section", "Grade"});
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
            JOptionPane.showMessageDialog(null, "Transcript downloaded successfully to:\n" + filePath, "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        System.out.println("\tGoing back to Student Dashboard..");
        new studentDashboard(username, role, in_pass, rollNo);
    }
}