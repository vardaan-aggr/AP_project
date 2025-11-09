package edu.univ.erp.ui.instructor;

import javax.swing.*;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;  
import edu.univ.erp.ui.common.AllCourses;

public class InstructorDashboard {
    
    private JFrame dashboardFrame; 

    public InstructorDashboard(String roll_no) {
        dashboardFrame = new JFrame("Instructor Dashboard");
        dashboardFrame.setSize(500, 600); // Made height taller
        dashboardFrame.setLayout(null);
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        dashboardFrame.setVisible(true);

        // --- BUTTONS ---
        JButton mySections = new JButton("My Sections");
        mySections.setBounds(140, 80, 220, 50); // Moved up
        dashboardFrame.add(mySections);

        JButton computefinal = new JButton("Compute Final Grades");
        computefinal.setBounds(140, 160, 220, 50); // Moved up
        dashboardFrame.add(computefinal);
    
        JButton classStats = new JButton(" Class Statistics");
        classStats.setBounds(140, 240, 220, 50); // Moved up
        dashboardFrame.add(classStats);
        
        // --- NEW BUTTON ADDED ---
        JButton viewcourses = new JButton("Courses/Sections");
        viewcourses.setBounds(140, 320, 220, 50);
        dashboardFrame.add(viewcourses);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(185, 400, 130, 40); // Moved down
        dashboardFrame.add(logoutBtn);


        // --- ACTION LISTENERS ---
        mySections.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dashboardFrame.dispose(); 
                new MySectionsFrame(roll_no);
            }
        });
        
        computefinal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dashboardFrame.dispose();
                new ComputeFinalFrame(roll_no);
            }
        });
        
        classStats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dashboardFrame.dispose();
                new ClassStatisticsFrame(roll_no);
            }
        });

        // --- NEW ACTION LISTENER ---
        viewcourses.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dashboardFrame.dispose();
                new AllCourses(roll_no, "instructor");
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(dashboardFrame, "Logged out.");

                dashboardFrame.dispose();
            }
        });
    }
}