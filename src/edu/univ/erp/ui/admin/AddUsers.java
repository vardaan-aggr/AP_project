package edu.univ.erp.ui.admin;

import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.ui.admin.manageUsers.addAdm;
import edu.univ.erp.ui.admin.manageUsers.addIns;
import edu.univ.erp.ui.admin.manageUsers.addStd;
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

public class AddUsers {
    public AddUsers(String rollNo) {

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
        
        JLabel l0 = new JLabel("ADD USER");
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
        
        JLabel l1 = new JLabel("Role:");
        l1.setFont(gFont.deriveFont(Font.PLAIN, 24)); 
        l1.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l1, gbc);

        JTextField tRole = new JTextField(20);
        tRole.setFont(gFont.deriveFont(Font.PLAIN, 21)); 
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        p2.add(tRole, gbc);
        
        // ---- LOWS ----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false); 
        
        JButton b1 = new JButton("Back");
        b1.setBackground(Color.decode("#2f77b1")); 
        b1.setForeground(Color.WHITE); 
        b1.setFont(breatheFont.deriveFont(Font.PLAIN, 35)); 
        b1.setMargin(new Insets(10, 30, 5, 30));
        buttonPanel.add(b1);

        JButton b2 = new JButton("Next");
        b2.setBackground(Color.decode("#2f77b1")); 
        b2.setForeground(Color.WHITE); 
        b2.setFont(breatheFont.deriveFont(Font.PLAIN, 35)); 
        b2.setMargin(new Insets(10, 30, 5, 30));
        buttonPanel.add(b2);

        gbc.gridx = 0; gbc.gridy = 1; 
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(40, 10, 10, 10); 
        p2.add(buttonPanel, gbc);
        
        f.add(p2, BorderLayout.CENTER);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // ---- ActionListeners ----
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\t to Admin Dashboard..");
                new adminDashboard(rollNo);
                f.dispose();
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String role = tRole.getText().trim().toLowerCase();
                if (role.equals("student")) {
                    new addStd(rollNo);
                    System.out.println(" to add student user..");
                    f.dispose();
                } else if (role.equals("instructor")) {
                    System.out.println(" to add instructor user..");
                    new addIns(rollNo);
                    f.dispose();
                } else if (role.equals("admin")) {
                    System.out.println(" to add admin user..");
                    new addAdm(rollNo);
                    f.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "ENTER ONLY Student, Instructor, Admin", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Rerunning add users..");
                }
            }
        });
    }
}
