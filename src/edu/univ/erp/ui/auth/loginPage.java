package edu.univ.erp.ui.auth;

import edu.univ.erp.util.BREATHEFONT;
import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.auth.changePassword;
import edu.univ.erp.service.LoginService;
import edu.univ.erp.service.LoginService.ServiceLoginResult;

import edu.univ.erp.ui.student.studentDashboard;
import edu.univ.erp.ui.admin.adminDashboard;
import edu.univ.erp.ui.instructor.InstructorDashboard;

import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class loginPage {
    
    private static String roll_no = "";
    private final LoginService loginService = new LoginService();

    public loginPage() {
        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to make FlatLaf", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Failed to make FlatLaf");
        }
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame f = new JFrame();
        f.setSize(800,600);
        
        // ---- Top ----
        JPanel p1 = new JPanel();    
        p1.setBackground(Color.decode("#051072")); 
        p1.setOpaque(true); 
        JLabel l0 = new JLabel("ERP LOGIN");
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 100));
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- Middle ----
        JPanel p2 = new JPanel();
        p2.setLayout(new GridBagLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        p2.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel l1 = new JLabel("Username:");
        l1.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l1.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l1, gbc);

        JTextField tUsername = new JTextField(50);
        tUsername.setFont(gFont.deriveFont(Font.PLAIN, 21));
        gbc.gridx = 1; gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        p2.add(tUsername, gbc);
        
        JLabel l2 = new JLabel("Password:");
        l2.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l2.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        gbc.fill= GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l2, gbc);
        JPasswordField tPassword = new JPasswordField(50);
        tPassword.setFont(gFont.deriveFont(Font.PLAIN, 22));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        p2.add(tPassword, gbc);
        f.add(p2, BorderLayout.CENTER);
        
        // ---- Low ----
        JPanel p3 = new JPanel();
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        
        JButton b1 = new JButton("LOGIN");
        b1.setBackground(Color.decode("#2f77b1")); 
        b1.setForeground(Color.WHITE); 
        b1.setFont(breatheFont.deriveFont(Font.PLAIN, 35f));
        b1.setMargin(new Insets(10, 30, 5, 30));

        JButton b2 = new JButton("Change Password");
        b2.setBackground(Color.decode("#87c3fa")); 
        b2.setForeground(Color.WHITE); 
        b2.setFont(breatheFont.deriveFont(Font.PLAIN, 30f));
        b2.setMargin(new Insets(10, 30, 5, 30));

        p3.add(b1);
        p3.add(Box.createHorizontalStrut(20)); 
        p3.add(b2);
        
        f.add(p3, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
        tUsername.setText("std1");
        tPassword.setText("mdo1");

        // --- LOGIN BUTTON LOGIC ---
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username_in = tUsername.getText().trim();
                String password_in = new String(tPassword.getPassword());

                ServiceLoginResult result = loginService.attemptLogin(username_in, password_in);

                if (result.status == LoginService.STATUS_SUCCESS) {
                    // --- SUCCESS ---
                    System.out.println("\nLogin Successful");
                    roll_no = result.userDetails.rollNo; 

                    // Open the correct dashboard
                    if (result.userDetails.role.equals("student")) {
                        new studentDashboard(username_in, result.userDetails.role, password_in, roll_no);
                        System.out.println("\tOpening Student Dashboard..");
                    }
                    else if (result.userDetails.role.equals("instructor")) {
                        new InstructorDashboard(username_in, result.userDetails.role, password_in, roll_no);
                        System.out.println("\tOpening Instructor Dashboard");
                    }
                    else if (result.userDetails.role.equals("admin")) {
                        new adminDashboard(roll_no);
                        System.out.println("\tOpening Admin Dashboard");
                    }
                    f.dispose();

                } 
                else if (result.status == LoginService.STATUS_LOCKED) {
                    // --- LOCKED OUT ---
                    long currentTime = System.currentTimeMillis();
                    long endsTime = result.lockoutEndsTimestamp;
                    long timeLeftMs = endsTime - currentTime;
                    long timeLeftSeconds = Math.max(0, (timeLeftMs / 1000) + 1); 
                    
                    JOptionPane.showMessageDialog(f,
                        "Account locked due to too many failed attempts. Try again in " + timeLeftSeconds + " seconds.",
                        "Login Locked", JOptionPane.ERROR_MESSAGE);

                } 
                else if (result.status == LoginService.STATUS_INVALID) {
                    // --- WRONG CREDENTIALS ---
                    JOptionPane.showMessageDialog(f, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);

                } 
                else if (result.status == LoginService.STATUS_ERROR) {
                    // --- DB ERROR ---
                    JOptionPane.showMessageDialog(f, "Database error during login. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- CHANGE PASSWORD BUTTON LOGIC ---
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userForChange = tUsername.getText();
                
                if(userForChange.isEmpty()) {
                     JOptionPane.showMessageDialog(f, "Please enter your Username in the text box first.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                } else {
                    roll_no = userForChange; 
                    new changePassword(roll_no);
                    System.out.println("Change Password clicked for: " + roll_no);
                    f.dispose();
                }
            }
        });
    }

    public static void main(String[] args) {
        new loginPage();
    }
}