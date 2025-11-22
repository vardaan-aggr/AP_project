package edu.univ.erp.ui.common;
import edu.univ.erp.ui.admin.adminDashboard;
import edu.univ.erp.ui.student.studentDashboard;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.ui.instructor.InstructorDashboard;
import edu.univ.erp.util.BREATHEFONT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;

public class catalog {

    public catalog(String username, String role, String password, String roll_no) {

        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame("Catalog Menu");
        f.setSize(800, 600);
        f.setLayout(new BorderLayout());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);

        // ---- TOP (Title) ----
        JPanel p1 = new JPanel();
        p1.setOpaque(true);
        p1.setBackground(Color.decode("#051072"));

        JLabel l0 = new JLabel("CATALOG");
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80f));
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE (Selection Buttons) ----
        JPanel centerPanel = new JPanel(new GridBagLayout()); 
        centerPanel.setBackground(Color.decode("#dbd3c5"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 30, 20, 30); 
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.5;
        gbc.ipady = 40; 
        gbc.ipadx = 240; 

        // Button 1: Courses
        JButton bCourses = new JButton("Courses");
        bCourses.setBackground(Color.decode("#2f77b1"));
        bCourses.setForeground(Color.WHITE);
        bCourses.setFont(gFont.deriveFont(Font.PLAIN, 25f));
        bCourses.setFocusPainted(false);
        
        // Button 2: Sections
        JButton bSections = new JButton("Sections");
        bSections.setBackground(Color.decode("#2f77b1"));
        bSections.setForeground(Color.WHITE);
        bSections.setFont(gFont.deriveFont(Font.PLAIN, 25f));
        bSections.setFocusPainted(false);

        centerPanel.add(bCourses, gbc);
        gbc.gridy = 1;
        centerPanel.add(bSections, gbc);

        f.add(centerPanel, BorderLayout.CENTER);

        // ---- BOTTOM ----
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.decode("#2f77b1"));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(breatheFont.deriveFont(Font.PLAIN, 35f));
        backButton.setMargin(new Insets(10, 30, 5, 30));

        p3.add(backButton);
        f.add(p3, BorderLayout.SOUTH);
        f.setVisible(true);

        // --- Action Listeners ---
        bCourses.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tOpening Course Catalog...");
                new courses(username, role, password, roll_no);
                f.dispose();
            }
        });

        bSections.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tOpening Section Catalog...");
                new sections(username, role, password, roll_no);
                f.dispose();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tGoing Back...");
                if (role.equalsIgnoreCase("student")) {
                    new studentDashboard(username, role, password, roll_no); 
                    f.dispose();
                }
                else if (role.equalsIgnoreCase("instructor")) {
                    new InstructorDashboard(username, role, password, roll_no);
                    f.dispose();
                }
                else if (role.equalsIgnoreCase("admin")) {
                    new adminDashboard(roll_no);
                    f.dispose();
                }
            }
        });
    }
}