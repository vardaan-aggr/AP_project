package edu.univ.erp.ui.common;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.ui.instructor.InstructorDashboard;
import edu.univ.erp.data.DatabaseConnector;
import edu.univ.erp.util.BREATHEFONT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;

public class AllCourses {

    // Note: If you need to go back to StudentDashboard, you might need to pass 'username' and 'password' here too.
    public AllCourses(String roll_no, String role) {

        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame("Course Catalog");
        f.setSize(800, 600);
        f.setLayout(new BorderLayout());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.setLocationRelativeTo(null);

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setOpaque(true); 
        p1.setBackground(Color.decode("#051072")); 
        
        JLabel l0 = new JLabel("COURSE CATALOG");
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 60f)); 
        l0.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE ----
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.decode("#dbd3c5"));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // 1. Search Bar Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.decode("#dbd3c5"));
        
        JLabel searchLabel = new JLabel("Search Code: ");
        searchLabel.setFont(gFont.deriveFont(Font.BOLD, 18f));
        searchLabel.setForeground(Color.decode("#051072"));
        
        JTextField t1 = new JTextField(20);
        t1.setFont(gFont.deriveFont(Font.PLAIN, 18f));
        
        JButton searchButton = new JButton("🔍 Search");
        searchButton.setBackground(Color.decode("#2f77b1"));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(gFont.deriveFont(Font.BOLD, 16f));
        
        searchPanel.add(searchLabel);
        searchPanel.add(t1);
        searchPanel.add(searchButton);
        
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        String data[][] = dataPull();
        String columName[] = {"Code", "Title", "Section", "Credits"};
        
        JTable t = new JTable(data, columName);
        t.setFillsViewportHeight(true);
        
        JTableHeader header = t.getTableHeader();
        header.setBackground(Color.decode("#051072"));
        header.setForeground(Color.decode("#dbd3c5"));
        header.setFont(gFont.deriveFont(Font.BOLD, 16));
        header.setOpaque(true);
        
        t.setFont(gFont.deriveFont(Font.PLAIN, 14));
        t.setRowHeight(25);
        t.setSelectionBackground(Color.decode("#2f77b1"));
        t.setSelectionForeground(Color.WHITE);
        t.setGridColor(Color.LIGHT_GRAY);

        JScrollPane sp = new JScrollPane(t);
        sp.getViewport().setBackground(Color.WHITE);
        centerPanel.add(sp, BorderLayout.CENTER);

        f.add(centerPanel, BorderLayout.CENTER);

        // ---- BOTTOM ----
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 20));
        p3.setBackground(Color.decode("#dbd3c5"));
        
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.decode("#2f77b1"));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(breatheFont.deriveFont(Font.PLAIN, 24f));
        backButton.setMargin(new Insets(5, 20, 5, 20));
        
        p3.add(backButton);
        f.add(p3, BorderLayout.SOUTH);
        
        f.setVisible(true);


        // --- Action Listeners ---
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = t1.getText().trim();
                if(input.isEmpty()) return;

                boolean found = false;
                for (int i = 0; i < t.getRowCount(); i++) {
                    String code = t.getValueAt(i, 0).toString(); 
                    if (code.equalsIgnoreCase(input)) {
                        t.setRowSelectionInterval(i, i);
                        t.scrollRectToVisible(t.getCellRect(i, 0, true));
                        found = true;
                        break;
                    }
                }
                
                if (!found) {
                    JOptionPane.showMessageDialog(f, "Course not found: " + input, "Result", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tGoing Back...");
                if (role.equalsIgnoreCase("student")) {
                    f.dispose();
                    // Note: studentDashboard requires (username, role, pass, roll_no). 
                    // You may need to pass these to AllCourses to use them here.
                    // new studentDashboard(..., ..., ..., roll_no); 
                }
                else if (role.equalsIgnoreCase("instructor")) {
                    f.dispose();
                    new InstructorDashboard(roll_no);
                }
                else if (role.equalsIgnoreCase("admin")) {
                    f.dispose();
                    // new adminDashboard(Integer.parseInt(roll_no));
                }
            }
        });
    }

    private String[][] dataPull() {
        ArrayList<String[]> arrList = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select course_code, title, section, credits FROM courses; 
                    """)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String course_code = resultSet.getString("course_code");
                        String title = resultSet.getString("title");
                        String section = resultSet.getString("section");
                        String credits = resultSet.getString("credits");
             
                        arrList.add(new String[]{course_code, title, section, credits});
                    } 
                    if (empty) {
                        System.out.println("\t (no data)");
                    }
                }
            } 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        String[][] strArr = new String[arrList.size()][4];
        arrList.toArray(strArr);
        return strArr;
    }    
}