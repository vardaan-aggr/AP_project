package edu.univ.erp.ui.common;
import edu.univ.erp.ui.admin.adminDashboard;
import edu.univ.erp.ui.student.studentDashboard;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.ui.instructor.InstructorDashboard;
import edu.univ.erp.service.CatalogService;
import edu.univ.erp.util.BREATHEFONT;

import java.sql.SQLException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;

public class courses {

    public courses(String username, String role, String password, String roll_no) {

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
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80f)); 
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE ----
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.decode("#dbd3c5"));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.decode("#dbd3c5"));
        
        JLabel searchLabel = new JLabel("Search Code:");
        searchLabel.setFont(gFont.deriveFont(Font.PLAIN, 18f));  
        searchLabel.setForeground(Color.decode("#030c5d"));
        
        JTextField tSearchCode = new JTextField(20);
        tSearchCode.setFont(gFont.deriveFont(Font.PLAIN, 18f));
        
        JButton searchButton = new JButton("🔍 Search");
        searchButton.setBackground(Color.decode("#2f77b1"));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(gFont.deriveFont(Font.PLAIN, 16f));
        
        searchPanel.add(searchLabel);
        searchPanel.add(tSearchCode);
        searchPanel.add(searchButton);
        
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        String data[][] = new String[0][0];
        try {
            CatalogService service = new CatalogService();
            String[][] fetchedData = service.getCatalogTableData(); 

            if (fetchedData != null) {
                data = fetchedData;
            }

            if (data.length == 0) {
                JOptionPane.showMessageDialog(null, "No active courses found.", "Information", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("\t (no courses found)");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        String columName[] = {"Code", "Title", "Section", "Credits"};
        
        JTable t = new JTable(data, columName);
        t.setFillsViewportHeight(true);
        
        JTableHeader header = t.getTableHeader();
        header.setBackground(Color.decode("#051072"));
        header.setForeground(Color.decode("#dbd3c5"));
        header.setFont(gFont.deriveFont(Font.PLAIN, 18));
        header.setOpaque(true);
        
        t.setFont(gFont.deriveFont(Font.PLAIN, 16));
        t.setRowHeight(25);
        t.setSelectionBackground(Color.decode("#2f77b1"));
        t.setSelectionForeground(Color.WHITE);
        t.setShowGrid(true);
        t.setGridColor(Color.decode("#051072"));
        
        JScrollPane sp = new JScrollPane(t);
        sp.getViewport().setBackground(Color.WHITE);
        centerPanel.add(sp, BorderLayout.CENTER);

        f.add(centerPanel, BorderLayout.CENTER);

        // ---- BOTTOM ----
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 20));
        p3.setBackground(Color.decode("#dbd3c5"));
        
        JButton bBack = new JButton("Back");
        bBack.setBackground(Color.decode("#2f77b1"));
        bBack.setForeground(Color.WHITE);
        bBack.setFont(breatheFont.deriveFont(Font.PLAIN, 35f));
        bBack.setMargin(new Insets(10, 30, 5, 30));
        
        p3.add(bBack);
        f.add(p3, BorderLayout.SOUTH);        
        f.setVisible(true);

        // --- Action Listeners ---
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = tSearchCode.getText().trim();
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

        bBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("\tGoing Back...");
                if (role.equalsIgnoreCase("student")) {
                    f.dispose();
                    new studentDashboard(username, role, password, roll_no); 
                }
                else if (role.equalsIgnoreCase("instructor")) {
                    f.dispose();
                    new InstructorDashboard(username, role, password, roll_no);
                }
                else if (role.equalsIgnoreCase("admin")) {
                    f.dispose();
                    new adminDashboard(roll_no);
                }
            }
        });
    }
}