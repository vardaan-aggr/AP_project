package edu.univ.erp.ui.admin;

import javax.swing.*;

public class adminDashboard {

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();

        JButton userManagement = new JButton("User Management");
        userManagement.setBounds(0, 100, 220, 50);
        frame.add(userManagement);

        JButton courseManagement = new JButton("Course Management");
        courseManagement.setBounds(300, 100, 220, 50);
        frame.add(courseManagement);
    
        JButton instructorAssignment = new JButton("Instructor Assignment");
        instructorAssignment.setBounds(0, 500, 220, 50);
        frame.add(instructorAssignment);

        JButton maintenanceMode = new JButton("Maintenance Mode");
        maintenanceMode.setBounds(300, 500, 220, 50);
        frame.add(maintenanceMode);

        frame.setSize(500, 600);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}