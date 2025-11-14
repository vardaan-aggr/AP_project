package edu.univ.erp.ui.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.*;

import edu.univ.erp.data.DatabaseConnector;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class dropCourseFrame {
    public dropCourseFrame(String username, String role, String in_pass, String roll_no) {
        JFrame f = new JFrame();
        f.setSize(800, 600);
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));

        JLabel l0 = new JLabel("Drop Course");
        l0.setBounds(0, 0, 800, 60);
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#d8d0c4"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);

        JLabel l1 = new JLabel("Course code: ");
        l1.setBounds(145, 150, 100, 30);
        f.add(l1);

        JTextField t1 = new JTextField(50);
        t1.setBounds(255, 150, 400, 30);
        f.add(t1);

        // JLabel l2 = new JLabel("Section: ");
        // l2.setBounds(145, 220, 100, 30);
        // f.add(l2);

        // JTextField t2 = new JTextField();
        // t2.setBounds(255, 220, 400, 30);
        // f.add(t2);

        // JLabel l3 = new JLabel("Roll num: ");
        // l3.setBounds(145, 290, 100, 30);
        // f.add(l3);

        // JTextField t3 = new JTextField();
        // t3.setBounds(255, 290, 400, 30);
        // f.add(t3);

        JButton b1 = new JButton("Drop");
        b1.setBounds(270, 400, 120, 40); 
        b1.setBackground(Color.decode("#2f77b1"));
        b1.setForeground(Color.WHITE);
        b1.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b1);

        JButton b2 = new JButton("Back");
        b2.setBounds(410, 400, 120, 40); 
        b2.setBackground(Color.decode("#2f77b1"));
        b2.setForeground(Color.WHITE);
        b2.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b2);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // --- Action Listeners ---
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DatabaseConnector.getErpConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("""
                                delete from enrollments where roll_no = ? and course_code = ?;
                            """)) {
                        statement.setString(1, roll_no);
                        statement.setString(2, t1.getText());
                        int rowsDeleted = statement.executeUpdate();
                        if (rowsDeleted == 0) {
                            JOptionPane.showMessageDialog(null, "Error: Couldn't Drop in database.", "Error", JOptionPane.ERROR_MESSAGE);
                            System.out.println("Error: Couldn't Drop in database.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Course dropped successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            System.out.println("Course droped successfully.");
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error Droping: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                new studentDashboard(username, role, in_pass, roll_no);
                f.dispose();
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Going back to Student Dashboard..");
                new studentDashboard(username, role, in_pass, roll_no);
                f.dispose();
            }
        });
    }
}