package edu.univ.erp.ui.student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationFrame {

    private JFrame frame;

    public RegistrationFrame() {
        frame = new JFrame("Course Registration");
        frame.setSize(800, 600);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        // Use DISPOSE_ON_CLOSE so it only closes this window, not the whole app
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

        // --- 1. Course Catalog (for Registering) ---
        JLabel catalogLabel = new JLabel("Available Courses (Course Catalog)");
        catalogLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        catalogLabel.setBounds(20, 20, 300, 20);
        frame.add(catalogLabel);

        // Table for course catalog
        String[] catalogCols = {"Code", "Title", "Credits", "Capacity", "Instructor"};
        Object[][] catalogData = {
            {"CS101", "Intro to CS", 3, 50, "Dr. Turing"},
            {"MATH101", "Calculus I", 4, 60, "Dr. Newton"},
            {"PHY101", "Physics I", 4, 40, "Dr. Curie"},
            // In a real app, you load this from your 'service' layer
        };
        JTable catalogTable = new JTable(new DefaultTableModel(catalogData, catalogCols));
        JScrollPane catalogScrollPane = new JScrollPane(catalogTable);
        catalogScrollPane.setBounds(20, 50, 740, 200);
        frame.add(catalogScrollPane);

        JButton registerButton = new JButton("Register for Selected Section");
        registerButton.setBounds(20, 260, 250, 30);
        frame.add(registerButton);

        // --- 2. My Sections (for Dropping) ---
        JLabel mySectionsLabel = new JLabel("My Registered Sections");
        mySectionsLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        mySectionsLabel.setBounds(20, 310, 300, 20);
        frame.add(mySectionsLabel);

        // Table for registered sections
        String[] mySectionsCols = {"Code", "Title", "Instructor", "Time/Room"};
        Object[][] mySectionsData = {
            {"CS101", "Intro to CS", "Dr. Turing", "Mon 10:00"},
            // Load this from your service layer
        };
        JTable mySectionsTable = new JTable(new DefaultTableModel(mySectionsData, mySectionsCols));
        JScrollPane mySectionsScrollPane = new JScrollPane(mySectionsTable);
        mySectionsScrollPane.setBounds(20, 340, 740, 150);
        frame.add(mySectionsScrollPane);
        
        JButton dropButton = new JButton("Drop Selected Section");
        dropButton.setBounds(20, 500, 250, 30);
        frame.add(dropButton);

        // --- 3. Navigation ---
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBounds(610, 500, 150, 30);
        frame.add(backButton);

        frame.setVisible(true);

        // --- Action Listeners ---
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new studentDashboard(); // Open the dashboard
                frame.dispose(); // Close this window
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 1. Get selected row from catalogTable
                int selectedRow = catalogTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select a course to register.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // 2. Call your "service.register(...)" method
                // 3. Show success/error based on spec (e.g., "Section full", "duplicate")
                JOptionPane.showMessageDialog(frame, "Registration Successful! (Mockup)");
                // 4. Reload "My Registered Sections" table
            }
        });

        dropButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 1. Get selected row from mySectionsTable
                int selectedRow = mySectionsTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select a section to drop.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // 2. Call your "service.drop(...)" method
                JOptionPane.showMessageDialog(frame, "Section Dropped! (Mockup)");
                // 3. Reload "My Registered Sections" table
            }
        });
    }
}

