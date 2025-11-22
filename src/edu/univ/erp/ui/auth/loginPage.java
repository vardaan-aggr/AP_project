package edu.univ.erp.ui.auth;

import edu.univ.erp.util.BREATHEFONT;
import edu.univ.erp.data.AuthCommandRunner;
import java.sql.SQLException;

import javax.swing.*;
import org.mindrot.jbcrypt.BCrypt;
import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.data.AuthCommandRunner.loginResult;
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

        JTextField t1 = new JTextField(50);
        t1.setFont(gFont.deriveFont(Font.PLAIN, 21));
        gbc.gridx = 1; gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        p2.add(t1, gbc);
        
        JLabel l2 = new JLabel("Password:");
        l2.setFont(gFont.deriveFont(Font.PLAIN, 24));
        l2.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        gbc.fill= GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l2, gbc);
        JPasswordField t2 = new JPasswordField(50);
        t2.setFont(gFont.deriveFont(Font.PLAIN, 22));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        p2.add(t2, gbc);
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
        
        // For testing purposes only: remove in production
        t1.setText("student1");
        t2.setText("mdo1");

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username_in = t1.getText().trim();
                String password_in = new String(t2.getPassword());
                loginResult lr = new loginResult();
                try {
                    lr = AuthCommandRunner.fetchUser(username_in);
                    if(lr == null) {
                        System.out.println("\t (no data for given username found)");
                        JOptionPane.showMessageDialog(null, "Username not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        if (BCrypt.checkpw(password_in, lr.hashPass)) {
                            System.out.println("\nCorrect Password");
                            if (lr.role.equals("student")) {
                                new studentDashboard(username_in, lr.role, password_in, roll_no);
                                System.out.println("\tOpening Student Dashboard..");
                                f.dispose();
                                return;
                            }
                            else if (lr.role.equals("instructor")) {
                                new InstructorDashboard(username_in, lr.role, password_in, roll_no);
                                System.out.println("\tOpening Instructor Dashboard");
                                f.dispose();
                                return;
                            }
                            else if (lr.role.equals("admin")) {
                                new adminDashboard(roll_no);
                                System.out.println("\tOpening Admin Dashboard");
                                f.dispose();
                                return;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Wrong Password sorry", "Error", JOptionPane.ERROR_MESSAGE);
                            System.out.println("WrongPassword");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new changePassword(roll_no);
                System.out.println("Change Password clicked");
                f.dispose();
            }
        });
    }

    public static void main(String[] args) {
        new loginPage();
    }
}
