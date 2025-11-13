package edu.univ.erp.ui.student;

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


public class gradeFrame {
    public gradeFrame(String username, String role, String in_pass, String roll_no) {
        JFrame f = new JFrame();
        // Updated frame size
        f.setSize(800, 600);
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));

        JLabel l0 = new JLabel("GRADES");
        // Updated banner size
        l0.setBounds(0, 0, 800, 60);
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#d8d0c4"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);

        String[][] data = gradePull(roll_no);
        String[] columName = {"Course code", "Grade"};
        JTable t = new JTable(data, columName);

        t.getTableHeader().setBackground(Color.decode("#051072"));
        t.getTableHeader().setForeground(Color.decode("#d8d0c4"));
        t.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        t.setFont(new Font("Arial", Font.PLAIN, 12));
        t.setRowHeight(25);

        JScrollPane sp = new JScrollPane(t);
        // Updated scroll pane bounds for new frame size
        sp.setBounds(20, 80, 760, 440);
        f.add(sp);

        JButton b1 = new JButton("Back");
        // Updated button bounds for new frame size
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