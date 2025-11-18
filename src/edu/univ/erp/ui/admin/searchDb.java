package edu.univ.erp.ui.admin;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import edu.univ.erp.ui.admin.searchDB.searchAdm;
import edu.univ.erp.ui.admin.searchDB.searchIns;
import edu.univ.erp.ui.admin.searchDB.searchStd;
import edu.univ.erp.ui.student.studentDashboard;

public class searchDb {
    public searchDb(int rollNo) {
        JFrame f = new JFrame();
        f.setSize(800, 600);
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));
        
        JLabel l0 = new JLabel("Search DataBase");
        l0.setBounds(0, 0, 800, 60); 
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);

        JLabel l1 = new JLabel("Where to look at?");
        l1.setBounds(50, 80, 100, 30);
        f.add(l1);
        // JTextField t1 = new JTextField(50);
        // t1.setBounds(150, 80, 250, 30);
        // f.add(t1);
        
        JButton b1 = new JButton("Student");
        b1.setBounds(150, 130, 100, 30);
        b1.setBackground(Color.decode("#2f77b1")); 
        b1.setForeground(Color.WHITE); 
        b1.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b1);

        JButton b2 = new JButton("Instructor");
        b2.setBounds(260, 130, 100, 30);
        b2.setBackground(Color.decode("#2f77b1")); 
        b2.setForeground(Color.WHITE); 
        b2.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b2);

        JButton b3 = new JButton("Admin");
        b3.setBounds(370, 130, 100, 30);
        b3.setBackground(Color.decode("#2f77b1")); 
        b3.setForeground(Color.WHITE); 
        b3.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b3);

        JButton b4 = new JButton("Back");
        b4.setBounds(660, 540, 120, 40);
        b4.setBackground(Color.decode("#2f77b1"));
        b4.setForeground(Color.WHITE);
        b4.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b4);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // ---- Action Listeners ----
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tGoing to Search Student Dashboard..");
                new searchStd(rollNo);
                f.dispose();
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tGoing to Search Instructor Dashboard..");
                new searchIns(rollNo);
                f.dispose();
            }
        });

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tGoing to Search Admin Dashboard..");
                new searchAdm(rollNo);
                f.dispose();
            }
        });

        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tGoing to Admin Dashboard..");
                new adminDashboard(rollNo);
                f.dispose();
            }
        });
    }
}