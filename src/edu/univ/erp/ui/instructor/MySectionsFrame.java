package edu.univ.erp.ui.instructor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.univ.erp.data.DatabaseConnector;

public class MySectionsFrame {

    public MySectionsFrame(String roll_no) {
        JFrame f ;
        JFrame.setDefaultLookAndFeelDecorated(true);
        f = new JFrame("My Sections");
        f.setSize(800, 600);
        f.setLayout(new BorderLayout(10, 10));
        f.setLocationRelativeTo(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        JLabel l0 = new JLabel("My Sections");
        l0.setBounds(0, 0, 800, 60);
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        l0.setPreferredSize(new Dimension(800, 60));
        f.add(l0, BorderLayout.NORTH);

        // ---- table ----
        String data[][] = dataPull(roll_no);
        String columName[] = {"Course code"};
        JTable t = new JTable(data, columName);
        
        t.getTableHeader().setBackground(Color.decode("#051072"));
        t.getTableHeader().setForeground(Color.decode("#dbd3c5"));
        t.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        t.setFont(new Font("Arial", Font.PLAIN, 12));
        t.setRowHeight(25);
        
        JScrollPane sp = new JScrollPane(t);
        // sp.setBounds(50, 50, 500, 200);        
        sp.setBorder(new EmptyBorder(0, 20, 0, 20));
        f.add(sp, BorderLayout.CENTER);
 
        // --- south ----
        JPanel p1 = new JPanel();
        p1.setBackground(Color.decode("#d8d0c1"));
        JButton backButton = new JButton("Back to Dashboard");
        // backButton.setBounds(200, 300, 200, 30);
        backButton.setBackground(Color.decode("#2f77b1"));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        p1.add(backButton);
        f.add(p1, BorderLayout.SOUTH);


        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new InstructorDashboard(roll_no);
                f.dispose();
            }
        });
    }

    private String[][] dataPull(String roll_no) {
        ArrayList<String[]> arrList = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select course_code FROM sections WHERE roll_no = ?; 
                    """)) {
                statement.setString(1, roll_no); // Replace with actual instructor ID

                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String course_code = resultSet.getString("course_code");
                        arrList.add(new String[]{course_code });
                    } 
                    if (empty) {
                        System.out.println("\t (no data)");
                    }
                }
            } 
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        String[][] strArr = new String[arrList.size()][1];
        arrList.toArray(strArr);
        return strArr;
    }
}
