package edu.univ.erp.ui.student;

import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;

import edu.univ.erp.util.modeOps;
import edu.univ.erp.util.BREATHEFONT;

public class studentDashboard {
    public studentDashboard(String username, String role, String in_pass, String roll_no) {
        Font breatheFont = BREATHEFONT.fontGen();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to make FlatLaf", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Failed to make FlatLaf");
        }
        JFrame f = new JFrame();
        f.setSize(800, 600); 

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setBackground(Color.decode("#051072")); 
        JLabel l0 = new JLabel("STUDENT DASHBOARD");
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80));
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE ----
        JPanel p2 = new JPanel();
        p2.setLayout(new GridBagLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        p2.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        Dimension buttonSize = new Dimension(280, 80);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0; gbc.gridy = 0;
        JButton b1 = new JButton("Register");
        b1.setPreferredSize(buttonSize);
        b1.setMargin(new Insets(10, 30, 5, 30));
        b1.setBackground(Color.decode("#2f77b1")); 
        b1.setForeground(Color.WHITE); 
        b1.setFont(new Font("Gabarito BLACK", Font.PLAIN, 21));
        p2.add(b1, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        JButton b2 = new JButton("My Time Table");
        b2.setPreferredSize(buttonSize);
        b2.setMargin(new Insets(10, 30, 5, 30));
        b2.setBackground(Color.decode("#2f77b1")); 
        b2.setForeground(Color.WHITE); 
        b2.setFont(new Font("Gabarito Black", Font.PLAIN, 21));
        p2.add(b2, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JButton b3 = new JButton("My Grades");
        b3.setPreferredSize(buttonSize);
        b3.setMargin(new Insets(10, 30, 5, 30));
        b3.setBackground(Color.decode("#2f77b1")); 
        b3.setForeground(Color.WHITE); 
        b3.setFont(new Font("Gabarito Black", Font.PLAIN, 21));
        p2.add(b3, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        JButton b4 = new JButton("Download Transcript");
        b4.setPreferredSize(buttonSize);
        b4.setMargin(new Insets(10, 30, 5, 30));
        b4.setBackground(Color.decode("#2f77b1")); 
        b4.setForeground(Color.WHITE); 
        b4.setFont(new Font("Gabarito Black", Font.PLAIN, 21));
        p2.add(b4, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JButton b6 = new JButton("Course Catalog");
        b6.setPreferredSize(buttonSize);
        b6.setMargin(new Insets(10, 30, 5, 30));
        b6.setBackground(Color.decode("#2f77b1")); 
        b6.setForeground(Color.WHITE); 
        b6.setFont(new Font("Gabarito Black", Font.PLAIN, 21));
        p2.add(b6, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        JButton b7 = new JButton("Drop Course");
        b7.setPreferredSize(buttonSize);
        b7.setMargin(new Insets(10, 30, 5, 30));
        b7.setBackground(Color.decode("#2f77b1")); 
        b7.setForeground(Color.WHITE); 
        b7.setFont(new Font("Gabarito Black", Font.PLAIN, 21));
        p2.add(b7, gbc);
        f.add(p2, BorderLayout.CENTER);

        JPanel p3 = new JPanel();
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        JButton b5 = new JButton("Logout");
        b5.setBackground(Color.decode("#2f77b1")); 
        b5.setForeground(Color.WHITE); 
        b5.setFont(breatheFont.deriveFont(Font.PLAIN, 35f));
        b5.setMargin(new Insets(10, 30, 5, 30));
        p3.add(b5);
        f.add(p3, BorderLayout.SOUTH);

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