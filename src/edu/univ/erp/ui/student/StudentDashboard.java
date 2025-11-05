package edu.univ.erp.ui.student;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentDashboard {

    private JFrame StudentDashboardFrame;

    public StudentDashboard() {
        StudentDashboardFrame = new JFrame("Student Dashboard");
        StudentDashboardFrame.setSize(800, 600); 
        StudentDashboardFrame.setLayout(null);
        StudentDashboardFrame.setLocationRelativeTo(null); 

        JLabel titleLabel = new JLabel("Student Dashboard");
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        titleLabel.setBounds(275, 30, 300, 30); 
        StudentDashboardFrame.add(titleLabel);

        JButton registrationBtn = new JButton("Course Catalog / Register");
        registrationBtn.setBounds(100, 120, 250, 100);
        StudentDashboardFrame.add(registrationBtn);

        JButton timetableBtn = new JButton("My Time Table");
        timetableBtn.setBounds(450, 120, 250, 100);
        StudentDashboardFrame.add(timetableBtn);

        JButton gradesBtn = new JButton("My Grades");
        gradesBtn.setBounds(100, 250, 250, 100);
        StudentDashboardFrame.add(gradesBtn);

        JButton transcriptBtn = new JButton("Download Transcript");
        transcriptBtn.setBounds(450, 250, 250, 100);
        StudentDashboardFrame.add(transcriptBtn);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(450, 400, 250, 40); 
        StudentDashboardFrame.add(logoutBtn);

        JButton changePassBtn = new JButton("Change Password");
        changePassBtn.setBounds(100, 400, 250, 40); 
        StudentDashboardFrame.add(changePassBtn);

        StudentDashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        StudentDashboardFrame.setVisible(true);

        // --- Action Listeners ---
        registrationBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StudentDashboardFrame.dispose();
                new RegisterFrame();
            }
        });

        timetableBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StudentDashboardFrame.dispose();
                new timetableFrame();
            }
        });

        gradesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StudentDashboardFrame.dispose();
                new gradeFrame();
            }
        });

        transcriptBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StudentDashboardFrame.dispose();
                new transFrame();
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(StudentDashboardFrame, "Logged out.");
            }
        });
    }
}