package edu.univ.erp.ui.admin;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import edu.univ.erp.data.DatabaseConnector;
import edu.univ.erp.util.HashGenerator;

public class addAdm {
    public addAdm(int rollNo) {
        JFrame f = new JFrame();
        f.setSize(800, 600);
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));

        JLabel l0 = new JLabel("Add Admin");
        l0.setBounds(0, 0, 800, 50);
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#dbd3c5"));
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
        JTextField t2 = new JTextField(50);
        t2.setBounds(150, 130, 250, 30);
        f.add(t2);

        JButton b1 = new JButton("Register");
        b1.setBounds(150, 190, 100, 30);
        b1.setBackground(Color.decode("#2f77b1")); 
        b1.setForeground(Color.WHITE); 
        b1.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b1);

        JButton b2 = new JButton("Back");
        b2.setBounds(260, 190, 100, 30);
        b2.setBackground(Color.decode("#2f77b1")); 
        b2.setForeground(Color.WHITE); 
        b2.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b2);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // ---- Action Listeners ----
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String hash_pass = HashGenerator.makeHash(t2.getText());
                try (Connection connection = DatabaseConnector.getAuthConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("""
                                INSERT INTO auth_table (username, role, hash_password) VALUES
                                    (?, ?, ?)
                            """)) {
                        statement.setString(1, t1.getText());
                        statement.setString(2, "admin");
                        statement.setString(3, hash_pass);
                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Admin added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                            System.out.println("Admin added successfully..");
                        } else {
                            JOptionPane.showMessageDialog(null, "Error: Failed to add info into auth db!", "Error", JOptionPane.ERROR_MESSAGE);
                            System.out.println("Error: Failed to add info into auth db");
                            return;
                        }
                    } 
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Failed to add info into auth db!", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Error: Failed to add info into auth db");
                    ex.printStackTrace();
                    return;
                }
                System.out.println("\tGoing back to Admin Dashboard..");
                new adminDashboard(rollNo);
                f.dispose();
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tGoing back to Admin Dashboard..");
                new adminDashboard(rollNo);
                f.dispose();
            }
        });    
    }
}
