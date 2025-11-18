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


public class searchStd {
    public searchStd(int roll_no_inp) {
        JFrame f = new JFrame();
        f.setSize(800, 600);
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));

        JLabel l0 = new JLabel("Student Info");
        l0.setBounds(0, 0, 800, 60);
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);

        String[][] data = dataPull();
        String[] columName = {"Username", "Roll No", "Program", "Year"};
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