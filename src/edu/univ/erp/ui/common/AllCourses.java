package edu.univ.erp.ui.common;

import javax.swing.*;

import org.mindrot.jbcrypt.BCrypt;

import edu.univ.erp.ui.admin.AdminDashboard;
import edu.univ.erp.ui.instructor.InstructorDashboard;
import edu.univ.erp.ui.student.StudentDashboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.univ.erp.data.DatabaseConnector;

import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;

public class AllCourses {
    private JFrame frame;

    public AllCourses(String username, String role) {
        // String UserName = username;

        JButton backButton = new JButton("<- Back");
        backButton.setBounds(200, 300, 200, 30);
        frame.add(backButton);
        
        try (Connection connection = DatabaseConnector.getErpConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("""
                                Select * FROM courses ; 
                            """)) {
                        try (ResultSet resultSet = statement.executeQuery()) {
                            boolean empty = true;
                            while (resultSet.next()) {
                                empty = false;
                                String course_code = resultSet.getString("course_code");
                                String title = resultSet.getString("title");
                                String section = resultSet.getString("section");
                                String credits = resultSet.getString("credits");
            
                          } 
                            if (empty) {
                                System.out.println("\t (no data)");
                            }
                        }
                    } 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            

        // action listener
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (role.equals("student")) {
                    frame.dispose();
                    new StudentDashboard(String username);
                }
                else if (role.equals("instructor")) {
                    frame.dispose();
                    new InstructorDashboard(String username);
                }
                else if (role.equals("admin")) {
                    frame.dispose();
                    new AdminDashboard(String username);
                }
            }
        });

    
        
    }


    
}
