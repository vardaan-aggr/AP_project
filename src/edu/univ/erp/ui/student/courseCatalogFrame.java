package edu.univ.erp.ui.student;

import javax.swing.*;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import edu.univ.erp.data.DatabaseConnector;
import edu.univ.erp.util.BREATHEFONT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class courseCatalogFrame {
    public courseCatalogFrame(String username, String role, String in_pass, String rollNo) {
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

        // ---- TOP  ----
        JPanel p1 = new JPanel();
        p1.setOpaque(true); 
        p1.setBackground(Color.decode("#051072")); 
        
        JLabel l0 = new JLabel("COURSE CATALOG");
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80)); 
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE  ----
        JPanel p2 = new JPanel(new BorderLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        p2.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        String[][] data = dataPull(rollNo);
        String[] columNames = {"Course Code", "Title", "Section", "Credits"};
        JTable t = new JTable(data, columNames);

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
                new studentDashboard(username, role, in_pass, rollNo);
                f.dispose();
            }
        });
    }

    public String[][] dataPull(String rollNo) {
        ArrayList<String[]> arrList1 = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select * FROM courses;
                    """)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String courseCode = resultSet.getString("course_code");
                        String title = resultSet.getString("title");
                        String section = resultSet.getString("section");
                        String credits = resultSet.getString("credits");
                        arrList1.add(new String[]{courseCode, section, title, credits});
                    }
                    if (empty) {
                        JOptionPane.showMessageDialog(null, "Error: no courses enrolled", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("\t (no courses enrolled)");
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error opening courses: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        String[][] strArray = new String[arrList1.size()][4];
        arrList1.toArray(strArray);
        return strArray;
    }
}