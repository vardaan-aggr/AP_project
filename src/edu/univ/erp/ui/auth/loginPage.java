package edu.univ.erp.ui.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import org.mindrot.jbcrypt.BCrypt;
import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.data.DatabaseConnector;
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

import edu.univ.erp.util.BREATHEFONT;

public class loginPage {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to make FlatLaf", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Failed to make FlatLaf");
        }

        Font breatheFont = BREATHEFONT.fontGen();
        JFrame f = new JFrame();
        f.setSize(800,600);
        
    // ---- Top ----
        JPanel p1 = new JPanel();    
        // p1.setBorder(BorderFactory.createEmptyBorder(00, 00, 00, 00));
        p1.setBackground(Color.decode("#051072")); 

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

        // Add 5px of padding around all components
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);

        JLabel l1 = new JLabel("Username: ");
        l1.setFont(new Font("Gabarito Black", Font.PLAIN, 24));
        l1.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l1, gbc);

        JTextField t1 = new JTextField(50);
        t1.setFont(new Font("Gabarito", Font.PLAIN, 21));
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        p2.add(t1, gbc);
        
        JLabel l2 = new JLabel("Password: ");
        l2.setFont(new Font("Gabarito Black", Font.PLAIN, 24));
        l2.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill= GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        p2.add(l2, gbc);
        JPasswordField t2 = new JPasswordField();
        t2.setFont(new Font("Gabarito", Font.PLAIN, 22));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        p2.add(t2, gbc);

        f.add(p2, BorderLayout.CENTER);
        
    // ---- Low ----
        JPanel p3 = new JPanel();
        JButton b1 = new JButton("LOGIN");
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        b1.setBackground(Color.decode("#2f77b1")); 
        b1.setForeground(Color.WHITE); 
        b1.setFont(breatheFont.deriveFont(Font.PLAIN, 35f));
        b1.setMargin(new java.awt.Insets(10, 30, 5, 30));
        p3.add(b1);
        f.add(p3, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
        // For testing purposes only: remove in production
        t1.setText("instructor1");
        t2.setText("instructor1");

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username_input = t1.getText();
                String password_input = new String(t2.getPassword());
                try (Connection connection = DatabaseConnector.getAuthConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("""
                                Select hash_password, role, roll_no FROM auth_table WHERE username = ?; 
                            """)) {
                        statement.setString(1, username_input);
                        try (ResultSet resultSet = statement.executeQuery()) {
                            boolean empty = true;
                            while (resultSet.next()) {
                                empty = false;
                                String hash_pass_db = resultSet.getString("hash_password");
                                String role_db = resultSet.getString("role");
                                String roll_no = resultSet.getString("roll_no");
                                // System.out.println("Billi kre meow meow 🙀: "+ hash_pass_db);
                                if (BCrypt.checkpw(password_input, hash_pass_db)) {
                                    System.out.println("\nCorrect Password");
                                    if (role_db.equals("student")) {
                                        new studentDashboard(username_input, role_db, password_input, roll_no);
                                        System.out.println("\tOpening Student Dashboard..");
                                        f.dispose();
                                    }
                                    else if (role_db.equals("instructor")) {
                                        new InstructorDashboard(roll_no);
                                        System.out.println("\tOpening Instructor Dashboard");
                                        f.dispose();
                                    }
                                    else if (role_db.equals("admin")) {
                                        new adminDashboard(Integer.parseInt(roll_no));
                                        System.out.println("\tOpening Admin Dashboard");
                                        f.dispose();
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Wrong Password sorry", "Error", JOptionPane.ERROR_MESSAGE);
                                    System.out.println("WrongPassword");
                                }
                            } 
                            if (empty) {
                                System.out.println("\t (no data)");
                            }
                        }
                    } 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}