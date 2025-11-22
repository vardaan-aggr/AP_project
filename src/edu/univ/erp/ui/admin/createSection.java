package edu.univ.erp.ui.admin;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.data.ErpCommandRunner;
import edu.univ.erp.util.BREATHEFONT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;


public class createSection {

    public createSection(String roll_no) {

        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen(); 

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame("Create Section");
        f.setSize(800, 650); 
        f.setLayout(new BorderLayout());

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setBackground(Color.decode("#051072")); 
        p1.setOpaque(true); 
        
        JLabel l0 = new JLabel("CREATE SECTION"); 
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 60f)); 
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE ----
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        p2.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Course Code
        addLabel(p2, gFont, "Course Code:", 0, gbc);
        JTextField tCourseCode = addField(p2, gFont, 0, gbc);

        // Row 1: Section
        addLabel(p2, gFont, "Section:", 1, gbc);
        JTextField tSection = addField(p2, gFont, 1, gbc);

        // Row 2: Instructor Roll
        addLabel(p2, gFont, "Instructor Roll No:", 2, gbc);
        JTextField tInstructorRollNo = addField(p2, gFont, 2, gbc);

        // Row 3: Day & Time
        addLabel(p2, gFont, "Day & Time:", 3, gbc);
        JTextField tDayTime = addField(p2, gFont, 3, gbc);

        // Row 4: Room No
        addLabel(p2, gFont, "Room No:", 4, gbc);
        JTextField tRoomNo = addField(p2, gFont, 4, gbc);

        // Row 5: Capacity
        addLabel(p2, gFont, "Capacity:", 5, gbc);
        JTextField tCapacity = addField(p2, gFont, 5, gbc);

        // Row 6: Semester
        addLabel(p2, gFont, "Semester:", 6, gbc);
        JTextField tSemester = addField(p2, gFont, 6, gbc);

        // Row 7: Year
        addLabel(p2, gFont, "Year:", 7, gbc);
        JTextField tYear = addField(p2, gFont, 7, gbc);

        // --- BUTTONS ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton assignButton = new JButton("Create"); 
        assignButton.setBackground(Color.decode("#2f77b1")); 
        assignButton.setForeground(Color.WHITE); 
        assignButton.setFont(breatheFont.deriveFont(Font.PLAIN, 35)); 
        assignButton.setMargin(new Insets(5, 20, 5, 20));
        buttonPanel.add(assignButton);
        
        JButton backButton = new JButton("Back"); 
        backButton.setBackground(Color.decode("#2f77b1")); 
        backButton.setForeground(Color.WHITE); 
        backButton.setFont(breatheFont.deriveFont(Font.PLAIN, 35));
        backButton.setMargin(new Insets(5, 20, 5, 20));
        buttonPanel.add(backButton);

        // Place buttons
        gbc.gridx = 0; gbc.gridy = 8; 
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 10, 10, 10); 
        p2.add(buttonPanel, gbc);

        f.add(p2, BorderLayout.CENTER);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // --- Action Listeners ---
        assignButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tCourseCode.getText().trim().isEmpty() || tSection.getText().trim().isEmpty() || tInstructorRollNo.getText().trim().isEmpty() || tDayTime.getText().trim().isEmpty() || tRoomNo.getText().trim().isEmpty() || tCapacity.getText().trim().isEmpty() || tSemester.getText().trim().isEmpty() || tYear.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    // Validate Instructor Roll No 
                    try {
                        Integer.parseInt(tInstructorRollNo.getText().trim()); 
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Instructor Roll No must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // Validate Capacity 
                    try {
                        int cap = Integer.parseInt(tCapacity.getText().trim());
                        if (cap <= 0) {
                            JOptionPane.showMessageDialog(null, "Capacity must be a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Capacity must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // Validate Year 
                    try {
                        int yr = Integer.parseInt(tYear.getText().trim());
                        if (yr <= 0) {
                            JOptionPane.showMessageDialog(null, "Year must be a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Year must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input data.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    ErpCommandRunner.createSectionHelper(tCourseCode.getText().trim(), tSection.getText().trim(), tInstructorRollNo.getText().trim(), tDayTime.getText().trim(), tRoomNo.getText().trim(), tCapacity.getText().trim(), tSemester.getText().trim(), tYear.getText().trim());    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error while creating section: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                new adminDashboard(roll_no);
                f.dispose();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Going back to Admin Dashboard..");
                new adminDashboard(roll_no);
                f.dispose();
            }
        });
    }

    private static void addLabel(JPanel p, Font f, String text, int y, GridBagConstraints gbc) {
        JLabel l = new JLabel(text);
        l.setFont(f.deriveFont(Font.BOLD, 18)); 
        l.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p.add(l, gbc);
    }

    private static JTextField addField(JPanel p, Font f, int y, GridBagConstraints gbc) {
        JTextField t = new JTextField(20);
        t.setFont(f.deriveFont(Font.PLAIN, 18));
        gbc.gridx = 1; gbc.gridy = y; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        p.add(t, gbc);
        return t;
    }
}
