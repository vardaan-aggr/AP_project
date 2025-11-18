package edu.univ.erp.ui.admin;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;

import edu.univ.erp.util.modeOps;


public class maintainMode {
    public maintainMode (int rollNo) {
        JFrame f = new JFrame();
        f.setSize(800, 600); 
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));

        JLabel l0 = new JLabel("Maintainance Mode");
        l0.setBounds(0, 0, 800, 60); 
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);

        JButton b1 = new JButton("Turn On");
        b1.setBackground(Color.decode("#2f77b1"));
        b1.setForeground(Color.WHITE);
        b1.setFont(new Font("Arial", Font.BOLD, 14));
        b1.setBounds(100, 100, 600, 50);
        f.add(b1);

        JButton b2 = new JButton("Turn Off");
        b2.setBackground(Color.decode("#2f77b1"));
        b2.setForeground(Color.WHITE);
        b2.setFont(new Font("Arial", Font.BOLD, 14));
        b2.setBounds(100, 170, 600, 50);
        f.add(b2);

        JButton b3 = new JButton("Back");
        b3.setBackground(Color.decode("#2f77b1"));
        b3.setForeground(Color.WHITE);
        b3.setFont(new Font("Arial", Font.BOLD, 14));
        b3.setBounds(100, 240, 600, 50); 
        f.add(b3);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // ---- Action Listeners ----
        b1.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (modeOps.setMaintainMode("true").equals("failure")) {
                    JOptionPane.showMessageDialog(null, "Error: Database Error occured.", "Failure", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Maintainance Mode On..", "Success", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Maintainance Mode On..");
                }
            }
        }) ;

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (modeOps.setMaintainMode("false").equals("failure")) {
                    JOptionPane.showMessageDialog(null, "Error: Database Error occured.", "Failure", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Maintainance Mode Off..", "Success", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Maintainance Mode Off..");
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
}
