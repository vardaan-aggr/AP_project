package edu.univ.erp.ui.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.*;

import edu.univ.erp.util.HashGenerator;
import edu.univ.erp.data.DatabaseConnector;

import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;

public class registerFrame {
    public registerFrame(String username, String role, String in_pass, String roll_no) {
        JFrame f = new JFrame();
        f.setSize(450,300);
        f.setLayout(null);
        
        JLabel l1 = new JLabel("Course code: ");
        l1.setBounds(50, 60, 100, 30);
        f.add(l1);
        JTextField t1 = new JTextField(50);
        t1.setBounds(150, 60, 250, 30);
        f.add(t1);
        
        JLabel l2 = new JLabel("Section: ");
        l2.setBounds(50, 110, 100, 30);
        f.add(l2);
        JTextField t2 = new JTextField();
        t2.setBounds(150, 110, 250, 30);
        f.add(t2);

        JLabel l3 = new JLabel("Roll num: ");
        l3.setBounds(50, 160, 100, 30);
        f.add(l3);
        JTextField t3 = new JTextField();
        t3.setBounds(150, 160, 250, 30);
        f.add(t3); 

        JButton b1 = new JButton("Register");
        b1.setBounds(150, 210, 100, 30);
        f.add(b1);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    
        JButton b2 = new JButton("Back");
        b2.setBounds(300, 210, 100, 30);
        f.add(b2);

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DatabaseConnector.getErpConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("""
                                INSERT INTO auth_table (roll_no, username, role, hash_password) VALUES
                                    ('?', '?', '?', '?');
                            """)) {
                        statement.setString(1, l3.getText());
                        statement.setString(2, username);
                        statement.setString(3, role);
                        statement.setString(4, HashGenerator.makeHash(in_pass));
                        int rowsInsreted = statement.executeUpdate();
                        if (rowsInsreted == 0) {
                            System.out.println("Error: Couldn't register in database.");
                        } else {
                            System.out.println("Registered Successfully.");
                        }
                    } 
                } catch (SQLException ex) {
                    System.out.println("Error: Couldn't register, " + ex);
                }
                f.dispose();
                new studentDashboard(username, role, in_pass, roll_no);
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                new studentDashboard(username, role, in_pass, roll_no);
            }
        });
    }
}


        // JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 cols, 10px gaps
        // formPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Add padding

        // JLabel userLabel = new JLabel("Course Code:");
        // formPanel.add(userLabel);
        
        // JTextField userText = new JTextField();
        // formPanel.add(userText);
        
        // JLabel sectionLabel = new JLabel("Section:"); // Renamed from passLabel
        // formPanel.add(sectionLabel);
        
        // JTextField sectionText = new JTextField(); // Changed from JPasswordField
        // formPanel.add(sectionText);

        // JLabel rollLabel = new JLabel("Roll No:"); // Fixed label text
        // formPanel.add(rollLabel);
        
        // JTextField rollText = new JTextField(); // Changed from JPasswordField
        // formPanel.add(rollText);
        
        // // 2. Create a panel for the button (to center it)
        // JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // JButton regBtn = new JButton("Register");
        // regBtn.setPreferredSize(new Dimension(100, 30)); // Give button a preferred size
        // buttonPanel.add(regBtn);

        // // 3. Add panels to the frame
        // f.add(formPanel, BorderLayout.CENTER);  // Form goes in the middle
        // f.add(buttonPanel, BorderLayout.SOUTH); // Button goes at the bottom

        // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // f.pack(); // Automatically sizes the window to fit components
        // f.setLocationRelativeTo(null); 
        // f.setVisible(true);
