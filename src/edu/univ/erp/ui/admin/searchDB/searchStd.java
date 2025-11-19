package edu.univ.erp.ui.admin.searchDB;

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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;

import edu.univ.erp.data.DatabaseConnector;
import edu.univ.erp.ui.admin.adminDashboard;
import edu.univ.erp.util.BREATHEFONT;


public class searchStd {
    public searchStd(String roll_no_inp) {
        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame();
        f.setSize(800, 600);
        f.setLayout(new BorderLayout()); // Use BorderLayout

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setOpaque(true); 
        p1.setBackground(Color.decode("#051072")); 
        
        JLabel l0 = new JLabel("STUDENT INFO");
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80f)); // Adjusted size
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE ----
        String[][] data = dataPull();
        String[] columName = {"Username", "Roll No", "Program", "Year"};
        
        JTable t = new JTable(data, columName);
        t.setFillsViewportHeight(true);
        
        JTableHeader header = t.getTableHeader();
        header.setBackground(Color.decode("#051072"));
        header.setForeground(Color.decode("#dbd3c5"));
        header.setFont(gFont.deriveFont(Font.BOLD, 18));
        header.setOpaque(true);
        
        t.setFont(gFont.deriveFont(Font.PLAIN, 16));
        t.setRowHeight(30);
        t.setSelectionBackground(Color.decode("#2f77b1"));
        t.setSelectionForeground(Color.WHITE);
        t.setShowGrid(true);
        t.setGridColor(Color.decode("#051072"));

        JScrollPane sp = new JScrollPane(t);
        sp.getViewport().setBackground(Color.WHITE);
        f.add(sp, BorderLayout.CENTER);

        // ---- LOWS ----
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 20));
        p3.setBackground(Color.decode("#dbd3c5"));
        
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
                System.out.println("\tGoing back to admin dashboard..");
                new adminDashboard(roll_no_inp);
                f.dispose();
            }
        });
    }

    private String[][] dataPull() {
        ArrayList<String[]> data = new ArrayList<>();
        String bedebop = """
                        Select auth_db.auth_table.roll_no, auth_db.auth_table.username, erp_db.students.program, erp_db.students.year 
                        From auth_db.auth_table
                        Join erp_db.students ON auth_db.auth_table.roll_no = erp_db.students.roll_no
                        Where auth_db.auth_table.role = ?;
                    """;
        try (Connection connection = DatabaseConnector.getAuthConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(bedebop)) {
                statement.setString(1, "student");
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String rollNo = resultSet.getString("roll_no");
                        String username = resultSet.getString("username");
                        String program = resultSet.getString("program");
                        String year = resultSet.getString("year");
                        data.add(new String[]{username, rollNo, program, year});
                    }
                    if (empty) {
                        JOptionPane.showMessageDialog(null, "Error: No student in both database together", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("\t (Error: No student in both database together)");
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error opening student info from database.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error opening student info from database: " + ex);
            ex.printStackTrace();
        }

        String[][] strArr = new String[data.size()][4];
        data.toArray(strArr);
        return strArr;
    }
}