package edu.univ.erp.ui.instructor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.univ.erp.data.DatabaseConnector;
import edu.univ.erp.util.BREATHEFONT;

public class MySectionsFrame {

    public MySectionsFrame(String roll_no) {

        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame("My Sections");
        f.setSize(800, 600);
        f.setLayout(new BorderLayout());
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setOpaque(true); 
        p1.setBackground(Color.decode("#051072")); 
        
        JLabel l0 = new JLabel("MY SECTIONS");
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80f)); 
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- CENTER ----
        JPanel p2 = new JPanel(new BorderLayout());
        p2.setBackground(Color.decode("#dbd3c5"));
        p2.setBorder(new EmptyBorder(20, 50, 20, 50));

        String data[][] = dataPull(roll_no);
        String columName[] = {"Course Code"};
        
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
        p2.add(sp, BorderLayout.CENTER);
        
        f.add(p2, BorderLayout.CENTER);
 
        // ---- LOWS ----
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 20));
        p3.setBackground(Color.decode("#dbd3c5")); 
        
        JButton b1 = new JButton("Back to Dashboard");
        b1.setBackground(Color.decode("#2f77b1"));
        b1.setForeground(Color.WHITE);
        b1.setFont(breatheFont.deriveFont(Font.PLAIN, 35f));
        b1.setMargin(new Insets(10, 30, 5, 30));
        
        p3.add(b1);
        f.add(p3, BorderLayout.SOUTH);

        f.setVisible(true);


        b1.addActionListener(new ActionListener() {
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
