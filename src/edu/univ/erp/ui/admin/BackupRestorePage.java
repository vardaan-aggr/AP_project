package edu.univ.erp.ui.admin;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import edu.univ.erp.util.BREATHEFONT;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class BackupRestorePage {

    private String dbName;
    private String dbUser;
    private String dbPass;

    public BackupRestorePage(String rollNo) {
        if (!loadConfig()) {
            JOptionPane.showMessageDialog(null, "Error: Could not load database config.");
            new adminDashboard(rollNo); 
            return;
        }

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
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 60f)); 
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

        // Style Definitions
        Font btnFont = gFont.deriveFont(Font.PLAIN, 25f);
        Color btnBg = Color.decode("#2f77b1");
        Color txtColor = Color.WHITE;

        // Button 1: Backup
        JButton btnBackup = new JButton("Backup Database");
        btnBackup.setBackground(btnBg); 
        btnBackup.setForeground(txtColor);
        btnBackup.setFont(btnFont);
        btnBackup.setMargin(new Insets(15, 40, 15, 40)); 
        gbc.gridy = 0;
        p2.add(btnBackup, gbc);

        // Button 2: Restore
        JButton btnRestore = new JButton("Restore Database");
        btnRestore.setBackground(btnBg); 
        btnRestore.setForeground(txtColor);
        btnRestore.setFont(btnFont);
        btnRestore.setMargin(new Insets(15, 40, 15, 40));
        gbc.gridy = 1;
        p2.add(btnRestore, gbc);

        // Button 3: Back
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


        // --- ACTIONS  ---

        String fixedPath = "AP_project/src/edu/univ/erp/BackupRestore/erp_backup.sql"; 

        btnBackup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (performBackup(fixedPath)) {
                    JOptionPane.showMessageDialog(f, "Backup Success!\nSaved to: " + fixedPath);
                } else {
                    JOptionPane.showMessageDialog(f, "Backup Failed.\nCheck console for errors.");
                }
            }
        });

        btnRestore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(f, 
                    "WARNING: This will DELETE current data and replace it from:\n" + fixedPath + "\nAre you sure?", 
                    "Confirm Restore", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    File backupFile = new File(fixedPath);
                    if (!backupFile.exists()) {
                        JOptionPane.showMessageDialog(f, "Error: Backup file not found at " + fixedPath);
                        return;
                    }

                    if (performRestore(fixedPath)) {
                        JOptionPane.showMessageDialog(f, "Restore Success!\nPlease restart the app.");
                    } else {
                        JOptionPane.showMessageDialog(f, "Restore Failed.\nCheck console for errors.");
                    }
                }
            }
        });
       
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Converting String rollNo to int for Dashboard compatibility
                try {
                    new adminDashboard(rollNo);
                } catch (NumberFormatException ex) {
                    // Fallback if rollNo isn't a number
                    System.err.println("Invalid admin ID"); 
                }
                f.dispose();
            }
        });

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    // --- CONFIG LOADER ---
    private boolean loadConfig() {
        Properties prop = new Properties();
        
        String hardcodedPath = "AP_project/src/resources/Erpdatabase.properties"; 

        try (java.io.FileInputStream input = new java.io.FileInputStream(hardcodedPath)) {
            prop.load(input);
            
            this.dbUser = prop.getProperty("username");
            this.dbPass = prop.getProperty("password");
            String url = prop.getProperty("jdbcUrl");
            
            if (url != null) {
                this.dbName = url.substring(url.lastIndexOf("/") + 1);
                if (this.dbName.contains("?")) {
                    this.dbName = this.dbName.substring(0, this.dbName.indexOf("?"));
                }
            } else {
                System.out.println("Error: jdbcUrl missing in properties.");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error loading config from: " + hardcodedPath);
            e.printStackTrace();
            return false;
        }
    }

    // --- BACKUP LOGIC ---
    private boolean performBackup(String savePath) {
        // Ensure "mariadb-dump" is in your Windows/Mac PATH environment variable
        List<String> command = Arrays.asList(
            "mariadb-dump", 
            "-u" + dbUser, 
            "-p" + dbPass, 
            "--add-drop-table", 
            dbName
        );

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectOutput(new File(savePath));

        try {
            Process p = pb.start();
            int exitCode = p.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- RESTORE LOGIC ---
    private boolean performRestore(String filePath) {
        List<String> command = Arrays.asList(
            "mariadb", 
            "-u" + dbUser, 
            "-p" + dbPass, 
            dbName
        );

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectInput(new File(filePath));

        try {
            Process p = pb.start();
            int exitCode = p.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}