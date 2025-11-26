package edu.univ.erp.ui.instructor;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;
import java.awt.*;

import edu.univ.erp.ui.common.catalog;
import edu.univ.erp.util.BREATHEFONT;
import edu.univ.erp.service.InstructorService;

public class InstructorDashboard {
    
    public InstructorDashboard(String username, String role, String password, String roll_no) {
        
        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame("Instructor Dashboard");
        f.setSize(800, 600);
        f.setLayout(new BorderLayout());

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setOpaque(true); 
        p1.setBackground(Color.decode("#051072")); 
        
        JLabel l0 = new JLabel("INSTRUCTOR DASHBOARD");
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80f)); 
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE ----
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        p2.setBorder(BorderFactory.createEmptyBorder(75, 75, 75, 75));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.BOTH; 
        gbc.weightx = 1.0; 
        gbc.weighty = 1.0; 
        
        // Match button size to Student Dashboard
        Dimension buttonSize = new Dimension(280, 80);

        // 1. My Sections (Row 0, Col 0)
        gbc.gridx = 0; gbc.gridy = 0;
        JButton bMySections = new JButton("My Sections");
        bMySections.setPreferredSize(buttonSize);
        bMySections.setMargin(new Insets(10, 30, 5, 30));
        bMySections.setBackground(Color.decode("#2f77b1")); 
        bMySections.setForeground(Color.WHITE); 
        bMySections.setFont(gFont.deriveFont(Font.PLAIN, 21f));
        p2.add(bMySections, gbc);

        // 2. Compute Grades (Row 0, Col 1)
        gbc.gridx = 1; gbc.gridy = 0;
        JButton bComputeGrade = new JButton("Compute Final Grades");
        bComputeGrade.setPreferredSize(buttonSize);
        bComputeGrade.setMargin(new Insets(10, 30, 5, 30));
        bComputeGrade.setBackground(Color.decode("#2f77b1")); 
        bComputeGrade.setForeground(Color.WHITE); 
        bComputeGrade.setFont(gFont.deriveFont(Font.PLAIN, 21f));
        p2.add(bComputeGrade, gbc);
    
        // 3. Class Statistics (Row 1, Col 0)
        gbc.gridx = 0; gbc.gridy = 1;
        JButton bClassStats = new JButton("Class Statistics");
        bClassStats.setPreferredSize(buttonSize);
        bClassStats.setMargin(new Insets(10, 30, 5, 30));
        bClassStats.setBackground(Color.decode("#2f77b1")); 
        bClassStats.setForeground(Color.WHITE); 
        bClassStats.setFont(gFont.deriveFont(Font.PLAIN, 21f));
        p2.add(bClassStats, gbc);
        
        // 4. Catalog (Row 1, Col 1)
        gbc.gridx = 1; gbc.gridy = 1;
        JButton bCatalog = new JButton("Courses/Sections");
        bCatalog.setPreferredSize(buttonSize);
        bCatalog.setMargin(new Insets(10, 30, 5, 30));
        bCatalog.setBackground(Color.decode("#2f77b1")); 
        bCatalog.setForeground(Color.WHITE); 
        bCatalog.setFont(gFont.deriveFont(Font.PLAIN, 21f));
        p2.add(bCatalog, gbc);

        f.add(p2, BorderLayout.CENTER);

        // ---- BOTTOM ----
        JPanel p3 = new JPanel();
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));

        // Notifications Button (Moved to bottom to match Student Dashboard)
        JButton bNotify = new JButton("Notifications");
        bNotify.setBackground(Color.decode("#87c3fa")); 
        bNotify.setForeground(Color.WHITE);
        bNotify.setFont(breatheFont.deriveFont(Font.PLAIN, 30f)); 
        bNotify.setMargin(new Insets(10, 30, 5, 30));

        // Logout Button
        JButton bLogout = new JButton("Logout");
        bLogout.setBackground(Color.decode("#2f77b1"));
        bLogout.setForeground(Color.WHITE);
        bLogout.setFont(breatheFont.deriveFont(Font.PLAIN, 35f)); 
        bLogout.setMargin(new Insets(10, 30, 5, 30));
        
        p3.add(bLogout);
        p3.add(Box.createHorizontalStrut(20)); // Spacer
        p3.add(bNotify);
        
        f.add(p3, BorderLayout.SOUTH);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // --- ACTION LISTENERS ---
        bMySections.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening My Sections...");
                new MySectionsFrame(username, role, password, roll_no);
                f.dispose(); 
            }
        });
        
        bComputeGrade.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InstructorService service = new InstructorService();
                
                if (service.isSystemActive()) {
                    System.out.println("Opening Compute Final Grades...");
                    new ComputeFinalFrame(username, role, password, roll_no);
                    f.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot change, Maintenance mode is on.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        bClassStats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening Class Statistics...");
                new ClassStatisticsFrame(username, role, password, roll_no);
                f.dispose();
            }
        });

        bCatalog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening Course Catalog...");
                new catalog(username, role, password, roll_no);
                f.dispose();
            }
        });

        bNotify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new edu.univ.erp.ui.common.NotificationDialog(f, roll_no);
            }
        });

        bLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(f, "Logged out.");
                System.out.println("Logged out.");
                f.dispose();
            }
        });
    }
}