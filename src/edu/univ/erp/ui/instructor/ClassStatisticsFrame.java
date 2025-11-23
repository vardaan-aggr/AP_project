package edu.univ.erp.ui.instructor;

import java.sql.SQLException;
import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;

import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.util.BREATHEFONT;

public class ClassStatisticsFrame {
    public ClassStatisticsFrame(String username, String role, String password, String roll_no) {

        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame("Class Statistics");
        f.setSize(800, 600);
        f.setLayout(new BorderLayout());
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setOpaque(true); 
        p1.setBackground(Color.decode("#051072")); 
        
        JLabel l0 = new JLabel("CLASS STATISTICS");
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80f));
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- CENTER ----
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setBackground(Color.decode("#dbd3c5"));
        p2.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel l1 = new JLabel("Course Code:");
        l1.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l1.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        p2.add(l1, gbc);

        JTextField code = new JTextField(20);
        code.setFont(gFont.deriveFont(Font.PLAIN, 21));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        p2.add(code, gbc);

        JLabel l2 = new JLabel("Section:");
        l2.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l2.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        p2.add(l2, gbc);

        JTextField sectionField = new JTextField(20);
        sectionField.setFont(gFont.deriveFont(Font.PLAIN, 21));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        p2.add(sectionField, gbc);

        f.add(p2, BorderLayout.CENTER);

        // ---- LOWS ----
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        p3.setBackground(Color.decode("#dbd3c5"));
        
        JButton seeStats = new JButton("See Statistics");
        seeStats.setBackground(Color.decode("#2f77b1"));
        seeStats.setForeground(Color.WHITE);
        seeStats.setFont(breatheFont.deriveFont(Font.PLAIN, 35f));
        seeStats.setMargin(new Insets(10, 30, 5, 30));
        p3.add(seeStats);

        JButton bBack = new JButton("Back");
        bBack.setBackground(Color.decode("#2f77b1"));
        bBack.setForeground(Color.WHITE);
        bBack.setFont(breatheFont.deriveFont(Font.PLAIN, 35f));
        bBack.setMargin(new Insets(10, 30, 5, 30));
        p3.add(bBack);

        f.add(p3, BorderLayout.SOUTH);
        f.setVisible(true);

        // --- Action Listeners ---
        seeStats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String courseCode_in = code.getText().trim();
                String section_in = sectionField.getText().trim();
                
                if(courseCode_in.isEmpty() || section_in.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both Course Code and Section.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double stats = computeStats(courseCode_in, section_in);
                if (stats != 0.0) {
                    JOptionPane.showMessageDialog(null, "Average CGPA for " + courseCode_in + " (" + section_in + ") = " + String.format("%.2f", stats), "Statistics", JOptionPane.INFORMATION_MESSAGE);
                }      
            }
        });

        bBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tGoing back to Instructor Dashboard..");
                new InstructorDashboard(username, role, password, roll_no);
                f.dispose();
            }
        });
    }

    private String[] gradesArr(String courseCode, String section) {
        String[] strArr = null;
        try {
            strArr = ErpCommandRunner.instructorStatsHelper(courseCode, section);
        } catch(SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new String[0];
        }
        return strArr;
    }
    
    private double computeStats(String courseCode, String section) {
        String[] grades = gradesArr(courseCode, section);
        if (grades == null || grades.length == 0) { return 0.0; }
        int totalPoints = 0;
        for (String grade : grades) {
            switch (grade.toUpperCase()) {
                case "A":
                    totalPoints += 3;
                    break;
                case "B":
                    totalPoints += 2;
                    break;
                case "C":
                    totalPoints += 1;
                    break;
                case "F":
                    totalPoints += 0;
                    break;
                default:
                    break;
            }
        }
        return grades.length == 0 ? 0.0 : (double) totalPoints / grades.length;
    }
}