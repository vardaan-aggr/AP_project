package edu.univ.erp.ui.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import org.mindrot.jbcrypt.BCrypt;

import edu.univ.erp.data.DatabaseConnector;
import edu.univ.erp.ui.student.studentDashboard;
import edu.univ.erp.ui.admin.adminDashboard;
import edu.univ.erp.ui.instructor.InstructorDashboard;

import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;

public class loginPage {
    public static void main(String[] args) {
        JFrame loginFrame = new JFrame("University ERP Login"); 

        loginFrame.setSize(450, 300); 
        loginFrame.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 60, 100, 30);
        loginFrame.add(userLabel);
        
        JTextField t1 = new JTextField();
        t1.setBounds(150, 60, 250, 30);
        loginFrame.add(t1);
        
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 110, 100, 30);
        loginFrame.add(passLabel);
        
        JPasswordField t2 = new JPasswordField();
        t2.setBounds(150, 110, 250, 30);
        loginFrame.add(t2);
        
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 170, 100, 30);
        loginFrame.add(loginBtn);

        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null); 
        loginFrame.setVisible(true);
    
    loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username_input = t1.getText();
                String password_input = new String(t2.getPassword());
                try (Connection connection = DatabaseConnector.getAuthConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("""
                                Select hash_password, role, roll_no FROM auth_table WHERE username = ?; 
                            """)) {
                        statement.setString(1, username_input);
                        try (ResultSet resultSet = statement.executeQuery()) {
                            boolean empty = true;
                            while (resultSet.next()) {
                                empty = false;
                                String hash_pass_db = resultSet.getString("hash_password");
                                String role_db = resultSet.getString("role");
                                // System.out.println("Billi kre meow meow 🙀: "+ hash_pass_db);
                                if (BCrypt.checkpw(password_input, hash_pass_db)) {
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


