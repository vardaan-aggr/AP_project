package edu.univ.erp.ui.admin;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import edu.univ.erp.access.modeOps;
import edu.univ.erp.domain.Settings;
import edu.univ.erp.util.BREATHEFONT;


public class maintainMode {
    public maintainMode (String rollNo) {
        
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
        
        JLabel l0 = new JLabel("MAINTENANCE MODE");
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
        Dimension btnSize = new Dimension(280, 60); 

        gbc.gridy = 0;
        JButton bOn = new JButton("Turn On");
        styleButton(bOn, btnBg, btnFg, btnFont, btnSize);
        p2.add(bOn, gbc);

        gbc.gridy = 1;
        JButton bOff = new JButton("Turn Off");
        styleButton(bOff, btnBg, btnFg, btnFont, btnSize);
        p2.add(bOff, gbc);

        gbc.gridy = 2;
        JButton b3 = new JButton("Back");
        styleButton(b3, btnBg, btnFg, btnFont, btnSize);
        p2.add(b3, gbc);

        f.add(p2, BorderLayout.CENTER);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // ---- Action Listeners ----
        bOn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a generic settings object
                Settings s = new Settings("maintain_mode", "true");
                
                // Pass the object to the util
                if (modeOps.updateSetting(s)) {
                    JOptionPane.showMessageDialog(null, "Maintenance Mode On.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Maintenance Mode On..");
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Database Error occurred.", "Failure", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        bOff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings s = new Settings("maintain_mode", "false");
                
                if (modeOps.updateSetting(s)) {
                    JOptionPane.showMessageDialog(null, "Maintenance Mode Off.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Maintenance Mode Off..");
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Database Error occurred.", "Failure", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tGoing to Admin Dashboard..");
                new adminDashboard(rollNo);
                f.dispose();
            }
        });
    }
    private void styleButton(JButton button, Color bg, Color fg, Font font, Dimension size) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(font);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
    }
}
