package edu.univ.erp.ui.student;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import edu.univ.erp.data.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class courseCatalogFrame {
    public courseCatalogFrame(String username, String role, String in_pass, String rollNo) {
        JFrame f = new JFrame();
        f.setSize(800, 600);
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));

        JLabel l0 = new JLabel("COURSE CATALOG");
        l0.setBounds(0, 0, 800, 60);
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#d8d0c4"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);

        String[][] data = dataPull(rollNo);
        String[] columNames = {"Course Code", "Title", "Section", "Credits"};
        JTable t = new JTable(data, columNames);

        t.getTableHeader().setBackground(Color.decode("#051072"));
        t.getTableHeader().setForeground(Color.decode("#d8d0c4"));
        t.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        t.setFont(new Font("Arial", Font.PLAIN, 12));
        t.setRowHeight(25);

        JScrollPane sp = new JScrollPane(t);
        sp.setBounds(20, 80, 760, 440);
        f.add(sp);

        JButton b1 = new JButton("Back");
        b1.setBounds(660, 540, 120, 40);
        b1.setBackground(Color.decode("#2f77b1"));
        b1.setForeground(Color.WHITE);
        b1.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(b1);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // --- Action Listeners ---
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Going back to Student Dashboard..");
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