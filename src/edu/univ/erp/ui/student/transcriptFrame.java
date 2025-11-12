package edu.univ.erp.ui.student;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.opencsv.CSVWriter;
import edu.univ.erp.data.DatabaseConnector;
import java.util.ArrayList;

public class transcriptFrame {
    public transcriptFrame(String rollNo) throws IOException {
        File f = new File("AP_project/src/edu/univ/erp/util/output.csv"); 
        try {
            FileWriter outFile = new FileWriter(f);
            CSVWriter csvWriter = new CSVWriter(outFile);
            
            String[] header = {"Course ", "Title -----------", "Grade"};
            csvWriter.writeNext(header);
            ArrayList<String[][]>

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
                                
                            } 
                            if (empty) {
                                System.out.println("\t (no data)");
                            }
                        }
                    } 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
         
    }
}
