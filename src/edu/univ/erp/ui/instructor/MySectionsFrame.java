package edu.univ.erp.ui.instructor;

import javax.swing.*;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.univ.erp.data.DatabaseConnector;

public class MySectionsFrame {

    public MySectionsFrame(String roll_no) {
        JFrame f ;
        f = new JFrame("My Sections");
        f.setSize(600, 400);
        f.setLayout(null);
        f.setLocationRelativeTo(null); 
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        String data[][] = dataPull(roll_no);
        String columName[] = {" course Code "};
        JTable t = new JTable(data, columName);
        JScrollPane sp = new JScrollPane(t);
        sp.setBounds(50, 50, 500, 200);
        
        f.add(sp);
 
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBounds(200, 300, 200, 30);
        f.add(backButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new InstructorDashboard(roll_no);
                f.dispose();
            }
        });

    }

    private String[][] dataPull(String roll_no) {
        ArrayList<String[]> arrList = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select course_code FROM sections WHERE roll_no = ?; 
                    """)) {
                statement.setString(1, roll_no); // Replace with actual instructor ID

                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String course_code = resultSet.getString("course_code");

             
                        arrList.add(new String[]{course_code });
                    } 
                    if (empty) {
                        System.out.println("\t (no data)");
                    }
                }
            } 
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        String[][] strArr = new String[arrList.size()][1];
        arrList.toArray(strArr);
        return strArr;
    }
}
