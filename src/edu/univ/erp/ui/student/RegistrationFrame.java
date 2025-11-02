package edu.univ.erp.ui.student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List; // Import List

// Import your service and domain classes
import edu.univ.erp.service.studentService;
import edu.univ.erp.domain.Course;

public class RegistrationFrame {

    private JFrame frame;
    private studentService service; // The "brain"
    private JTable catalogTable;
    private DefaultTableModel catalogModel;
    
    // You'll need these later for the other table
    // private JTable mySectionsTable;
    // private DefaultTableModel mySectionsModel;

    public RegistrationFrame() {
        this.service = new studentService(); // Create the service

        frame = new JFrame("Course Registration");
        frame.setSize(800, 600);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

        // --- 1. Course Catalog (for Registering) ---
        JLabel catalogLabel = new JLabel("Available Courses (Course Catalog)");
        catalogLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        catalogLabel.setBounds(20, 20, 300, 20);
        frame.add(catalogLabel);

        // --- REAL TABLE SETUP ---
        String[] catalogCols = {"Code", "Title", "Credits"};
        // Create a model, but with 0 rows
        catalogModel = new DefaultTableModel(null, catalogCols);
        catalogTable = new JTable(catalogModel);
        
        JScrollPane catalogScrollPane = new JScrollPane(catalogTable);
        catalogScrollPane.setBounds(20, 50, 740, 200);
        frame.add(catalogScrollPane);
        
        // --- End of table setup ---

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
        // Create an empty model for now
        DefaultTableModel mySectionsModel = new DefaultTableModel(null, mySectionsCols);
        JTable mySectionsTable = new JTable(mySectionsModel);
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
                new studentDashboard(); 
                frame.dispose(); 
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ... (Logic for this button will be your next step) ...
                JOptionPane.showMessageDialog(frame, "Register button clicked!");
            }
        });

        dropButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ... (Logic for this button will be your next step) ...
                JOptionPane.showMessageDialog(frame, "Drop button clicked!");
            }
        });

        // --- FINALLY, LOAD THE DATA ---
        loadCourseCatalog();
    }

    /**
     * A new method to fetch data from the service
     * and load it into the JTable.
     */
    private void loadCourseCatalog() {
        // 1. Get data from the "brain"
        List<Course> courseList = service.getCourseCatalog();
        
        // 2. Clear any old data from the table
        catalogModel.setRowCount(0); 
        
        // 3. Add each course to the table model
        for (Course course : courseList) {
            Object[] row = new Object[3];
            row[0] = course.getCode();
            row[1] = course.getTitle();
            row[2] = course.getCredits();
            catalogModel.addRow(row);
        }
    }
}