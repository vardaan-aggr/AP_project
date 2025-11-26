package edu.univ.erp.auth;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.service.LoginService;
import edu.univ.erp.ui.auth.loginPage;
import edu.univ.erp.util.BREATHEFONT;

public class changePassword {
    
    public changePassword(String usernameIn) {

        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen();
        
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame("Change Password");
        f.setSize(800, 600);
        f.setLayout(new BorderLayout());

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setBackground(Color.decode("#051072")); 
        
        JLabel l0 = new JLabel("CHANGE PASSWORD");
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
        
        // Label 1
        JLabel l1 = new JLabel("Username:");
        l1.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l1.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l1, gbc);

        // Text Field 1 (Pre-filled)
        JTextField tUsername = new JTextField(usernameIn, 50);
        tUsername.setFont(gFont.deriveFont(Font.PLAIN, 21));
        tUsername.setEditable(false); // Lock it so they can't change someone else's pass
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p2.add(tUsername, gbc);

        // Label 2
        JLabel l2 = new JLabel("New Password:");
        l2.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l2.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        p2.add(l2, gbc);

        // Text Field 2
        JTextField tNewPassword = new JTextField(50); 
        tNewPassword.setFont(gFont.deriveFont(Font.PLAIN, 22));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p2.add(tNewPassword, gbc);

        f.add(p2, BorderLayout.CENTER);

        // ---- LOW ----
        JPanel p3 = new JPanel();
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
  
        JButton bChange = new JButton("Change Password");
        styleButton(bChange, breatheFont);
        
        JButton bBack = new JButton("Back");
        styleButton(bBack, breatheFont);

        p3.add(bChange);
        p3.add(Box.createHorizontalStrut(20));
        p3.add(bBack);

        f.add(p3, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // ---- Action Listeners ----

        bChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = tUsername.getText().trim();
                String newPass = tNewPassword.getText().trim();
                
                // Call Service
                LoginService service = new LoginService();
                String result = service.changeUserPassword(user, newPass);

                if ("Success".equals(result)) {
                    JOptionPane.showMessageDialog(f, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new loginPage();
                    f.dispose();
                } else {
                    JOptionPane.showMessageDialog(f, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        bBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new loginPage();
                f.dispose();
            }
        });
    }

    private void styleButton(JButton b, Font font) {
        b.setBackground(Color.decode("#2f77b1")); 
        b.setForeground(Color.WHITE); 
        b.setFont(font.deriveFont(Font.PLAIN, 35));
        b.setMargin(new Insets(10, 30, 5, 30));
    }
}