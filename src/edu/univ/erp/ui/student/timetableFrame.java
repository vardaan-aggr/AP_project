package edu.univ.erp.ui.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;

import edu.univ.erp.data.DatabaseConnector;

public class timetableFrame {
    public timetableFrame(String username, String role, String in_pass, String roll_no) {
        JFrame f = new JFrame();
        f.setSize(800, 600);
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));

        JLabel l0 = new JLabel("TIMETABLE");
        l0.setBounds(0, 0, 800, 60);
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#d8d0c4"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);

        String data[][] = dataPull(roll_no);
        String columName[] = {"Code", "Day_Time", "Room"};
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