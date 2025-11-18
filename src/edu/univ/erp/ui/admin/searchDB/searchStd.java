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
        l0.setForeground(Color.decode("#d8d0c4"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);

        String[][] data = dataPull();
        String[] columName = {"Username", "Roll No", "Program", "Year"};
        JTable t = new JTable(data, columName);

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
                statement.setString(1, "student");
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String rollNo = resultSet.getString("roll_no");
                        String username = resultSet.getString("username");
                        try (Connection connection2 = DatabaseConnector.getErpConnection()) {
                            try (PreparedStatement statement1 = connection2.prepareStatement("""
                                        Select program, year FROM students where roll_no = ?;
                                    """)) {
                                statement1.setString(1, String.valueOf(rollNo));
                                try (ResultSet resultSet2 = statement1.executeQuery()) {
                                    boolean empty1 = true;
                                    while (resultSet2.next()) {
                                        empty1 = false;
                                        String program = resultSet2.getString("program");
                                        String year = resultSet2.getString("year");
                                        data.add(new String[]{username, rollNo, program, year});
                                    }
                                    if (empty1) {
                                        JOptionPane.showMessageDialog(null, "Error: no student in erp database", "Error", JOptionPane.ERROR_MESSAGE);
                                        System.out.println("\t (no student in erp database)");
                                    }
                                }
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Error opening students from erp: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    }
                    if (empty) {
                        JOptionPane.showMessageDialog(null, "Error: no student in auth database", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("\t (no student in auth database)");
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error opening student from auth: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        String[][] strArr = new String[data.size()][4];
        for (int i = 0; i < data.size(); i++) {
            strArr[i][0] = data.get(i)[0];
            strArr[i][1] = data.get(i)[1];
            strArr[i][2] = data.get(i)[2];
            strArr[i][3] = data.get(i)[3];
        }
        return strArr;
    }
}