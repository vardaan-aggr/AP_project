package edu.univ.erp.ui.admin;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import edu.univ.erp.ui.admin.manageCourseSection.createCourse;
import edu.univ.erp.ui.admin.manageCourseSection.createSection;
import edu.univ.erp.ui.admin.manageCourseSection.deleteSectionACourse;
import edu.univ.erp.ui.admin.manageCourseSection.editCourse;
import edu.univ.erp.ui.admin.manageCourseSection.editSection;
import edu.univ.erp.util.BREATHEFONT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class CreateEdit {

    public CreateEdit(String roll_no) {

        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen();
        
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame("Manage Courses");
        f.setSize(800, 600);
        f.setLayout(new BorderLayout());

        // ---- TOP ----
        JPanel p1 = new JPanel();
        p1.setBackground(Color.decode("#051072")); 
        
        JLabel l0 = new JLabel("MANAGE COURSES"); 
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80f)); 
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- MIDDLE ----
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); 
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1; 
        gbc.weighty = 1;

        Font btnFont = gFont.deriveFont(Font.PLAIN, 21f);
        Color btnColor = Color.decode("#2f77b1");
        Color txtColor = Color.WHITE;

        // --- ROW 1 ---
        JButton bCreateSection = new JButton("Create Section");
        bCreateSection.setBackground(btnColor); bCreateSection.setForeground(txtColor);
        bCreateSection.setFont(btnFont);
        
        gbc.gridx = 0; gbc.gridy = 0;
        p2.add(bCreateSection, gbc);

        JButton bCreateCourse = new JButton("Create Course");
        bCreateCourse.setBackground(btnColor); bCreateCourse.setForeground(txtColor);
        bCreateCourse.setFont(btnFont);
        
        gbc.gridx = 1; gbc.gridy = 0;
        p2.add(bCreateCourse, gbc);

        // --- ROW 2 ---
        JButton bEditSection = new JButton("Edit Section");
        bEditSection.setBackground(btnColor); bEditSection.setForeground(txtColor);
        bEditSection.setFont(btnFont);
        
        gbc.gridx = 0; gbc.gridy = 1;
        p2.add(bEditSection, gbc);

        JButton bEditCourse = new JButton("Edit Course");
        bEditCourse.setBackground(btnColor); bEditCourse.setForeground(txtColor);
        bEditCourse.setFont(btnFont);
        
        gbc.gridx = 1; gbc.gridy = 1;
        p2.add(bEditCourse, gbc);

        // --- ROW 3 ---
        JButton bDelete = new JButton("Delete Section / Course");
        bDelete.setBackground(Color.decode("#2f77b1")); 
        bDelete.setForeground(txtColor);
        bDelete.setFont(btnFont);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2; 
        p2.add(bDelete, gbc);

        // --- ROW 4 (Back) ---
        JButton bBack = new JButton("Back");
        bBack.setBackground(btnColor); bBack.setForeground(txtColor);
        bBack.setFont(btnFont);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        p2.add(bBack, gbc);

        JPanel container = new JPanel(new BorderLayout());
        container.add(p2, BorderLayout.CENTER);
        p2.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100)); 

        f.add(container, BorderLayout.CENTER);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        bCreateSection.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new createSection(roll_no);
                f.dispose();
            }
        }); 

        bCreateCourse.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                new createCourse(roll_no);
                f.dispose();;
            }
        });

        bEditSection.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new editSection(roll_no);
                f.dispose();
            }
        });

        bEditCourse.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new editCourse(roll_no);
                f.dispose();
            }
        });

        bDelete.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new deleteSectionACourse(roll_no);
                f.dispose();
            }
        });

        bBack.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new adminDashboard(roll_no);
                f.dispose();
            }
        });
        
        
    }
}
