package edu.univ.erp.ui.admin;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.data.ErpCommandRunner;
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

        JTextField t1 = new JTextField(20);
        t1.setFont(gFont.deriveFont(Font.PLAIN, 21));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        p2.add(t1, gbc);

        // Row 1: Section
        JLabel l2 = new JLabel("Section:");
        l2.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l2.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l2, gbc);

        JTextField t2 = new JTextField(20);
        t2.setFont(gFont.deriveFont(Font.PLAIN, 21));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        p2.add(t2, gbc);

        // --- BUTTONS ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(Color.decode("#051072")); 
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(breatheFont.deriveFont(Font.PLAIN, 35));
        deleteButton.setMargin(new Insets(10, 30, 5, 30));
        buttonPanel.add(deleteButton);

        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.decode("#2f77b1"));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(breatheFont.deriveFont(Font.PLAIN, 35));
        backButton.setMargin(new Insets(10, 30, 5, 30));
        buttonPanel.add(backButton);

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
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowsAffected = -1;
                String courseCode = t1.getText().trim();
                String section = t2.getText().trim();

                if (courseCode.isEmpty() || section.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both Course Code and Section.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(null, 
                    "Are you sure you want to delete " + courseCode + " section " + section + "?\nThis will remove it from both Courses and Sections tables.", 
                    "Confirm Delete", 
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    rowsAffected = ErpCommandRunner.deleteSecCourseHelper(courseCode, section); 
                    switch (rowsAffected) {
                        case -3: {
                            JOptionPane.showMessageDialog(null, "Error: Couldn't connect to database.", "Error", JOptionPane.WARNING_MESSAGE);
                            System.out.println("Error: Couldn't connect to database.");
                        }
                        case -1: {
                            JOptionPane.showMessageDialog(null, "Error: Couldn't delete section.", "Error", JOptionPane.WARNING_MESSAGE);
                            System.out.println("Error: Couldn't delete section");
                        }
                        case -2: {
                            JOptionPane.showMessageDialog(null, "Error: Section didn't existed, but course existed so delted.", "Error", JOptionPane.WARNING_MESSAGE);
                            System.out.println("Error: Section didn't existed, but course existed so delted.");
                        }
                        case 2: {
                            JOptionPane.showMessageDialog(null, "Error: Section deleted, but Course not found in 'courses' table.", "Error", JOptionPane.WARNING_MESSAGE);
                            System.out.println("Error: Section deleted, but Course not found in 'courses' table.");
                        }
                        case 3: {
                            JOptionPane.showMessageDialog(null, "Error: Course didn't existed, but section existed so delted.", "Error", JOptionPane.WARNING_MESSAGE);
                            System.out.println("Error: Course deleted (if existed), but section not found in 'courses' table.");
                        }
                        case 4: {
                            JOptionPane.showMessageDialog(null, "Error: None of the section and course exist.", "Error", JOptionPane.WARNING_MESSAGE);
                            System.out.println("Error: None of the section and course exist.");
                   
                        }
                        default: {
                            JOptionPane.showMessageDialog(null, "Course and Section deleted successfully.", "Error", JOptionPane.WARNING_MESSAGE);
                            System.out.println("Course and Section deleted successfully.");
                        }
                    }
                    new adminDashboard(roll_no);
                    f.dispose();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Going back...");
                new adminDashboard(roll_no);
                f.dispose();
            }
        });
    }
}