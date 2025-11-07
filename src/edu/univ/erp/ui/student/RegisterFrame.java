package edu.univ.erp.ui.student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import org.mindrot.jbcrypt.BCrypt;

import edu.univ.erp.data.DatabaseConnector;
import edu.univ.erp.ui.student.StudentDashboard;
import edu.univ.erp.ui.admin.AdminDashboard;
import edu.univ.erp.ui.instructor.InstructorDashboard;

import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;

public class RegisterFrame {
    public static void main(String[] args) {
        JFrame loginFrame = new JFrame("Registration");
        // Use BorderLayout for the main frame
        loginFrame.setLayout(new BorderLayout());

        // 1. Create a panel for the form fields with a GridLayout
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 cols, 10px gaps
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Add padding

        JLabel userLabel = new JLabel("Course Code:");
        formPanel.add(userLabel);
        
        JTextField userText = new JTextField();
        formPanel.add(userText);
        
        JLabel sectionLabel = new JLabel("Section:"); // Renamed from passLabel
        formPanel.add(sectionLabel);
        
        JTextField sectionText = new JTextField(); // Changed from JPasswordField
        formPanel.add(sectionText);

        JLabel rollLabel = new JLabel("Roll No:"); // Fixed label text
        formPanel.add(rollLabel);
        
        JTextField rollText = new JTextField(); // Changed from JPasswordField
        formPanel.add(rollText);
        
        // 2. Create a panel for the button (to center it)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton regBtn = new JButton("Register");
        regBtn.setPreferredSize(new Dimension(100, 30)); // Give button a preferred size
        buttonPanel.add(regBtn);

        // 3. Add panels to the frame
        loginFrame.add(formPanel, BorderLayout.CENTER);  // Form goes in the middle
        loginFrame.add(buttonPanel, BorderLayout.SOUTH); // Button goes at the bottom

        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.pack(); // Automatically sizes the window to fit components
        loginFrame.setLocationRelativeTo(null); 
        loginFrame.setVisible(true);

        regBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 1. Get text from the fields
                String courseCode_in = userText.getText();
                String section_in = new String(passText.getPassword());
                try (Connection connection = DatabaseConnector.getErpConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("""
                                Insert into enrollments (roll_no, section_id, status) values 
                                    ('?', '?', '?');
                            """)) {
                        statement.setString(1, );
                        statement.setString(2, section_in);
                        statement.setString(3, "enrolled");
                        try (ResultSet resultSet = statement.executeQuery()) {
                            boolean empty = true;
                            while (resultSet.next()) {
                                empty = false;
                                String hash_pass_db = resultSet.getString("hash_password");
                                String role_db = resultSet.getString("role");
                                if (BCrypt.checkpw(section_in, hash_pass_db)) {
                                    System.out.println("\nCorrect Password\n");
                                    if (role_db.equals("student")) {
                                        loginFrame.dispose();
                                        System.out.println("--- Opening Student Dashboard");
                                        new StudentDashboard();
                                    }
                                    else if (role_db.equals("instructor")) {
                                        loginFrame.dispose();
                                        System.out.println("--- Opening Instructor Dashboard");
                                        new InstructorDashboard();
                                    }
                                    else if (role_db.equals("admin")) {
                                        loginFrame.dispose();
                                        System.out.println("--- Opening Admin Dashboard");
                                        new AdminDashboard();
                                    }
                                } else {
                                    System.out.println("WrongPassword");
                                }
                            } 
                            if (empty) {
                                System.out.println("\t (no data)");
                            }
                        }
                    } 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
