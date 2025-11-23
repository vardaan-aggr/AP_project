package edu.univ.erp.ui.student;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import com.opencsv.CSVWriter;
import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.domain.Grades;

public class transcriptFrame {
    public transcriptFrame(String username, String role, String in_pass, String rollNo) {
        String filePath = System.getProperty("user.home") + "/Downloads/transcript_" + rollNo + ".csv";
        File f = new File(filePath);
        boolean success = false;

        try (FileWriter outFile = new FileWriter(f);
             CSVWriter csvWriter = new CSVWriter(outFile)) {
            csvWriter.writeNext(new String[]{"Course", "Grade"});
            ArrayList<Grades> arrCourseCode = ErpCommandRunner.studentTranscriptHelper(rollNo);
            for (Grades g : arrCourseCode) {
                csvWriter.writeNext(new String[]{g.getCourseCode(), g.getGrade()});
            }
            success = true;
        } catch (IOException ioex) {
            JOptionPane.showMessageDialog(null, "File access error: " + ioex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error generating transcript: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (success) {
            JOptionPane.showMessageDialog(null, "Transcript downloaded to:\n" + filePath, "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        System.out.println("\tGoing back to Student Dashboard..");
        new studentDashboard(username, role, in_pass, rollNo);
    }
}