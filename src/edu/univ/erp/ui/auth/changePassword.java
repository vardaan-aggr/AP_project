package edu.univ.erp.ui.auth;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import edu.univ.erp.service.LoginService;
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

        // ---- MIDDLE (Aligned to match loginPage) ----
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        
        // Label 1
        JLabel l1 = new JLabel("Username:");
        l1.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l1.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 0;
        p2.add(l1, gbc);

        // Text Field 1 (Pre-filled, reduced size to 20 to match login)
        JTextField tUsername = new JTextField(usernameIn, 20);
        tUsername.setFont(gFont.deriveFont(Font.PLAIN, 21));
        tUsername.setEditable(false); 
        gbc.gridx = 1; gbc.gridy = 0;
        p2.add(tUsername, gbc);

        // Label 2
        JLabel l2 = new JLabel("New Password:");
        l2.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l2.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 1;
        p2.add(l2, gbc);

        // Text Field 2
        JTextField tNewPassword = new JTextField(20); 
        tNewPassword.setFont(gFont.deriveFont(Font.PLAIN, 22));
        gbc.gridx = 1; gbc.gridy = 1;
        p2.add(tNewPassword, gbc);

        f.add(p2, BorderLayout.CENTER);

        // ---- LOW (Buttons aligned) ----
        JPanel p3 = new JPanel();
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));

        JButton bChange = new JButton("Change Password");
        // Primary Button Style (Using the style from the previous fix)
        bChange.setBackground(Color.decode("#87c3fa")); 
        bChange.setForeground(Color.WHITE); 
        bChange.setFont(breatheFont.deriveFont(Font.PLAIN, 32));
        bChange.setMargin(new Insets(10, 30, 5, 30));

        JButton bBack = new JButton("Back");
        // Secondary Button Style (Using the style from the previous fix)
        bBack.setBackground(Color.decode("#2f77b1")); 
        bBack.setForeground(Color.WHITE); 
        bBack.setFont(breatheFont.deriveFont(Font.PLAIN, 32));
        bBack.setMargin(new Insets(10, 30, 5, 30));

        // Swapped the order here: Back first, then Change Password
        p3.add(bBack);
        p3.add(Box.createHorizontalStrut(20));
        p3.add(bChange);

        f.add(p3, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // ---- Action Listeners ----

        bChange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = tUsername.getText().trim();
                String newPass = tNewPassword.getText().trim();
                
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
}