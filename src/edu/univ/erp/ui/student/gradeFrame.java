package edu.univ.erp.ui.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

import javax.swing.*;

import edu.univ.erp.data.DatabaseConnector;


public class gradeFrame {
    public gradeFrame(String roll_no) {
        JFrame f = new JFrame();
        f.setSize(600,600);

        String[][] data = gradePull(roll_no);
        String [] columName = {"Course code", "Grade"};
        JTable t = new JTable(data, columName);

        JScrollPane sp = new JScrollPane(t);
        f.add(sp);

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
    private String[][] gradePull(String roll_no) {        
        ArrayList<String[]> data = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select course_code, grade FROM grades WHERE roll_no = ?; 
                    """)) {
                statement.setString(1, String.valueOf(roll_no));
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String courseCode = resultSet.getString("course_code");
                        String grade = resultSet.getString("grade"); 
                        data.add(new String[]{courseCode, grade});
                    } 
                    if (empty) {
                        System.out.println("\t (no data)");
                    }
                }
            } 
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        String[][] strArr = new String[data.size()][2];
        for (int i = 0; i < data.size(); i++) {
            strArr[i][0] = data.get(i)[0];
            strArr[i][1] = data.get(i)[1];
        }
        return strArr;
    }
}