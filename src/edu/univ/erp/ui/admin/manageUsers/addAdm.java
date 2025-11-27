package edu.univ.erp.ui.admin.manageUsers;

import edu.univ.erp.auth.HashGenerator;
import edu.univ.erp.service.AdminService;
import edu.univ.erp.ui.admin.adminDashboard;

import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.util.BREATHEFONT;

public class addAdm {
    public addAdm(String rollNo) {
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
        p1.setOpaque(true); 
        
        JLabel l0 = new JLabel("ADD ADMIN"); 
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80f)); 
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

        JLabel l1 = new JLabel("Username:");
        l1.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l1.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l1, gbc);

        JTextField tUsername = new JTextField(20);
        tUsername.setFont(gFont.deriveFont(Font.PLAIN, 21));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        p2.add(tUsername, gbc);

        JLabel l2 = new JLabel("Password:");
        l2.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l2.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l2, gbc);

        JTextField tPassword = new JTextField(20); 
        tPassword.setFont(gFont.deriveFont(Font.PLAIN, 21));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        p2.add(tPassword, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        
        // ---- LOWS ----
        JButton b1 = new JButton("Back");
        b1.setBackground(Color.decode("#2f77b1")); 
        b1.setForeground(Color.WHITE); 
        b1.setFont(breatheFont.deriveFont(Font.PLAIN, 35));
        b1.setMargin(new Insets(10, 30, 5, 30));
        buttonPanel.add(b1);

        JButton b2 = new JButton("Register");
        b2.setBackground(Color.decode("#2f77b1")); 
        b2.setForeground(Color.WHITE); 
        b2.setFont(breatheFont.deriveFont(Font.PLAIN, 35));
        b2.setMargin(new Insets(10, 30, 5, 30));
        buttonPanel.add(b2);

        gbc.gridx = 0; gbc.gridy = 2; 
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(120, 10, 10, 10); 
        p2.add(buttonPanel, gbc);

        f.add(p2, BorderLayout.CENTER);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // ---- Action Listeners ----
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tUsername.getText().isEmpty() || tPassword.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String username = tUsername.getText();
                String hashPass = HashGenerator.makeHash(tPassword.getText().trim());

                AdminService service = new AdminService();  
                String result = service.registerAdmin(username, hashPass);

                if (result.startsWith("Success")) {
                    JOptionPane.showMessageDialog(null, result, "Success", JOptionPane.INFORMATION_MESSAGE);
                    f.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
                System.out.println("\tGoing back to Admin Dashboard..");
                new adminDashboard(rollNo);
                f.dispose();
            }
        });

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tGoing back to Admin Dashboard..");
                new adminDashboard(rollNo);
                f.dispose();
            }
        });    
    }
}
