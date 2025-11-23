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

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class editSection {
    public editSection(String roll_no) {

        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen(); 

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f0 = new JFrame("Edit Section");
        f0.setSize(800, 600); 
        f0.setLayout(new BorderLayout());

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setBackground(Color.decode("#051072")); 
        p1.setOpaque(true); 
        
        JLabel l0 = new JLabel("EDIT SECTION"); 
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 60f)); 
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f0.add(p1, BorderLayout.NORTH);

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

        JButton bContinue = new JButton("Continue");
        bContinue.setBackground(Color.decode("#2f77b1")); 
        bContinue.setForeground(Color.WHITE); 
        bContinue.setFont(breatheFont.deriveFont(Font.PLAIN, 35));
        bContinue.setMargin(new Insets(10, 30, 5, 30));
        buttonPanel.add(bContinue);

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

        f0.add(p2, BorderLayout.CENTER);

        f0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f0.setLocationRelativeTo(null);
        f0.setVisible(true);

        // --- Action Listeners ---
        bContinue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String[] arr = ErpCommandRunner.SectionInfoGetter(tCourseCode.getText().trim(), tSection.getText().trim());   
                    new editSectionPage2(roll_no,arr);
                    f0.dispose();             
                } catch (SQLException ex) {
                    System.err.println("Error while getting section: " + ex);
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error while getting section: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                    new adminDashboard(roll_no);
                    f0.dispose();
                }
            }
        });

        bBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Going back to Admin Dashboard..");
                new adminDashboard(roll_no);
                f0.dispose();
            }
        });        
    }
}
