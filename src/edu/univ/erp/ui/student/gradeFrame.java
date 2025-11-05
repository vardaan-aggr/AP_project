package edu.univ.erp.ui.student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List; // <-- IMPORT ADDED

public class gradeFrame {

    private JFrame frame;
    private JTable gradesTable; // <-- ADDED
    private DefaultTableModel gradesModel; // <-- ADDED

    public gradeFrame() {

        frame = new JFrame("My Grades");
        frame.setSize(600, 400);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("My Grades");
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        titleLabel.setBounds(250, 20, 200, 20);
        frame.add(titleLabel);

        // --- DYNAMIC GRADES TABLE ---
        String[] columns = {"Course", "Assessment", "Score", "Final Grade"};
        
        // Make table non-editable
        gradesModel = new DefaultTableModel(null, columns) {
             @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        gradesTable = new JTable(gradesModel);
        
        JScrollPane scrollPane = new JScrollPane(gradesTable);
        scrollPane.setBounds(20, 50, 540, 250);
        frame.add(scrollPane);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBounds(225, 320, 150, 30);
        frame.add(backButton);

        frame.setVisible(true);

        // Action Listener
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new studentDashboard(); 
                frame.dispose(); 
            }
        });
    }

}