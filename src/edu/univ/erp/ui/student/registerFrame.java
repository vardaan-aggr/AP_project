package edu.univ.erp.ui.student;

import edu.univ.erp.service.StudentService;

import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.util.BREATHEFONT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class registerFrame {
    public registerFrame(String username, String role, String in_pass, String roll_no) {
        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen();
        
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame();
        f.setSize(800, 600);
        f.setLayout(new BorderLayout()); 

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setOpaque(true); 
        p1.setBackground(Color.decode("#051072")); 
        
        JLabel l0 = new JLabel("REGISTER");
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80f));
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE ----
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        p2.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel l1 = new JLabel("Course code:");
        l1.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l1.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        p2.add(l1, gbc);

        JTextField tCourseCode = new JTextField(20);
        tCourseCode.setFont(gFont.deriveFont(Font.PLAIN, 21));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        p2.add(tCourseCode, gbc);

        JLabel l2 = new JLabel("Section:");
        l2.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l2.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        p2.add(l2, gbc);

        JTextField tSection = new JTextField(20);
        tSection.setFont(gFont.deriveFont(Font.PLAIN, 21));
        tSection.setFont(gFont.deriveFont(Font.PLAIN, 21));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        p2.add(tSection, gbc);

        f.add(p2, BorderLayout.CENTER);

        // ---- LOWS ----
        JPanel p3 = new JPanel();
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        
        JButton b1 = new JButton("Register");
        b1.setBackground(Color.decode("#2f77b1")); 
        b1.setForeground(Color.WHITE); 
        b1.setFont(breatheFont.deriveFont(Font.PLAIN, 32f));
        b1.setPreferredSize(new Dimension(160, 50));
        b1.setMargin(new Insets(10, 30, 5, 30));
        p3.add(b1);

        p3.add(Box.createHorizontalStrut(20));

        JButton b2 = new JButton("Back");
        b2.setBackground(Color.decode("#2f77b1")); 
        b2.setForeground(Color.WHITE); 
        b2.setFont(breatheFont.deriveFont(Font.PLAIN, 32f));
        b2.setPreferredSize(new Dimension(160, 50));
        b2.setMargin(new Insets(10, 30, 5, 30));
        p3.add(b2);

        f.add(p3, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);


        // --- Action Listeners ---
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StudentService service = new StudentService();
                int rowsInserted = service.registerCourse(roll_no, tCourseCode.getText().trim(), tSection.getText().trim()); 

                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(null, "Registered successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Registered Successfully.");
                } else if (rowsInserted == -3) {
                    JOptionPane.showMessageDialog(null, "Drop failed: Maintenance Mode is ON.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Drop failed: Maintenance Mode is ON.");
                } else if (rowsInserted == -11) {
                    JOptionPane.showMessageDialog(null, "Failed: Course capacity is full.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Failed: Maximum course capcity reached");
                } else if (rowsInserted == -5) {
                    JOptionPane.showMessageDialog(null, "You are already registered for this course/section!","Duplicate Registration", JOptionPane.WARNING_MESSAGE);
                    System.out.println("Failed: Maximum course capcity reached");
                }
                 else if (rowsInserted == 0 || rowsInserted == -1) {
                    // Result is 0 or -1 (Not enrolled or DB error)
                    JOptionPane.showMessageDialog(null, "Error: Not enrolled or course invalid.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Error: Not enrolled or course invalid.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Database Error.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tGoing back to Student Dashboard..");
                new studentDashboard(username, role, in_pass, roll_no);
                f.dispose();
            }
        });
    }
}