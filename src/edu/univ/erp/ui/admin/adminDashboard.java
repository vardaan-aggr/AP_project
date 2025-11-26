package edu.univ.erp.ui.admin;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import edu.univ.erp.util.BREATHEFONT;
import edu.univ.erp.service.AdminService;

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

        JFrame f = new JFrame("Admin Dashboard");
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
        gbc.weightx = 1.0; 
        gbc.weighty = 1.0;      
        
        // Consistent Button Styling Variables
        Dimension buttonSize = new Dimension(280, 80);
        Color btnBg = Color.decode("#2f77b1"); // Primary button color (Dark Blue)
        Color btnFg = Color.WHITE;
        Font btnFont = gFont.deriveFont(Font.PLAIN, 21f);
        Insets btnMargin = new Insets(10, 30, 5, 30);
        
        // Secondary/Lighter button color (Light Blue)
        Color btnBgLight = Color.decode("#87c3fa"); 

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        JButton bAddUsers = new JButton("Add Users");
        styleButton(bAddUsers, btnBg, btnFg, btnFont, buttonSize, btnMargin);
        p2.add(bAddUsers, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        JButton bEditCourse = new JButton("Edit Course/Section");
        styleButton(bEditCourse, btnBg, btnFg, btnFont, buttonSize, btnMargin);
        p2.add(bEditCourse, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        JButton bUnassign = new JButton("Unassign Instructor");
        styleButton(bUnassign, btnBg, btnFg, btnFont, buttonSize, btnMargin);
        p2.add(bUnassign, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        JButton bMaintenance = new JButton("Maintenance Mode");
        styleButton(bMaintenance, btnBg, btnFg, btnFont, buttonSize, btnMargin);
        p2.add(bMaintenance, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        JButton bSearch = new JButton("Search Database");
        styleButton(bSearch, btnBg, btnFg, btnFont, buttonSize, btnMargin);
        p2.add(bSearch, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        JButton bBackup = new JButton("Backup / Restore");
        styleButton(bBackup, btnBg, btnFg, btnFont, buttonSize, btnMargin);
        p2.add(bBackup, gbc);

        f.add(p2, BorderLayout.CENTER);

        // ---- BOTTOM ---- 
        // Reverting p3 to use FlowLayout to match the loginPage's structure
        JPanel p3 = new JPanel(); 
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));

        // Logout Button (Primary color and font/size of the LOGIN button)
        JButton bLogout = new JButton("Logout");
        styleButton(bLogout, btnBg, btnFg, breatheFont.deriveFont(Font.PLAIN, 35f), null, btnMargin); // Use original Logout styling

        // Set Drop Deadline Button (Secondary color and font/size of the Change Password button)
        JButton bDeadline = new JButton("Set Drop Deadline");
        styleButton(bDeadline, btnBgLight, btnFg, breatheFont.deriveFont(Font.PLAIN, 30f), null, btnMargin); 
        
        // Positioning: Logout (Primary) on Left, Spacer, Deadline (Secondary) on Right
        p3.add(bLogout);
        p3.add(Box.createHorizontalStrut(20)); // Spacer
        p3.add(bDeadline);
        
        f.add(p3, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);


        // ---- Action Listeners ----
        bAddUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening users adding interface..");
                new AddUsers(rollNo);
                f.dispose();
            }
        }) ;

        bEditCourse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening courses/sections adding/editing interface..");
                new CreateEdit(rollNo);
                f.dispose();
            }
        });

        bUnassign.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening assign instructor interface..");
                new unassignIns(rollNo);
                f.dispose();
            }
        });

        bMaintenance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening toggle maintainence interface..");
                new maintainMode(rollNo);
                f.dispose();
            }
        });

        bSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening Search Database interface..");
                new searchDb(rollNo);
                f.dispose();
            }
        });

        bBackup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening Backup/Restore interface..");
                new BackupRestorePage(rollNo); 
                f.dispose();
            }
        });

        bDeadline.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String dateInput = JOptionPane.showInputDialog(f, "Enter Drop Deadline (YYYY-MM-DD):", "Set Deadline", JOptionPane.QUESTION_MESSAGE);

                if (dateInput != null) {
                    AdminService service = new AdminService();
                    String result = service.setDropDeadline(dateInput);

                    if ("Success".equals(result)) {
                        JOptionPane.showMessageDialog(f, "Drop Deadline updated to: " + dateInput, "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(f, result, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
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
        if(dim != null) btn.setPreferredSize(dim);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(font);
        btn.setMargin(margin);
    }
}