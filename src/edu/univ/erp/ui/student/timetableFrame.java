package edu.univ.erp.ui.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

import javax.swing.*;

import edu.univ.erp.data.DatabaseConnector;

public class timetableFrame {
    public timetableFrame(String roll_no) {
        JFrame f = new JFrame();
        f.setSize(600,600);
        
        String data[][] = dataPull(roll_no);
        String columName[] = {"Code", "Day_Time", "Room"};
        JTable t = new JTable(data, columName);
        
        JScrollPane sp = new JScrollPane(t);
        f.add(sp);

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private String[][] dataPull(String roll_no) {
        ArrayList<String[]> arrList = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select course_code, day_time, room, semester, year FROM sections WHERE roll_no = ?; 
                    """)) {
                statement.setString(1, String.valueOf(roll_no));
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String courseCode = resultSet.getString("course_code");
                        String day_time = resultSet.getString("day_time"); 
                        String room = resultSet.getString("room"); 
                        arrList.add(new String[]{courseCode, day_time, room});
                    } 
                    if (empty) {
                        System.out.println("\t (no data)");
                    }
                }
            } 
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        String[][] strArr = new String[arrList.size()][3];
        arrList.toArray(strArr);
        return strArr;
    }
}
