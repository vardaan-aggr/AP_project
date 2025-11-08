package edu.univ.erp.ui.student;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class studentDashboard {
    public studentDashboard(String username, String role, String in_pass, String roll_no) {
        JFrame f = new JFrame();
        f.setSize(800, 600); 
        f.setLayout(null);

        JLabel l1 = new JLabel("Student Dashboard");
        l1.setBounds(275, 30, 300, 30); 
        f.add(l1);

        JButton b1 = new JButton("Course Catalog / Register");
        b1.setBounds(100, 120, 250, 100);
        f.add(b1);

        JButton b2 = new JButton("My Time Table");
        b2.setBounds(450, 120, 250, 100);
        f.add(b2);

        JButton b3 = new JButton("My Grades");
        b3.setBounds(100, 250, 250, 100);
        f.add(b3);

        JButton b4 = new JButton("Download Transcript");
        b4.setBounds(450, 250, 250, 100);
        f.add(b4);
        
        JButton b5 = new JButton("Logout");
        b5.setBounds(450, 400, 250, 40); 
        f.add(b5);

        JButton b6 = new JButton("Change Password");
        b6.setBounds(100, 400, 250, 40); 
        f.add(b6);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.setLocationRelativeTo(null); 
        f.setVisible(true);

        // --- Action Listeners ---
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                new registerFrame(username, role, in_pass, roll_no);
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                new timetableFrame(roll_no);
            }
        });

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                new gradeFrame(roll_no);
            }
        });

        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                new transcriptFrame();
            }
        });

        b5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(f, "Logged out.");
            }
        });
    }
}