package edu.univ.erp.ui.admin;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import edu.univ.erp.util.BREATHEFONT;
import edu.univ.erp.service.AdminService; 

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class BackupRestorePage {

    public BackupRestorePage(String rollNo) {

        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame("Backup & Restore");
        f.setSize(800, 600); 
        f.setLayout(new BorderLayout());

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setBackground(Color.decode("#051072")); 
        p1.setOpaque(true); 
        
        JLabel l0 = new JLabel("BACKUP & RESTORE"); 
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80f)); 
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE ----
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.gridx = 0;
        gbc.weightx = 1;

        Font btnFont = gFont.deriveFont(Font.PLAIN, 25f);
        Color btnBg = Color.decode("#2f77b1");
        Color txtColor = Color.WHITE;

        // Backup
        JButton btnBackup = new JButton("Backup Database");
        btnBackup.setBackground(btnBg); 
        btnBackup.setForeground(txtColor);
        btnBackup.setFont(btnFont);
        btnBackup.setMargin(new Insets(15, 40, 15, 40)); 
        gbc.gridy = 0;
        p2.add(btnBackup, gbc);

        // Restore
        JButton btnRestore = new JButton("Restore Database");
        btnRestore.setBackground(btnBg); 
        btnRestore.setForeground(txtColor);
        btnRestore.setFont(btnFont);
        btnRestore.setMargin(new Insets(15, 40, 15, 40));
        gbc.gridy = 1;
        p2.add(btnRestore, gbc);

        // Back
        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.setBackground(Color.decode("#2f77b1")); 
        btnBack.setForeground(txtColor);
        btnBack.setFont(btnFont);
        btnBack.setMargin(new Insets(15, 40, 15, 40));
        gbc.gridy = 2;
        p2.add(btnBack, gbc);

        // Container to center p2
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(Color.decode("#dbd3c5"));
        container.add(p2);

        f.add(container, BorderLayout.CENTER);

        // --- Action Listeners ---
        // Hardcoded path 
        String fixedPath = "AP_project/src/edu/univ/erp/BackupRestore/erp_backup.sql"; 

        btnBackup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminService service = new AdminService();
                String result = service.performBackup(fixedPath);
                
                if ("Success".equals(result)) {
                    JOptionPane.showMessageDialog(f, "Backup Success!\nSaved to: " + fixedPath);
                } else {
                    JOptionPane.showMessageDialog(f, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnRestore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(f, "WARNING: This will DELETE current data and replace it from:\n" + fixedPath + "\nAre you sure?", "Confirm Restore", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    File backupFile = new File(fixedPath);
                    if (!backupFile.exists()) {
                        JOptionPane.showMessageDialog(f, "Error: Backup file not found at " + fixedPath);
                        return;
                    }

                    AdminService service = new AdminService();
                    String result = service.performRestore(fixedPath);

                    if ("Success".equals(result)) {
                        JOptionPane.showMessageDialog(f, "Restore Success!");
                    } else {
                        JOptionPane.showMessageDialog(f, result, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
       
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new adminDashboard(rollNo);
                f.dispose();
            }
        });

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}