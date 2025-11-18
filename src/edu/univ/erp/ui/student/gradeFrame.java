package edu.univ.erp.ui.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import edu.univ.erp.data.DatabaseConnector;
import edu.univ.erp.util.BREATHEFONT;


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
        // l0.setFont(gFont.deriveFont(Font.BOLD, 60));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80f));
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE ----
        JPanel p2 = new JPanel(new BorderLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        p2.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        String data[][] = gradePull(roll_no); 
        String columName[] = {"Course code", "Grade"};
        JTable t = new JTable(data, columName);

        JTableHeader header = t.getTableHeader();
        header.setBackground(Color.decode("#051072"));
        header.setForeground(Color.decode("#dbd3c5"));
        header.setFont(gFont.deriveFont(Font.BOLD, 18));
        // header.setFont(new Font("Gabarito Black", Font.BOLD, 18));
        header.setOpaque(true);

        t.setFont(gFont.deriveFont(Font.PLAIN, 16));
        // t.setFont(new Font("Gabarito", Font.PLAIN, 16));
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
                new studentDashboard(username, role, in_pass, roll_no);
                f.dispose();
            }
        });
    }

    private String[][] gradePull(String roll_no) {
        ArrayList<String[]> data = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select course_code, grade FROM grades WHERE roll_no = ?;
                    """)) {
                statement.setString(1, String.valueOf(roll_no));
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String courseCode = resultSet.getString("course_code");
                        String grade = resultSet.getString("grade");
                        data.add(new String[]{courseCode, grade});
                    }
                    if (empty) {
                        JOptionPane.showMessageDialog(null, "Error: no courses in grade table", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("\t (no courses in grade table)");
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error opening grades: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        String[][] strArr = new String[data.size()][2];
        for (int i = 0; i < data.size(); i++) {
            strArr[i][0] = data.get(i)[0];
            strArr[i][1] = data.get(i)[1];
        }
        return strArr;
    }
}