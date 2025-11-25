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

import edu.univ.erp.service.StudentService;
import edu.univ.erp.ui.common.catalog;
import edu.univ.erp.util.BREATHEFONT;

public class studentDashboard {
    public studentDashboard(String username, String role, String in_pass, String roll_no) {

        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen();

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

        // Register
        gbc.gridx = 0; gbc.gridy = 0;
        JButton bRegister = new JButton("Register");
        bRegister.setPreferredSize(buttonSize);
        bRegister.setMargin(new Insets(10, 30, 5, 30));
        bRegister.setBackground(Color.decode("#2f77b1")); 
        bRegister.setForeground(Color.WHITE); 
        bRegister.setFont(gFont.deriveFont(Font.PLAIN, 21f));
        p2.add(bRegister, gbc);

        // My timetable
        gbc.gridx = 1; gbc.gridy = 0;
        JButton bTimetable = new JButton("My Time Table");
        bTimetable.setPreferredSize(buttonSize);
        bTimetable.setMargin(new Insets(10, 30, 5, 30));
        bTimetable.setBackground(Color.decode("#2f77b1")); 
        bTimetable.setForeground(Color.WHITE); 
        bTimetable.setFont(gFont.deriveFont(Font.PLAIN, 21f));
        p2.add(bTimetable, gbc);

        // My Grades
        gbc.gridx = 0; gbc.gridy = 1;
        JButton bGrades = new JButton("My Grades");
        bGrades.setPreferredSize(buttonSize);
        bGrades.setMargin(new Insets(10, 30, 5, 30));
        bGrades.setBackground(Color.decode("#2f77b1")); 
        bGrades.setForeground(Color.WHITE); 
        bGrades.setFont(gFont.deriveFont(Font.PLAIN, 21f));
        p2.add(bGrades, gbc);

        // Download transcript
        gbc.gridx = 1; gbc.gridy = 1;
        JButton bTranscript = new JButton("Download Transcript");
        bTranscript.setPreferredSize(buttonSize);
        bTranscript.setMargin(new Insets(10, 30, 5, 30));
        bTranscript.setBackground(Color.decode("#2f77b1")); 
        bTranscript.setForeground(Color.WHITE); 
        bTranscript.setFont(gFont.deriveFont(Font.PLAIN, 21f));
        p2.add(bTranscript, gbc);

        // Catalog
        gbc.gridx = 0; gbc.gridy = 2;
        JButton bCatalog = new JButton("Catalog");
        bCatalog.setPreferredSize(buttonSize);
        bCatalog.setMargin(new Insets(10, 30, 5, 30));
        bCatalog.setBackground(Color.decode("#2f77b1")); 
        bCatalog.setForeground(Color.WHITE); 
        bCatalog.setFont(gFont.deriveFont(Font.PLAIN, 21f));
        p2.add(bCatalog, gbc);

        // Drop Course
        gbc.gridx = 1; gbc.gridy = 2;
        JButton bDrop = new JButton("Drop Course");
        bDrop.setPreferredSize(buttonSize);
        bDrop.setMargin(new Insets(10, 30, 5, 30));
        bDrop.setBackground(Color.decode("#2f77b1")); 
        bDrop.setForeground(Color.WHITE); 
        bDrop.setFont(gFont.deriveFont(Font.PLAIN, 21f));
        p2.add(bDrop, gbc);
        f.add(p2, BorderLayout.CENTER);

        // --- NEW: Notifications Button ---
        gbc.gridx = 0; gbc.gridy = 3; 
        gbc.gridwidth = 2; 
        JButton bNotify = new JButton("Notifications");
        bNotify.setPreferredSize(buttonSize);
        bNotify.setMargin(new Insets(10, 30, 5, 30));
        bNotify.setBackground(Color.decode("#2f77b1")); 
        bNotify.setForeground(Color.WHITE); 
        bNotify.setFont(gFont.deriveFont(Font.PLAIN, 21f));
        p2.add(bNotify, gbc);

        // Logout 
        JPanel p3 = new JPanel();
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        JButton bLogout = new JButton("Logout");
        bLogout.setBackground(Color.decode("#2f77b1")); 
        bLogout.setForeground(Color.WHITE); 
        bLogout.setFont(breatheFont.deriveFont(Font.BOLD, 35f));
        bLogout.setMargin(new Insets(10, 30, 5, 30));
        p3.add(bLogout);
        f.add(p3, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.setLocationRelativeTo(null); 
        f.setVisible(true);
        
        // --- Action Listeners ---
        bRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StudentService service = new StudentService();
                if (service.isSystemActive()) {
                    System.out.println("\tOpening Registration Portal..");
                    new registerFrame(username, role, in_pass, roll_no);
                    f.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot change, Maintenance mode is on.", "FYI", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bTimetable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tOpening timetable..");
                new timetableFrame(username, role, in_pass, roll_no);
                f.dispose();
            }
        });

        bGrades.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tOpening grades..");
                new gradeFrame(username, role, in_pass, roll_no);
                f.dispose();
            }
        });

        bTranscript.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tDownloading transcript..");
                new transcriptFrame(username, role, in_pass, roll_no);
                f.dispose();
            }
        });

        bLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Logging out..");
                System.out.println("\nLogged out successfully...");
                f.dispose();
            }
        });

        bCatalog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening Course Catalog...");
                new catalog(username, role, in_pass, roll_no);
                f.dispose();
            }
        });

        bDrop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StudentService service = new StudentService();

                if (service.isSystemActive()) {
                    System.out.println("\tOpening Drop Course Portal..");
                    new dropCourseFrame(username, role, in_pass, roll_no);
                    f.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot change, Maintenance mode is on.", "FYI", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bNotify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new edu.univ.erp.ui.common.NotificationDialog(f, roll_no);
            }
        });
    }
}