package edu.univ.erp.ui.student;
import edu.univ.erp.data.ErpCommandRunner;

import java.sql.SQLException;

import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.util.BREATHEFONT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class dropCourseFrame {
    public dropCourseFrame(String username, String role, String in_pass, String roll_no) {

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
        p1.setBackground(Color.decode("#051072")); 
        
        JLabel l0 = new JLabel("DROP COURSE");
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

        f.add(p2, BorderLayout.CENTER);

        // ---- LOWS ----
        JPanel p3 = new JPanel();
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
  
        JButton bDrop = new JButton("Drop");
        bDrop.setBackground(Color.decode("#2f77b1")); 
        bDrop.setForeground(Color.WHITE); 
        bDrop.setFont(breatheFont.deriveFont(Font.PLAIN, 35));
        bDrop.setMargin(new Insets(10, 30, 5, 30));
        p3.add(bDrop);

        p3.add(Box.createHorizontalStrut(20));

        JButton bBack = new JButton("Back");
        bBack.setBackground(Color.decode("#2f77b1")); 
        bBack.setForeground(Color.WHITE); 
        bBack.setFont(breatheFont.deriveFont(Font.PLAIN, 35));
        bBack.setMargin(new Insets(10, 30, 5, 30));
        p3.add(bBack);

        f.add(p3, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);


        // --- Action Listeners ---
        bDrop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowsDeleted = ErpCommandRunner.studentDropCourseHelper(roll_no, tCourseCode.getText().trim());
                if (rowsDeleted == 0) {
                    JOptionPane.showMessageDialog(null, "Error: Not enrolled in given course.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Error: Not enrolled in given course.");
                } else {
                    JOptionPane.showMessageDialog(null, "Course dropped successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Course droped successfully.");
                }
                new studentDashboard(username, role, in_pass, roll_no);
                f.dispose();
            }
        });

        bBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tGoing back to Student Dashboard..");
                new studentDashboard(username, role, in_pass, roll_no);
                f.dispose();
            }
        });
    }
}