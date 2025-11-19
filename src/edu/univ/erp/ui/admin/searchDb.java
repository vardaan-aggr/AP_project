package edu.univ.erp.ui.admin;

import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.ui.admin.searchDB.searchAdm;
import edu.univ.erp.ui.admin.searchDB.searchIns;
import edu.univ.erp.ui.admin.searchDB.searchStd;
import edu.univ.erp.util.BREATHEFONT;

public class searchDb {
    public searchDb(String rollNo) {

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
        p1.setOpaque(true); 
        p1.setBackground(Color.decode("#051072")); 
        
        JLabel l0 = new JLabel("SEARCH DATABASE");
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80f)); 
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE  ----
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        p2.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        Color btnBg = Color.decode("#2f77b1");
        Color btnFg = Color.WHITE;
        Font btnFont = gFont.deriveFont(Font.BOLD, 20f); 
        Dimension btnSize = new Dimension(440, 60);

        gbc.gridy = 0;
        JButton b1 = new JButton("Search Student");
        styleButton(b1, btnBg, btnFg, btnFont, btnSize);
        p2.add(b1, gbc);

        gbc.gridy = 1;
        JButton b2 = new JButton("Search Instructor");
        styleButton(b2, btnBg, btnFg, btnFont, btnSize);
        p2.add(b2, gbc);

        gbc.gridy = 2;
        JButton b3 = new JButton("Search Admin");
        styleButton(b3, btnBg, btnFg, btnFont, btnSize);
        p2.add(b3, gbc);

        f.add(p2, BorderLayout.CENTER);

        // ---- LOWS ----
        JPanel p3 = new JPanel();
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton b4 = new JButton("Back");
        b4.setBackground(Color.decode("#2f77b1")); 
        b4.setForeground(Color.WHITE); 
        b4.setFont(breatheFont.deriveFont(Font.PLAIN, 35f)); 
        b4.setMargin(new Insets(10, 30, 5, 30));
        
        p3.add(b4);
        f.add(p3, BorderLayout.SOUTH);

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
    void styleButton(JButton button, Color bg, Color fg, Font font, Dimension size) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(font);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
    }
}