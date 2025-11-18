package edu.univ.erp.ui.admin;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import edu.univ.erp.data.DatabaseConnector;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class deleteSectionACourse {
    public deleteSectionACourse(String roll_no) { 

    // public static void main(String[] args) {

        JFrame f = new JFrame("Delete Section and Course");
        f.setSize(800, 600); 
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));
        
        JLabel l0 = new JLabel("DELETE SECTION & COURSE");
        l0.setBounds(0, 0, 800, 60); 
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#d8d0c4"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);
        
        JLabel l1 = new JLabel("Course code: ");
        l1.setBounds(145, 200, 100, 30);
        f.add(l1);
        JTextField t1 = new JTextField(50);
        t1.setBounds(275, 200, 350, 30);
        f.add(t1);
        
        JLabel l2 = new JLabel("Section: ");
        l2.setBounds(145, 280, 100, 30);
        f.add(l2);
        JTextField t2 = new JTextField();
        t2.setBounds(275, 280, 350, 30);
        f.add(t2);

        JButton deleteButton = new JButton("Delete Both");
        deleteButton.setBackground(Color.decode("#ec1414"));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBounds(190, 350, 150, 50); 
        f.add(deleteButton);

        JButton backButton = new JButton("<- BACK");
        backButton.setBackground(Color.decode("#2f77b1"));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBounds(440, 350, 150, 50); 
        f.add(backButton);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // --- Action Listeners ---
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String courseCode = t1.getText();
                String section = t2.getText();

                if (courseCode.isEmpty() || section.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both Course Code and Section.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(null, 
                    "Are you sure you want to delete " + courseCode + " section " + section + "?\nThis will remove it from both Courses and Sections tables.", 
                    "Confirm Delete", 
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try (Connection connection = DatabaseConnector.getErpConnection()) {
                        // 1. Delete from Sections table
                        try (PreparedStatement stmtSection = connection.prepareStatement(
                                "DELETE FROM sections WHERE course_code = ? AND section = ?")) {
                            stmtSection.setString(1, courseCode);
                            stmtSection.setString(2, section);
                            stmtSection.executeUpdate();
                        }

                        // 2. Delete from Courses table
                        try (PreparedStatement stmtCourse = connection.prepareStatement(
                                "DELETE FROM courses WHERE course_code = ? AND section = ?")) {
                            stmtCourse.setString(1, courseCode);
                            stmtCourse.setString(2, section);
                            int rowsAffected = stmtCourse.executeUpdate();

                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(null, "Course and Section deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Note: Section deleted (if existed), but Course not found in 'courses' table.", "Warning", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error deleting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                    new adminDashboard(roll_no)
                    f.dispose();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Going back...");
                new adminDashboard(roll_no);
                f.dispose();
            }
        });
    }
}