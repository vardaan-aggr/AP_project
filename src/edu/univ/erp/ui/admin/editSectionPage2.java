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

import edu.univ.erp.service.AdminService;
import edu.univ.erp.util.BREATHEFONT;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class editSectionPage2 {
    public editSectionPage2(String roll_no,String arrgs[]) {

     Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen(); 

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame("Edit Section Details");
        // Increased height to fit 8 fields comfortably
        f.setSize(800, 600); 
        f.setLayout(new BorderLayout());

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setBackground(Color.decode("#051072")); 
        p1.setOpaque(true); 
        
        JLabel l0 = new JLabel("EDIT SECTION"); 
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
        // Tighter vertical insets (5px) to fit many fields
        gbc.insets = new Insets(5, 10, 5, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Course Code (Read Only)
        addLabel(p2, gFont, "Course Code:", 0, gbc);
        JTextField tCourseCode = addField(p2, gFont, 0, gbc);
        tCourseCode.setText(arrgs[0]);
        tCourseCode.setEditable(false);

        // Row 1: Section (Read Only)
        addLabel(p2, gFont, "Section:", 1, gbc);
        JTextField tSection = addField(p2, gFont, 1, gbc);
        tSection.setText(arrgs[1]);
        tSection.setEditable(false);

        // Row 2: Instructor Roll
        addLabel(p2, gFont, "Instructor Roll No:", 2, gbc);
        JTextField tInsRollNo = addField(p2, gFont, 2, gbc);
        tInsRollNo.setText(arrgs[2]);

        // Row 3: Day & Time
        addLabel(p2, gFont, "Day & Time:", 3, gbc);
        JTextField tDayTime = addField(p2, gFont, 3, gbc);
        tDayTime.setText(arrgs[3]);

        // Row 4: Room No
        addLabel(p2, gFont, "Room No:", 4, gbc);
        JTextField tRoom = addField(p2, gFont, 4, gbc);
        tRoom.setText(arrgs[4]);

        // Row 5: Capacity
        addLabel(p2, gFont, "Capacity:", 5, gbc);
        JTextField tCapacity = addField(p2, gFont, 5, gbc);
        tCapacity.setText(arrgs[5]);

        // Row 6: Semester
        addLabel(p2, gFont, "Semester:", 6, gbc);
        JTextField tSemester = addField(p2, gFont, 6, gbc);
        tSemester.setText(arrgs[6]);

        // Row 7: Year
        addLabel(p2, gFont, "Year:", 7, gbc);
        JTextField tYear = addField(p2, gFont, 7, gbc);
        tYear.setText(arrgs[7]);

        // --- BUTTONS ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton bAssign = new JButton("Save Changes"); 
        bAssign.setBackground(Color.decode("#2f77b1")); 
        bAssign.setForeground(Color.WHITE); 
        bAssign.setFont(breatheFont.deriveFont(Font.PLAIN, 30));
        bAssign.setMargin(new Insets(5, 20, 5, 20));
        buttonPanel.add(bAssign);
        
        JButton bBack = new JButton("Back"); 
        bBack.setBackground(Color.decode("#2f77b1")); 
        bBack.setForeground(Color.WHITE); 
        bBack.setFont(breatheFont.deriveFont(Font.PLAIN, 30));
        bBack.setMargin(new Insets(5, 20, 5, 20));
        buttonPanel.add(bBack);

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
        bAssign.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String iRoll = tInsRollNo.getText().trim();
                String iDay = tDayTime.getText().trim();
                String iRoom = tRoom.getText().trim();
                String iCap = tCapacity.getText().trim();
                String iSem = tSemester.getText().trim();
                String iYear = tYear.getText().trim();
                String iCode = tCourseCode.getText().trim();
                String iSec = tSection.getText().trim();

                AdminService service = new AdminService();
                String result = service.updateSection(iRoll, iDay, iRoom, iCap, iSem, iYear, iCode, iSec);

                if ("Success".equals(result)) {
                    JOptionPane.showMessageDialog(null, "Section updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new adminDashboard(roll_no);
                    f.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }   
        });

        bBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Going back to Admin Dashboard..");
                new adminDashboard(roll_no);
                f.dispose();
            }
        });        
    }

    private void addLabel(JPanel p, Font f, String text, int y, GridBagConstraints gbc) {
        JLabel l = new JLabel(text);
        l.setFont(f.deriveFont(Font.BOLD, 18));
        l.setForeground(Color.decode("#020A48"));
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        p.add(l, gbc);
    }

    private JTextField addField(JPanel p, Font f, int y, GridBagConstraints gbc) {
        JTextField t = new JTextField(20);
        t.setFont(f.deriveFont(Font.PLAIN, 18));
        gbc.gridx = 1; gbc.gridy = y; gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        p.add(t, gbc);
        return t;
    }
}
