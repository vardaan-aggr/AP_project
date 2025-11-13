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
import java.awt.Color;
import java.awt.Font;

public class loginPage {
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(450,300);
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));

        JLabel l0 = new JLabel("ERP LOGIN");
        l0.setBounds(0, 0, 450, 50);
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#d8d0c4"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);
        
        JLabel l1 = new JLabel("Username: ");
        l1.setBounds(50, 80, 100, 30);
        f.add(l1);
        JTextField t1 = new JTextField(50);
        t1.setBounds(150, 80, 250, 30);
        f.add(t1);
        
        JLabel l2 = new JLabel("Password: ");
        l2.setBounds(50, 130, 100, 30);
        f.add(l2);
        JPasswordField t2 = new JPasswordField();
        t2.setBounds(150, 130, 250, 30);
        f.add(t2);

        JButton b1 = new JButton("Login");
        b1.setBounds(150, 190, 100, 30);
        b1.setBackground(Color.decode("#2f77b1")); 
        b1.setForeground(Color.WHITE); 
        b1.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b1);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    
        b1.addActionListener(new ActionListener() {
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
                                String roll_no = resultSet.getString("roll_no");
                                // System.out.println("Billi kre meow meow 🙀: "+ hash_pass_db);
                                if (BCrypt.checkpw(password_input, hash_pass_db)) {
                                    System.out.println("\nCorrect Password\n");
                                    if (role_db.equals("student")) {
                                        f.dispose();
                                        System.out.println("\tOpening Student Dashboard..");
                                        new studentDashboard(username_input, role_db, password_input, roll_no);
                                    }
                                    else if (role_db.equals("instructor")) {
                                        f.dispose();
                                        System.out.println("--- Opening Instructor Dashboard");
                                        new InstructorDashboard(roll_no);
                                    }
                                    else if (role_db.equals("admin")) {
                                        f.dispose();
                                        System.out.println("--- Opening Admin Dashboard");
                                        new adminDashboard();
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