package edu.univ.erp.ui.admin;

import javax.swing.*;
import edu.univ.erp.util.BREATHEFONT;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class BackupRestorePage {

    private String dbName;
    private String dbUser;
    private String dbPass;

    public BackupRestorePage(String rollNo) {
        // 1. LOAD CONFIGURATION FIRST
        if (!loadConfig()) {
            JOptionPane.showMessageDialog(null, "Error: Could not load config from resources/BackupConfig.properties");
            // If config fails, we stop here to prevent errors later
            return;
        }

        Font breatheFont = BREATHEFONT.fontGen();
        
        JFrame f = new JFrame("Backup & Restore");
        f.setSize(600, 400);
        f.setLayout(new GridBagLayout());
        f.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnBackup = new JButton("Backup Database");
        JButton btnRestore = new JButton("Restore Database");
        JButton btnBack = new JButton("Back to Dashboard");

        // Style
        Font btnFont = breatheFont.deriveFont(Font.BOLD, 20f);
        Color btnBg = Color.decode("#2f77b1");
        
        for (JButton b : new JButton[]{btnBackup, btnRestore, btnBack}) {
            b.setFont(btnFont);
            b.setBackground(btnBg);
            b.setForeground(Color.WHITE);
        }

        // --- BACKUP ACTION ---
        btnBackup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Backup As");
                if (fileChooser.showSaveDialog(f) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String path = file.getAbsolutePath();
                    if (!path.endsWith(".sql")) path += ".sql";
                    
                    if (performBackup(path)) {
                        JOptionPane.showMessageDialog(f, "Backup Success!\nSaved to: " + path);
                    } else {
                        JOptionPane.showMessageDialog(f, "Backup Failed. Check console for errors.");
                    }
                }
            }
        });
        
        // --- RESTORE ACTION ---
        btnRestore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select Backup File");
                if (fileChooser.showOpenDialog(f) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    
                    int confirm = JOptionPane.showConfirmDialog(f, 
                        "WARNING: This will DELETE current data and replace it.\nAre you sure?", 
                        "Confirm Restore", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (performRestore(file.getAbsolutePath())) {
                            JOptionPane.showMessageDialog(f, "Restore Success!\nPlease restart the app.");
                        } else {
                            JOptionPane.showMessageDialog(f, "Restore Failed. Check console for errors.");
                        }
                    }
                }
            }
        });
       
        // --- BACK ACTION ---
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new adminDashboard(rollNo);
                f.dispose();
            }
        });
        
        panel.add(btnBackup);
        panel.add(btnRestore);
        panel.add(btnBack);
        f.add(panel);
        f.setVisible(true);
    }

    // --- CONFIG LOADER ---
    private boolean loadConfig() {
        Properties prop = new Properties();
        String filePath = "resources/BackupConfig.properties"; 
        
        try (FileInputStream input = new FileInputStream(filePath)) {
            prop.load(input);
            
            this.dbName = prop.getProperty("db.name");
            this.dbUser = prop.getProperty("db.user");
            this.dbPass = prop.getProperty("db.password");
            
            System.out.println("Backup Config Loaded: User=" + dbUser + ", DB=" + dbName);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to load backup config file.");
            e.printStackTrace();
            return false;
        }
    }

    // --- BACKUP LOGIC ---
    private boolean performBackup(String savePath) {
        // For Arch Linux: using mariadb-dump
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
            return p.waitFor() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- RESTORE LOGIC ---
    private boolean performRestore(String filePath) {
        // For Arch Linux: using mariadb client
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
            return p.waitFor() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}