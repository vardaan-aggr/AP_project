package edu.univ.erp.ui.instructor;

import javax.swing.*;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;  
import edu.univ.erp.ui.common.AllCourses;
import java.awt.Color;
import java.awt.Font;

import edu.univ.erp.util.modeOps;

public class InstructorDashboard {
    
    private JFrame dashboardFrame; 

    public InstructorDashboard(String roll_no) {
        dashboardFrame = new JFrame("Instructor Dashboard");
        dashboardFrame.setSize(800, 600); // Made height taller
        dashboardFrame.setLayout(null);
        dashboardFrame.getContentPane().setBackground(Color.decode("#d8d0c1"));
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        dashboardFrame.setVisible(true);

        JLabel l0 = new JLabel("INSTRUCTOR DASHBOARD");
        l0.setBounds(0, 0, 800, 60);
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        dashboardFrame.add(l0);

        // --- BUTTONS ---
        JButton mySections = new JButton("My Sections");
        mySections.setBounds(100, 110, 600, 50); 
        mySections.setBackground(Color.decode("#2f77b1"));
        mySections.setForeground(Color.WHITE);
        mySections.setFont(new Font("Arial", Font.BOLD, 14));
        dashboardFrame.add(mySections);

        JButton computefinal = new JButton("Compute Final Grades");
        computefinal.setBounds(100, 180, 600, 50); 
        computefinal.setBackground(Color.decode("#2f77b1"));
        computefinal.setForeground(Color.WHITE);
        computefinal.setFont(new Font("Arial", Font.BOLD, 14));
        dashboardFrame.add(computefinal);
    
        JButton classStats = new JButton(" Class Statistics");
        classStats.setBounds(100, 250, 600, 50); 
        classStats.setBackground(Color.decode("#2f77b1"));
        classStats.setForeground(Color.WHITE);
        classStats.setFont(new Font("Arial", Font.BOLD, 14));
        dashboardFrame.add(classStats);
        
        // --- NEW BUTTON ADDED ---
        JButton viewcourses = new JButton("Courses/Sections");
        viewcourses.setBounds(100, 320, 600, 50);
        viewcourses.setBackground(Color.decode("#2f77b1"));
        viewcourses.setForeground(Color.WHITE);
        viewcourses.setFont(new Font("Arial", Font.BOLD, 14));
        dashboardFrame.add(viewcourses);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(325, 420, 150, 45); 
        logoutBtn.setBackground(Color.decode("#2f77b1"));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
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
                if (modeOps.getMaintainMode().equals("true")) {
                    JOptionPane.showMessageDialog(null, "Cannot change, Maintainance mode is on.", "FYI", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("\tCouldn't open bcz maintainance mode is on.");
                } else if (modeOps.getMaintainMode().equals("failure")) {
                    ;
                } else {
                dashboardFrame.dispose();
                new ComputeFinalFrame(roll_no);
                }
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