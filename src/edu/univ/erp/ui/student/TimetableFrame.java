package edu.univ.erp.ui.student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimetableFrame {

    private JFrame frame;

    public TimetableFrame() {
        frame = new JFrame("My Weekly Timetable");
        frame.setSize(800, 400);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("My Weekly Timetable");
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        titleLabel.setBounds(300, 20, 200, 20);
        frame.add(titleLabel);

        // Timetable Grid
        String[] columns = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        Object[][] data = {
            {"09:00 - 10:00", "", "", "MATH101", "", ""},
            {"10:00 - 11:00", "CS101", "", "", "CS101", ""},
            {"11:00 - 12:00", "", "PHY101", "", "", "PHY101"},
            // You will build this data by calling your service
        };

        JTable timetableTable = new JTable(new DefaultTableModel(data, columns));
        timetableTable.setRowHeight(40);
        JScrollPane scrollPane = new JScrollPane(timetableTable);
        scrollPane.setBounds(20, 50, 740, 250);
        frame.add(scrollPane);

        // Navigation
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBounds(325, 320, 150, 30);
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

