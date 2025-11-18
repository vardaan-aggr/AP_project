package edu.univ.erp.ui.admin.searchDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;

import edu.univ.erp.data.DatabaseConnector;
import edu.univ.erp.ui.admin.adminDashboard;

public class searchAdm {
    public searchAdm(int roll_no_inp) {
        JFrame f = new JFrame();
        f.setSize(800, 600);
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));

        JLabel l0 = new JLabel("Admin Info");
        l0.setBounds(0, 0, 800, 60);
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);

        String[][] data = dataPull();
        String[] columName = {"Username", "Roll No"};
        JTable t = new JTable(data, columName);

        t.getTableHeader().setBackground(Color.decode("#051072"));
        t.getTableHeader().setForeground(Color.decode("#dbd3c5"));
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
                System.out.println("\tGoing back to Admin Dashboard..");
                new adminDashboard(roll_no_inp);
                f.dispose();
            }
        });
    }

    private String[][] dataPull() {
        ArrayList<String[]> data = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getAuthConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select roll_no, username FROM auth_table WHERE role = ?;
                    """)) {
                statement.setString(1, "admin");
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String rollNo = resultSet.getString("roll_no");
                        String username = resultSet.getString("username");
                        data.add(new String[]{username, rollNo});
                    }
                    if (empty) {
                        JOptionPane.showMessageDialog(null, "Error: No admin in database", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("\t (No admin in database)");
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "A database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error fetching admin data from databases: " + ex);
            ex.printStackTrace();
        }

        String[][] strArr = new String[data.size()][2];
        data.toArray(strArr);
        return strArr;
    }
}