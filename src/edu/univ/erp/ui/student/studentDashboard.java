package edu.univ.erp.ui.student;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import edu.univ.erp.util.modeOps;

public class studentDashboard {
    public studentDashboard(String username, String role, String in_pass, String roll_no) {
        JFrame f = new JFrame();
        f.setSize(800, 600); 
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));

        JLabel l0 = new JLabel("STUDENT DASHBOARD");
        l0.setBounds(0, 0, 800, 60);  
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);

        JButton b1 = new JButton("Register");
        b1.setBounds(100, 100, 250, 100);
        b1.setBackground(Color.decode("#2f77b1")); 
        b1.setForeground(Color.WHITE); 
        b1.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b1);

        JButton b2 = new JButton("My Time Table");
        b2.setBounds(450, 100, 250, 100);
        b2.setBackground(Color.decode("#2f77b1")); 
        b2.setForeground(Color.WHITE); 
        b2.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b2);

        JButton b3 = new JButton("My Grades");
        b3.setBounds(100, 240, 250, 100);
        b3.setBackground(Color.decode("#2f77b1")); 
        b3.setForeground(Color.WHITE); 
        b3.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b3);

        JButton b4 = new JButton("Download Transcript");
        b4.setBounds(450, 240, 250, 100);
        b4.setBackground(Color.decode("#2f77b1")); 
        b4.setForeground(Color.WHITE); 
        b4.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b4);

        JButton b6 = new JButton("Course Catalog");
        b6.setBounds(100, 380, 250, 100); 
        b6.setBackground(Color.decode("#2f77b1")); 
        b6.setForeground(Color.WHITE); 
        b6.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b6);

        JButton b5 = new JButton("Logout");
        b5.setBounds(600, 500, 120, 40); 
        b5.setBackground(Color.decode("#2f77b1")); 
        b5.setForeground(Color.WHITE); 
        b5.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b5);

        JButton b7 = new JButton("Drop Course");
        b7.setBounds(450, 380, 250, 100); 
        b7.setBackground(Color.decode("#2f77b1")); 
        b7.setForeground(Color.WHITE); 
        b7.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b7);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.setLocationRelativeTo(null); 
        f.setVisible(true);
        
        // --- Action Listeners ---
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (modeOps.getMaintainMode().equals("true")) {
                    JOptionPane.showMessageDialog(null, "Cannot change, Maintainance mode is on.", "FYI", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("\tCouldn't open bcz maintainance mode is on.");
                } else if (modeOps.getMaintainMode().equals("failure")) {
                    ;
                } else {
                    System.out.println("\tOpening Registration Portal..");
                    new registerFrame(username, role, in_pass, roll_no);
                    f.dispose();
                }
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tOpening timetable..");
                new timetableFrame(username, role, in_pass, roll_no);
                f.dispose();
            }
        });

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tOpening grades..");
                new gradeFrame(username, role, in_pass, roll_no);
                f.dispose();
            }
        });

        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("\tDownloading transcript..");
                    new transcriptFrame(username, role, in_pass, roll_no);
                    f.dispose();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error opening transcript: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        b5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Logging out..");
                System.out.println("\nLogged out successfully...");
                f.dispose();
            }
        });

        b6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tOpening course catalog..");
                new courseCatalogFrame(username, role, in_pass, roll_no);
                f.dispose();
            }
        });

        b7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (modeOps.getMaintainMode().equals("true")) {
                    JOptionPane.showMessageDialog(null, "Cannot change, Maintainance mode is on.", "FYI", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("\tCouldn't open bcz maintainance mode is on.");
                } else if (modeOps.getMaintainMode().equals("failure")) {
                    ;
                } else {
                System.out.println("\tOpening Drop Course Portal..");
                new dropCourseFrame(username, role, in_pass, roll_no);
                f.dispose();
                }
            }   
        });
    }
}