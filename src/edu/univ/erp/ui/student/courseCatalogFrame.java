package edu.univ.erp.ui.student;

import javax.swing.*;

import edu.univ.erp.data.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class courseCatalogFrame {
    public courseCatalogFrame(String rollNo) {
        JFrame f = new JFrame();
        f.setSize(600, 600);

        String[][] data = dataPull(rollNo);
        String[] columNames = {"Course Code", "Title", "Section", "Credits"};
        JTable t = new JTable(data, columNames);
        JScrollPane sp = new JScrollPane(t);
        f.add(sp);

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public String[][] dataPull(String rollNo) {
        ArrayList<String[]> arrList1 = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select course_code, section FROM enrollments WHERE roll_no = ? and status = ?;
                    """)) {
                statement.setString(1, String.valueOf(rollNo));
                statement.setString(2, "enrolled");
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String courseCode = resultSet.getString("course_code");
                        String section = resultSet.getString("section"); 
                        arrList1.add(new String[]{courseCode, section});
                    } 
                    if (empty) {
                        System.out.println("\t (no courses enrolled)");
                    }
                }
            } 
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        ArrayList<String[]> arrList2 = new ArrayList<>();
        for (int i = 0; i <  arrList1.size(); i++) {
            try (Connection connection = DatabaseConnector.getErpConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("""
                            Select course_code, title, section, credits FROM courses WHERE course_code = ? and section = ?; 
                        """)) {
                    statement.setString(1, arrList1.get(i)[0]);
                    statement.setString(2, arrList1.get(i)[1]);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        boolean empty = true;
                        while (resultSet.next()) {
                            empty = false;
                            String courseCode = resultSet.getString("course_code");
                            String title = resultSet.getString("title"); 
                            String section = resultSet.getString("section"); 
                            String credits = resultSet.getString("credits"); 
                            arrList2.add(new String[]{courseCode, title, section, credits});
                        } 
                        if (empty) {
                            System.out.println("\t (no course with given enrollments)");
                        }
                    }
                } 
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        String[][] strArray = new String[arrList1.size()][4];
        arrList2.toArray(strArray);
        return strArray;
    }
}
