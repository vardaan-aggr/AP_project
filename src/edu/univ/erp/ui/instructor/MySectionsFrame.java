package edu.univ.erp.ui.instructor;

import javax.swing.*;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class MySectionsFrame {
    private JFrame frame;
    private JTable sectionsTable;

    public MySectionsFrame() {
        frame = new JFrame("My Sections");
        frame.setSize(600, 400);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Table setup
        sectionsTable = new JTable();
 
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBounds(200, 300, 200, 30);
        frame.add(backButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new instructorDashboard();
                frame.dispose();
            }
        });

    }

}
