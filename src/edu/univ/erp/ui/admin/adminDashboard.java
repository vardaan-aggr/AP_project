package edu.univ.erp.ui.admin;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 

public class adminDashboard {
    public adminDashboard(int rollNo) {
        JFrame f = new JFrame();
        f.setSize(800, 600); 
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));

        JLabel l0 = new JLabel("ADMIN DASHBOARD");
        l0.setBounds(0, 0, 800, 60); 
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#d8d0c4"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);

        JButton b1 = new JButton("Add users");
        b1.setBackground(Color.decode("#2f77b1"));
        b1.setForeground(Color.WHITE);
        b1.setFont(new Font("Arial", Font.BOLD, 14));
        b1.setBounds(100, 100, 600, 50); 
        f.add(b1);

        JButton b2 = new JButton("Create/edit courses and sections");
        b2.setBackground(Color.decode("#2f77b1"));
        b2.setForeground(Color.WHITE);
        b2.setFont(new Font("Arial", Font.BOLD, 14));
        b2.setBounds(100, 190, 600, 50); 
        f.add(b2);

        JButton b3 = new JButton("Assign instructor");
        b3.setBackground(Color.decode("#2f77b1"));
        b3.setForeground(Color.WHITE);
        b3.setFont(new Font("Arial", Font.BOLD, 14));
        b3.setBounds(100, 280, 600, 50); 
        f.add(b3);

        JButton b4 = new JButton("Toggle Maintenance Mode");
        b4.setBackground(Color.decode("#2f77b1"));
        b4.setForeground(Color.WHITE);
        b4.setFont(new Font("Arial", Font.BOLD, 14));
        b4.setBounds(100, 370, 600, 50); 
        f.add(b4);

        JButton b5 = new JButton("Search Database");
        b5.setBackground(Color.decode("#2f77b1"));
        b5.setForeground(Color.WHITE);
        b5.setFont(new Font("Arial", Font.BOLD, 14));
        b5.setBounds(100, 460, 600, 50); 
        f.add(b5);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // ---- Action Listeners ----
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AddUsers(rollNo);
                f.dispose();
            }
        }) ;

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new assignIns(rollNo);
                f.dispose();
            }
        });

        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        b5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new searchDb(rollNo);
                f.dispose();
            }
        });
    }
}