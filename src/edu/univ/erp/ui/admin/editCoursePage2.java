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

public class editCoursePage2 {

    public editCoursePage2(String roll_no,String arrgs[]) {

        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen(); 

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame("Edit Course Details");
        f.setSize(800, 600); 
        f.setLayout(new BorderLayout());

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setBackground(Color.decode("#051072")); 
        p1.setOpaque(true); 
        
        JLabel l0 = new JLabel("EDIT COURSE"); 
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 60f)); 
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE ----
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        p2.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Course Code (Non-Editable)
        JLabel l1 = new JLabel("Course Code:");
        l1.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l1.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l1, gbc);

        JTextField t1 = new JTextField(20);
        t1.setFont(gFont.deriveFont(Font.PLAIN, 21));
        t1.setText(arrgs[0]); // Pre-fill
        t1.setEditable(false); // Read-only
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        p2.add(t1, gbc);

        // Row 1: Section (Non-Editable)
        JLabel l2 = new JLabel("Section:");
        l2.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l2.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l2, gbc);

        JTextField t2 = new JTextField(20);
        t2.setFont(gFont.deriveFont(Font.PLAIN, 21));
        t2.setText(arrgs[2]); // Pre-fill
        t2.setEditable(false); // Read-only
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        p2.add(t2, gbc);

        // Row 2: Title (Editable)
        JLabel l3 = new JLabel("Title:");
        l3.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l3.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l3, gbc);

        JTextField t3 = new JTextField(20);
        t3.setFont(gFont.deriveFont(Font.PLAIN, 21));
        t3.setText(arrgs[1]); // Pre-fill
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        p2.add(t3, gbc);

        // Row 3: Credits (Editable)
        JLabel l4 = new JLabel("Credits:");
        l4.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l4.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l4, gbc);

        JTextField t4 = new JTextField(20);
        t4.setFont(gFont.deriveFont(Font.PLAIN, 21));
        t4.setText(arrgs[3]); // Pre-fill
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        p2.add(t4, gbc);

        // --- BUTTONS ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton assignButton = new JButton("Save"); 
        assignButton.setBackground(Color.decode("#2f77b1")); 
        assignButton.setForeground(Color.WHITE); 
        assignButton.setFont(breatheFont.deriveFont(Font.PLAIN, 35));
        assignButton.setMargin(new Insets(10, 30, 5, 30));
        buttonPanel.add(assignButton);

        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.decode("#2f77b1")); 
        backButton.setForeground(Color.WHITE); 
        backButton.setFont(breatheFont.deriveFont(Font.PLAIN, 35));
        backButton.setMargin(new Insets(10, 30, 5, 30));
        buttonPanel.add(backButton);

        // Place buttons
        gbc.gridx = 0; gbc.gridy = 4; 
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
        assignButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowsUpdated = -1;
                try {
                    rowsUpdated = ErpCommandRunner.courseUpdater(t3.getText().trim(), t4.getText().trim(), t1.getText().trim(), t2.getText().trim());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: Couldn't update course: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Error: Couldn't update course." + ex);
                }
                if (rowsUpdated == 0) {
                    JOptionPane.showMessageDialog(null, "Error: Course code and Section dont match.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Error: Course code and Section dont match.");
                } else {
                    JOptionPane.showMessageDialog(null, "Section updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Update Successful.");
                }
                new adminDashboard(roll_no);
                f.dispose();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Going back to Admin Dashboard..");
                new adminDashboard(roll_no);
                f.dispose();
            }
        });
    }
}
