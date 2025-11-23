package edu.univ.erp.ui.admin;

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
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 

public class adminDashboard {
    public adminDashboard(String rollNo) {

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

        JLabel l0 = new JLabel("ADMIN DASHBOARD");
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
        gbc.fill = GridBagConstraints.BOTH;      
        
        Dimension buttonSize = new Dimension(280, 80);
        Color btnBg = Color.decode("#2f77b1");
        Color btnFg = Color.WHITE;
        Font btnFont = gFont.deriveFont(Font.PLAIN, 21f);
        Insets btnMargin = new Insets(10, 30, 5, 30);

        gbc.gridx = 0; gbc.gridy = 0;
        JButton b1 = new JButton("Add Users");
        styleButton(b1, btnBg, btnFg, btnFont, buttonSize, btnMargin);
        p2.add(b1, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        JButton b2 = new JButton("Edit course/section");
        styleButton(b2, btnBg, btnFg, btnFont, buttonSize, btnMargin);
        p2.add(b2, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JButton b3 = new JButton("Unassign Instructor");
        styleButton(b3, btnBg, btnFg, btnFont, buttonSize, btnMargin);
        p2.add(b3, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        JButton b4 = new JButton("Maintenance Mode");
        styleButton(b4, btnBg, btnFg, btnFont, buttonSize, btnMargin);
        p2.add(b4, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JButton b5 = new JButton("Search Database");
        styleButton(b5, btnBg, btnFg, btnFont, buttonSize, btnMargin);
        p2.add(b5, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        JButton b6 = new JButton("Backup / Restore");
        styleButton(b6, btnBg, btnFg, btnFont, buttonSize, btnMargin);
        p2.add(b6, gbc);

        f.add(p2, BorderLayout.CENTER);

        // ---- LOWS ---- 
        JPanel p3 = new JPanel();
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton bLogout = new JButton("Logout");
        bLogout.setBackground(Color.decode("#2f77b1"));
        bLogout.setForeground(Color.WHITE);
        bLogout.setFont(breatheFont.deriveFont(Font.PLAIN, 35f));
        bLogout.setMargin(new Insets(10, 30, 5, 30));

        p3.add(bLogout);
        f.add(p3, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);


        // ---- Action Listeners ----
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening users adding interface..");
                new AddUsers(rollNo);
                f.dispose();
            }
        }) ;

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening courses/sections adding/editing interface..");
                new CreateEdit(rollNo);
                f.dispose();
            }
        });

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening assign instructor interface..");
                new unassignIns(rollNo);
                f.dispose();
            }
        });

        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening toggle maintainence interface..");
                new maintainMode(rollNo);
                f.dispose();
            }
        });

        b5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening Search Database interface..");
                new searchDb(rollNo);
                f.dispose();
            }
        });

        b6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening Backup/Restore interface..");
                new BackupRestorePage(rollNo); 
                f.dispose();
            }
        });

        bLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Logging out..");
                System.out.println("\nLogged out successfully...");
                f.dispose();
            }
        });
    }

    private static void styleButton(JButton btn, Color bg, Color fg, Font font, Dimension dim, Insets margin) {
        btn.setPreferredSize(dim);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(font);
        btn.setMargin(margin);
    }
}