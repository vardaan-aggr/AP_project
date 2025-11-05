package edu.univ.erp.ui.student;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class studentDashboard {

    private JFrame dashboardFrame;

    public studentDashboard() {
        dashboardFrame = new JFrame("Student Dashboard");
        dashboardFrame.setSize(800, 600); 
        dashboardFrame.setLayout(null);
        dashboardFrame.setLocationRelativeTo(null); 

        JLabel titleLabel = new JLabel("Student Dashboard");
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        titleLabel.setBounds(275, 30, 300, 30); 
        dashboardFrame.add(titleLabel);

        JButton registrationBtn = new JButton("Course Catalog / Register");
        registrationBtn.setBounds(100, 120, 250, 100);
        dashboardFrame.add(registrationBtn);

        JButton timetableBtn = new JButton("My Time Table");
        timetableBtn.setBounds(450, 120, 250, 100);
        dashboardFrame.add(timetableBtn);

        JButton gradesBtn = new JButton("My Grades");
        gradesBtn.setBounds(100, 250, 250, 100);
        dashboardFrame.add(gradesBtn);

        JButton transcriptBtn = new JButton("Download Transcript");
        transcriptBtn.setBounds(450, 250, 250, 100);
        dashboardFrame.add(transcriptBtn);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(450, 400, 250, 40); 
        dashboardFrame.add(logoutBtn);

        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        dashboardFrame.setVisible(true);

        // --- Action Listeners ---
        registrationBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        timetableBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        gradesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        transcriptBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(dashboardFrame, "Logged out.");
            }
        });
    }
}