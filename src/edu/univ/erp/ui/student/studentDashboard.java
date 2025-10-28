package edu.univ.erp.ui.student;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class studentDashboard {

    // Make the frame a member variable so action listeners can access it
    private JFrame dashboardFrame;

    public static void main(String[] args) {
        // Run all Swing UIs on the Event Dispatch Thread (EDT) for safety
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new studentDashboard(); // Create an instance of the dashboard
            }
        });
    }

    // Constructor - this is where the frame is built
    public studentDashboard() {
        dashboardFrame = new JFrame("Student Dashboard");
        dashboardFrame.setSize(800, 600); // Made the frame larger
        dashboardFrame.setLayout(null);
        dashboardFrame.setLocationRelativeTo(null); // Center the window

        // --- Title Label ---
        JLabel titleLabel = new JLabel("Student Dashboard");
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        titleLabel.setBounds(275, 30, 300, 30); // Centered
        dashboardFrame.add(titleLabel);

        // --- Buttons (with corrected, visible bounds) ---

        // Based on the project spec, "Course Catalog" and "Register" are
        // part of the same task, so I combined them.
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
        logoutBtn.setBounds(325, 400, 150, 40);
        dashboardFrame.add(logoutBtn);

        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setVisible(true);

        // --- Action Listeners ---
        // Each button closes this dashboard and opens the new frame

        registrationBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RegistrationFrame(); // Open the new frame
                dashboardFrame.dispose(); // Close this one
            }
        });

        timetableBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TimetableFrame();
                dashboardFrame.dispose();
            }
        });

        gradesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GradesFrame();
                dashboardFrame.dispose();
            }
        });

        transcriptBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TranscriptFrame();
                dashboardFrame.dispose();
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Assuming you have a login frame to return to
                // new LoginForm(); 
                JOptionPane.showMessageDialog(dashboardFrame, "Logged out.");
                dashboardFrame.dispose();
            }
        });
    }
}
