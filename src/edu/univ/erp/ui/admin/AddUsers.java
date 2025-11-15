package edu.univ.erp.ui.admin;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddUsers {
    public AddUsers() {
        JFrame f = new JFrame();
        f.setSize(800, 600);
        f.setLayout(null);
        
        JLabel l0 = new JLabel("ADD USER");
        l0.setBounds(0, 0, 450, 50);
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#d8d0c4"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);
        
        JLabel l1 = new JLabel("Role: ");
        l1.setBounds(50, 80, 100, 30);
        f.add(l1);
        JTextField t1 = new JTextField(50);
        t1.setBounds(150, 80, 250, 30);
        f.add(t1);
        String role = t1.getText().toLowerCase();

        if (role == "student") {
            new addStd();
            f.dispose();
        } else if (role == "instructor") {
            new addIns();
            f.dispose();
        } else if (role == "admin") {
            
        } else {
            JOptionPane.showMessageDialog(null, "ENTER ONLY Student, Instructor, Admin", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
