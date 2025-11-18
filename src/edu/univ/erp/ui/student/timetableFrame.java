package edu.univ.erp.ui.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import edu.univ.erp.data.DatabaseConnector;
import edu.univ.erp.util.BREATHEFONT;

public class timetableFrame {
    public timetableFrame(String username, String role, String in_pass, String roll_no) {
                
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
        
        JLabel l0 = new JLabel("TIMETABLE");
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80));
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE ----
        JPanel p2 = new JPanel(new BorderLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        p2.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        String data[][] = dataPull(roll_no); 
        String columName[] = {"Code", "Day_Time", "Room"};
        
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

        // ---- LOWS ----
        JPanel p3 = new JPanel();
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));

        JButton b1 = new JButton("Back");
        b1.setBackground(Color.decode("#2f77b1"));
        b1.setForeground(Color.WHITE);
        b1.setFont(breatheFont.deriveFont(Font.PLAIN, 35f));
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

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private String[][] dataPull(String roll_no) {
        ArrayList<String[]> arrList1 = new ArrayList<>();
        ArrayList<String[]> arrList2 = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select course_code, section FROM enrollments WHERE roll_no = ? and status = ?;
                    """)) {
                statement.setString(1, String.valueOf(roll_no));
                statement.setString(2, "enrolled");
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String courseCode = resultSet.getString("course_code");
                        String section = resultSet.getString("section");
                        arrList1.add(new String[]{courseCode, section});
                    }
                    if (empty) {
                        JOptionPane.showMessageDialog(null, "Error no enrollments", "Error", JOptionPane.ERROR_MESSAGE); 
                        System.out.println("\t (no enrollments)");
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error opening enrolled courses: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        for (int i = 0; i < arrList1.size(); i++) {
            String v1 = arrList1.get(i)[0];
            String v2 = arrList1.get(i)[1];
            try (Connection connection = DatabaseConnector.getErpConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("""
                            Select day_time, room FROM sections WHERE course_code = ? AND section = ?;
                        """)) {
                    statement.setString(1, v1);
                    statement.setString(2, v2);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        boolean empty = true;
                        while (resultSet.next()) {
                            empty = false;
                            String day_time = resultSet.getString("day_time");
                            String room = resultSet.getString("room");
                            arrList2.add(new String[]{v1, day_time, room});
                        }
                        if (empty) {
                            JOptionPane.showMessageDialog(null, "Error no section with given inputs exists", "Error", JOptionPane.ERROR_MESSAGE); 
                            System.out.println("\t (no section with given inputs exists)");
                        }
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error opening sections: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        String[][] strArr = new String[arrList2.size()][3];
        arrList2.toArray(strArr);
        return strArr;
    }
}