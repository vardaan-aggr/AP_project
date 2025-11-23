package edu.univ.erp.ui.student;

import edu.univ.erp.util.BREATHEFONT;
import edu.univ.erp.data.ErpCommandRunner;
import java.sql.SQLException;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.table.JTableHeader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

public class gradeFrame {
    public gradeFrame(String username, String role, String in_pass, String roll_no) {
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
        
        JLabel l0 = new JLabel("GRADES");
        l0.setForeground(Color.decode("#dbd3c5"));
        // l0.setFont(gFont.deriveFont(Font.PLAIN, 60));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80f));
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE ----
        JPanel p2 = new JPanel(new BorderLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        p2.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        try {
            String data[][] = ErpCommandRunner.studentGradeHelper(roll_no); 
            if (data == null) {
                System.out.println("\tGoing back to Student Dashboard..");
                new studentDashboard(username, role, in_pass, roll_no);
                f.dispose();
                return;
            }
            String columName[] = {"Course code", "Grade"};
            JTable t = new JTable(data, columName);

            JTableHeader header = t.getTableHeader();
            header.setBackground(Color.decode("#051072"));
            header.setForeground(Color.decode("#dbd3c5"));
            header.setFont(gFont.deriveFont(Font.PLAIN, 18));
            header.setOpaque(true);

            t.setFont(gFont.deriveFont(Font.PLAIN, 16));
            t.setRowHeight(30);
            t.setSelectionBackground(Color.decode("#2f77b1"));
            t.setSelectionForeground(Color.WHITE);
            t.setShowGrid(true);
            t.setGridColor(Color.decode("#051072"));
                    
            JScrollPane sp = new JScrollPane(t);
            sp.getViewport().setBackground(Color.WHITE);
            p2.add(sp, BorderLayout.CENTER);

            f.add(p2, BorderLayout.CENTER);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error registering: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            new studentDashboard(username, role, in_pass, roll_no);
            f.dispose();
        }

        // ---- LOWS ----
        JPanel p3 = new JPanel();
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton b1 = new JButton("Back");
        b1.setBackground(Color.decode("#2f77b1"));
        b1.setForeground(Color.WHITE);
        b1.setFont(breatheFont.deriveFont(Font.PLAIN, 35));
        b1.setMargin(new Insets(10, 30, 5, 30));
        
        p3.add(b1);
        f.add(p3, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // --- Action Listeners ---
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tGoing back to Student Dashboard..");
                new studentDashboard(username, role, in_pass, roll_no);
                f.dispose();
            }
        });
    }
}