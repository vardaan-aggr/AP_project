package edu.univ.erp.ui.admin;

import javax.swing.*;

import edu.univ.erp.util.HashGenerator;
import org.mindrot.jbcrypt.BCrypt;

import edu.univ.erp.data.DatabaseConnector;
import edu.univ.erp.ui.instructor.InstructorDashboard;
import edu.univ.erp.ui.student.studentDashboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class addStd {
    public addStd() {
        JFrame f = new JFrame();
        f.setSize(800, 600);
        f.setLayout(null);
        
        // roll num, program, yeear
        JLabel l1 = new JLabel("Username: ");
        l1.setBounds(50, 80, 100, 30);
        f.add(l1);
        JTextField t1 = new JTextField(50);
        t1.setBounds(150, 80, 250, 30);
        f.add(t1);

        JLabel l4 = new JLabel("Password: ");
        l4.setBounds(50, 80, 100, 30);
        f.add(l4);
        JTextField t4 = new JTextField(50);
        t4.setBounds(150, 80, 250, 30);
        f.add(t4);
        
        JLabel l2 = new JLabel("Program: ");
        l2.setBounds(50, 130, 100, 30);
        f.add(l2);
        JTextField t2 = new JTextField(50);
        t2.setBounds(150, 130, 250, 30);
        f.add(t2);

        JLabel l3 = new JLabel("Year: ");
        l3.setBounds(50, 130, 100, 30);
        f.add(l3);
        JTextField t3 = new JTextField(50);
        t3.setBounds(150, 180, 250, 30);
        f.add(t3);

        JButton b1 = new JButton("Register");
        b1.setBounds(150, 190, 100, 30);
        b1.setBackground(Color.decode("#2f77b1")); 
        b1.setForeground(Color.WHITE); 
        b1.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b1);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // ---- Action Listeners ----
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rollNo = -1;
                String hash_pass = HashGenerator.makeHash(t4.getText());
                try (Connection connection = DatabaseConnector.getAuthConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("""
                                INSERT INTO auth_table (username, role, hash_password) VALUES
                                    (?, ?, ?)
                            """)) {
                        statement.setString(1, t1.getText());
                        statement.setString(2, "Student");
                        statement.setString(3, hash_pass);
                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0) {
                             try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    rollNo = generatedKeys.getInt(1);
                                    System.out.println("Student added successfully in Auth with roll no: " + rollNo);
                                }
                            }
                        } else {
                            exit(1);
                            System.out.println("Failed to add student in Auth");
                        }
                    } 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                if (rollNo != -1) {
                    try (Connection connection = DatabaseConnector.getErpConnection()) {
                        try (PreparedStatement statement = connection.prepareStatement("""
                                    INSERT INTO students (roll_no, program, year) VALUES
                                        (?, ?, ?, ?)
                                """)) {
                            statement.setString(1, String.valueOf(rollNo));
                            statement.setString(2, t2.getText());
                            statement.setString(3, l3.getText());
                            int rowsAffected = statement.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Student added successfully in Erp db");
                            } else {
                                System.out.println("Failed to add student in Erp");
                            }
                        } 
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
}
