package edu.univ.erp.ui.student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GradesFrame {

    private JFrame frame;

    public GradesFrame() {
        frame = new JFrame("My Grades");
        frame.setSize(600, 400);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("My Grades");
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        titleLabel.setBounds(250, 20, 200, 20);
        frame.add(titleLabel);

        // Grades Table
        String[] columns = {"Course", "Assessment", "Score", "Final Grade"};
        Object[][] data = {
            {"CS101", "Quiz 1", "85/100", ""},
            {"CS101", "Midterm", "75/100", ""},
            {"CS101", "Final Exam", "90/100", "A-"},
            {"MATH101", "Midterm", "92/100", ""},
            {"MATH101", "Final Exam", "88/100", "A"},
            // Load this from your service
        };

        JTable gradesTable = new JTable(new DefaultTableModel(data, columns));
        JScrollPane scrollPane = new JScrollPane(gradesTable);
        scrollPane.setBounds(20, 50, 540, 250);
        frame.add(scrollPane);

        // Navigation
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBounds(225, 320, 150, 30);
        frame.add(backButton);

        frame.setVisible(true);

        // Action Listener
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new studentDashboard(); // Open the dashboard
                frame.dispose(); // Close this window
            }
        });
    }
}

