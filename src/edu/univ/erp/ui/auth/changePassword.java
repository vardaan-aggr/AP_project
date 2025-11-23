package edu.univ.erp.ui.auth;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

import edu.univ.erp.data.DatabaseConnector;
import edu.univ.erp.util.BREATHEFONT;
import edu.univ.erp.util.HashGenerator;

public class changePassword {
    public changePassword(String rollNo) {

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
        
        JLabel l1 = new JLabel("Roll No:");
        l1.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l1.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l1, gbc);

        JTextField tRollNo = new JTextField(50);
        tRollNo.setFont(gFont.deriveFont(Font.PLAIN, 21));
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        p2.add(tRollNo, gbc);

        JLabel l2 = new JLabel("New Password:");
        l2.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l2.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l2, gbc);

        JTextField tNewPassword = new JTextField(50); 
        tNewPassword.setFont(gFont.deriveFont(Font.PLAIN, 22));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        p2.add(tNewPassword, gbc);

        f.add(p2, BorderLayout.CENTER);

        // ---- LOW ----
        JPanel p3 = new JPanel();
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
  
        JButton b1 = new JButton("Change Password");
        b1.setBackground(Color.decode("#2f77b1")); 
        b1.setForeground(Color.WHITE); 
        b1.setFont(breatheFont.deriveFont(Font.PLAIN, 35));
        b1.setMargin(new Insets(10, 30, 5, 30));
        
        JButton b2 = new JButton("Back");
        b2.setBackground(Color.decode("#2f77b1")); 
        b2.setForeground(Color.WHITE); 
        b2.setFont(breatheFont.deriveFont(Font.PLAIN, 35));
        b2.setMargin(new Insets(10, 30, 5, 30));

        p3.add(b1);
        p3.add(Box.createHorizontalStrut(20));
        p3.add(b2);

        f.add(p3, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // ---- Action Listeners ----

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DatabaseConnector.getAuthConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("""
                                update auth_table set hash_password = ? where roll_no = ?;
                            """)) {
                        statement.setString(2, tRollNo.getText().trim());
                        String hashedPass = HashGenerator.makeHash(tNewPassword.getText().trim());
                        statement.setString(1, hashedPass);
                        int rowsUpdated = statement.executeUpdate();
                        if (rowsUpdated == 0) {
                            JOptionPane.showMessageDialog(null, "Error: Couldn't Change Password in database.", "Error", JOptionPane.ERROR_MESSAGE);
                            System.out.println("Error: Couldn't Change Password in database.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Successfully changed password.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            System.out.println("Successfully changed password.");
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error while changing passsword: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Error while changing passsword: " + ex);
                    ex.printStackTrace();
                }
                new loginPage();
                f.dispose();
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new loginPage();
                f.dispose();
            }
        });
    }
}
