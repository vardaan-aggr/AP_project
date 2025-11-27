package edu.univ.erp.ui.admin.manageCourseSection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.service.AdminService;
import edu.univ.erp.ui.admin.adminDashboard;
import edu.univ.erp.util.BREATHEFONT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class deleteSectionACourse {
    public deleteSectionACourse(String roll_no) { 

         Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen(); 

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame("Delete Section and Course");
        f.setSize(800, 600); 
        f.setLayout(new BorderLayout());

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setBackground(Color.decode("#051072")); 
        p1.setOpaque(true); 
        
        JLabel l0 = new JLabel("DELETE COURSE"); 
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 60f)); 
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE ----
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        p2.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Course Code
        JLabel l1 = new JLabel("Course Code:");
        l1.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l1.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l1, gbc);

        JTextField tCourseCode = new JTextField(20);
        tCourseCode.setFont(gFont.deriveFont(Font.PLAIN, 21));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        p2.add(tCourseCode, gbc);

        // Row 1: Section
        JLabel l2 = new JLabel("Section:");
        l2.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l2.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l2, gbc);

        JTextField tSection = new JTextField(20);
        tSection.setFont(gFont.deriveFont(Font.PLAIN, 21));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        p2.add(tSection, gbc);

        // --- BUTTONS ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton bDelete = new JButton("Delete");
        bDelete.setBackground(Color.decode("#051072")); 
        bDelete.setForeground(Color.WHITE);
        bDelete.setFont(breatheFont.deriveFont(Font.PLAIN, 35));
        bDelete.setMargin(new Insets(10, 30, 5, 30));
        buttonPanel.add(bDelete);

        JButton bBack = new JButton("Back");
        bBack.setBackground(Color.decode("#2f77b1"));
        bBack.setForeground(Color.WHITE);
        bBack.setFont(breatheFont.deriveFont(Font.PLAIN, 35));
        bBack.setMargin(new Insets(10, 30, 5, 30));
        buttonPanel.add(bBack);

        // Place buttons
        gbc.gridx = 0; gbc.gridy = 2; 
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(50, 10, 10, 10); 
        p2.add(buttonPanel, gbc);

        f.add(p2, BorderLayout.CENTER);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // --- Action Listeners ---
        bDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String courseCode = tCourseCode.getText().trim();
                String section = tSection.getText().trim();

                if (courseCode.isEmpty() || section.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both Course Code and Section.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 1. Call Service
                AdminService service = new AdminService();
                String message = service.deleteCourseAndSection(courseCode, section);
                
                // 2. Show Result
                if (message.startsWith("Success")) {
                    JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
                    new adminDashboard(roll_no);
                    f.dispose();
                } else if (message.startsWith("Warning")) {
                    JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
                    new adminDashboard(roll_no);
                    f.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        bBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Going back...");
                new adminDashboard(roll_no);
                f.dispose();
            }
        });
    }
}