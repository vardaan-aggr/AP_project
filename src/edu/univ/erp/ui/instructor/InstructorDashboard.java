package edu.univ.erp.ui.instructor;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import edu.univ.erp.ui.common.catalog;
import edu.univ.erp.util.BREATHEFONT;
import edu.univ.erp.access.modeOps;
import edu.univ.erp.domain.Settings; 

public class InstructorDashboard {
    
    public InstructorDashboard(String username, String role, String password, String roll_no) {
        
        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame("Instructor Dashboard");
        f.setSize(800, 600);
        f.setLayout(new BorderLayout());

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setOpaque(true); 
        p1.setBackground(Color.decode("#051072")); 
        
        JLabel l0 = new JLabel("INSTRUCTOR DASHBOARD");
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
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        Color btnBg = Color.decode("#2f77b1");
        Color btnFg = Color.WHITE;
        Font btnFont = gFont.deriveFont(Font.PLAIN, 20f); 
        Dimension btnSize = new Dimension(440, 60); 

        gbc.gridy = 0;
        JButton b1 = new JButton("My Sections");
        styleButton(b1, btnBg, btnFg, btnFont, btnSize);
        p2.add(b1, gbc);

        gbc.gridy = 1;
        JButton b2 = new JButton("Compute Final Grades");
        styleButton(b2, btnBg, btnFg, btnFont, btnSize);
        p2.add(b2, gbc);
    
        gbc.gridy = 2;
        JButton b3 = new JButton("Class Statistics");
        styleButton(b3, btnBg, btnFg, btnFont, btnSize);
        p2.add(b3, gbc);
        
        gbc.gridy = 3;
        JButton b4 = new JButton("Courses/Sections");
        styleButton(b4, btnBg, btnFg, btnFont, btnSize);
        p2.add(b4, gbc);

        gbc.gridy = 4;
        JButton bNotify = new JButton("🔔 Notifications");
        styleButton(bNotify, btnBg, btnFg, btnFont, btnSize);
        p2.add(bNotify, gbc);

        f.add(p2, BorderLayout.CENTER);

        // ---- LOWS ----
        JPanel p3 = new JPanel();
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(Color.decode("#2f77b1"));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(breatheFont.deriveFont(Font.PLAIN, 35f)); 
        logoutBtn.setMargin(new Insets(10, 30, 5, 30));
        
        p3.add(logoutBtn);
        f.add(p3, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // --- ACTION LISTENERS ---
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening My Sections...");
                new MySectionsFrame(username, role, password, roll_no);
                f.dispose(); 
            }
        });
        
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings mode = modeOps.getSetting("maintain_mode");
                
                if (mode != null && mode.isTrue()) {
                    JOptionPane.showMessageDialog(null, "Cannot change, Maintenance mode is on.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("\tCouldn't open bcz maintenance mode is on.");
                } else {
                    System.out.println("Opening Compute Final Grades...");
                    new ComputeFinalFrame(username, role, password, roll_no);
                    f.dispose();
                }
            }
        });
        
        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening Class Statistics...");
                new ClassStatisticsFrame(username, role, password, roll_no);
                f.dispose();
            }
        });

        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening Course Catalog...");
                new catalog(username, role, password, roll_no);
                f.dispose();
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(f, "Logged out.");
                System.out.println("Logged out.");
                f.dispose();
            }
        });

        bNotify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new edu.univ.erp.ui.common.NotificationDialog(f, roll_no);
            }
        });

    }

    void styleButton(JButton button, Color bg, Color fg, Font font, Dimension size) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(font);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
    }
}