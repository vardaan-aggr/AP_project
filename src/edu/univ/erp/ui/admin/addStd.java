package edu.univ.erp.ui.admin;

import javax.swing.*;

import edu.univ.erp.util.HashGenerator;

import edu.univ.erp.data.DatabaseConnector;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class addStd {
    public addStd(int rollNo) {
        JFrame f = new JFrame();
        f.setSize(800, 600);
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));

        JLabel l0 = new JLabel("Add student");
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

        JLabel l4 = new JLabel("Password: ");
        l4.setBounds(50, 130, 100, 30);
        f.add(l4);
        JTextField t4 = new JTextField(50);
        t4.setBounds(150, 130, 250, 30);
        f.add(t4);
        
        JLabel l2 = new JLabel("Program: ");
        l2.setBounds(50, 180, 100, 30);
        f.add(l2);
        JTextField t2 = new JTextField(50);
        t2.setBounds(150, 180, 250, 30);
        f.add(t2);

        JLabel l3 = new JLabel("Year: ");
        l3.setBounds(50, 230, 100, 30);
        f.add(l3);
        JTextField t3 = new JTextField(50);
        t3.setBounds(150, 230, 250, 30);
        f.add(t3);

        JButton b1 = new JButton("Register");
        b1.setBounds(150, 280, 100, 30);
        b1.setBackground(Color.decode("#2f77b1")); 
        b1.setForeground(Color.WHITE); 
        b1.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b1);

        JButton b2 = new JButton("Back");
        b2.setBounds(150, 350, 100, 30);
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
                int rollNoTemp = -1;
                String hash_pass = HashGenerator.makeHash(t4.getText());
                try (Connection connection = DatabaseConnector.getAuthConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("""
                                INSERT INTO auth_table (username, role, hash_password) VALUES
                                    (?, ?, ?)
                            """, Statement.RETURN_GENERATED_KEYS)) {
                        statement.setString(1, t1.getText());
                        statement.setString(2, "student");
                        statement.setString(3, hash_pass);
                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
                             try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    rollNoTemp = generatedKeys.getInt(1);
                                    JOptionPane.showMessageDialog(null, "Sucessfully added ", "Sucess", JOptionPane.INFORMATION_MESSAGE);
                                    System.out.println("Student added successfully in Auth with roll no: " + rollNoTemp);
                                } else {
                                    // the insert worked, but we couldn't get the new ID.
                                    JOptionPane.showMessageDialog(null, "Failed to retrieve new student's ID", "Error", JOptionPane.ERROR_MESSAGE);
                                    System.out.println("Failed to get generated key for new student.");
                                    return; 
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to add student info in database.", "Error", JOptionPane.ERROR_MESSAGE);
                            System.out.println("Failed to add student info in database.");
                            return;
                        }
                    } 
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database Error (Auth): " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                    return;
                }

                if (rollNoTemp != -1) {
                    try (Connection connection = DatabaseConnector.getErpConnection()) {
                        try (PreparedStatement statement = connection.prepareStatement("""
                                    INSERT INTO students (roll_no, program, year) VALUES
                                        (?, ?, ?)
                                """)) {
                            statement.setString(1, String.valueOf(rollNoTemp));
                            statement.setString(2, t2.getText());
                            statement.setString(3, t3.getText());
                            int rowsAffected = statement.executeUpdate();
                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(null, "Added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                                System.out.println("Student added successfully in Erp db");
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to add student to ERP database", "Error", JOptionPane.ERROR_MESSAGE);
                                System.out.println("Failed to add student in Erp");
                                return;
                            }
                        } 
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Database Error (ERP): " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                        return;
                    }
                } else {
                    System.out.println("Skipping ERP insert because auth insert failed.");
                    return;
                }
                System.out.println(" to Admin Dashboard..");
                new adminDashboard(rollNo);
                f.dispose();
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(" to Admin Dashboard..");
                new adminDashboard(rollNo);
                f.dispose();
            }
        });
    }
}
