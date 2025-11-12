package edu.univ.erp.ui.common;

import javax.swing.*;

import edu.univ.erp.ui.instructor.InstructorDashboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.univ.erp.data.DatabaseConnector;

import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;

public class AllCourses {

    public AllCourses(String roll_no, String role) {

        JFrame f = new JFrame("All Courses");
        f.setSize(600,600);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.setLocationRelativeTo(null);
        
        f.setVisible(true);

        String data[][] = dataPull();
        String columName[] = {" course Code ", " Title ", " section ", " credits "};
        JTable t = new JTable(data, columName);
        JScrollPane sp = new JScrollPane(t);
        
        f.add(sp);
        
        JButton backButton = new JButton("<- Back");
        backButton.setBounds(500, 30, 100, 30);
        sp.add(backButton);
       


        // action listener
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (role.equals("student")) {
                    f.dispose();
                    // new studentDashboard(Username);
                }
                else if (role.equals("instructor")) {
                    f.dispose();
                    new InstructorDashboard(roll_no);
                }
                else if (role.equals("admin")) {
                    f.dispose();
                    // new AdminDashboard(String username);
                }
            }
        });
    }

    private String[][] dataPull() {
        ArrayList<String[]> arrList = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select course_code, title, section,credits FROM courses; 
                    """)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String course_code = resultSet.getString("course_code");
                        String title = resultSet.getString("title");
                        String section = resultSet.getString("section");
                        String credits = resultSet.getString("credits");
             
                        arrList.add(new String[]{course_code , title ,  section , credits});
                    } 
                    if (empty) {
                        System.out.println("\t (no data)");
                    }
                }
            } 
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        String[][] strArr = new String[arrList.size()][4];
        arrList.toArray(strArr);
        return strArr;
    }    
}
