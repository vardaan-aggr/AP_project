package edu.univ.erp.ui.instructor;

import javax.swing.*;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;  

public class instructorDashboard {
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();

        JButton mySections = new JButton("My Sections");
        mySections.setBounds(0, 100, 220, 50);
        frame.add(mySections);

        JButton gradeBook = new JButton("Grade Book");
        gradeBook.setBounds(300, 100, 220, 50);
        frame.add(gradeBook);
    
        JButton classStats = new JButton("Class Stats");
        classStats.setBounds(0, 500, 220, 50);
        frame.add(classStats);

        frame.setSize(500, 600);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // actionlisteners
        mySections.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        gradeBook.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        classStats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}