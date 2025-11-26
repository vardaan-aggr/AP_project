package edu.univ.erp.ui.instructor;

import edu.univ.erp.service.InstructorService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.univ.erp.util.BREATHEFONT;

public class ComputeFinalFrame {
    public ComputeFinalFrame(String username, String role, String password, String insRollno) {

        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame("Compute Final Grades");
        f.setSize(800, 600);
        f.setLayout(new BorderLayout());
        f.setLocationRelativeTo(null); 
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ---- TOP  ----
        JPanel p1 = new JPanel();
        p1.setOpaque(true); 
        p1.setBackground(Color.decode("#051072")); 
        
        JLabel l0 = new JLabel("COMPUTE GRADES");
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 80f));
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- CENTER ----
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setBackground(Color.decode("#dbd3c5"));
        p2.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = gFont.deriveFont(Font.PLAIN, 22f);
        Font fieldFont = gFont.deriveFont(Font.PLAIN, 20f);
        Color labelColor = Color.decode("#020A48");

        JLabel student_no = new JLabel("Student Roll No:");
        student_no.setFont(labelFont);
        student_no.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        p2.add(student_no, gbc);

        JTextField studentRollNo_in = new JTextField(15);
        studentRollNo_in.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        p2.add(studentRollNo_in, gbc);

        JLabel course_code = new JLabel("Course Code:");
        course_code.setFont(labelFont);
        course_code.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        p2.add(course_code, gbc);

        JTextField code = new JTextField(15);
        code.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        p2.add(code, gbc);

        JLabel sectionLabel = new JLabel("Section:");
        sectionLabel.setFont(labelFont);
        sectionLabel.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        p2.add(sectionLabel, gbc);

        JTextField sectionField = new JTextField(15);
        sectionField.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1;
        p2.add(sectionField, gbc);

        JLabel quizLabel = new JLabel("Quiz Total:");
        quizLabel.setFont(labelFont);
        quizLabel.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        p2.add(quizLabel, gbc);

        JTextField tquizMarks = new JTextField(15);
        tquizMarks.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1;
        p2.add(tquizMarks, gbc);
        
        JLabel suffixQuiz = new JLabel("/ 10");
        suffixQuiz.setFont(labelFont);
        suffixQuiz.setForeground(labelColor);
        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0;
        p2.add(suffixQuiz, gbc);

        JLabel midsemLabel = new JLabel("Midsem Marks:");
        midsemLabel.setFont(labelFont);
        midsemLabel.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        p2.add(midsemLabel, gbc);

        JTextField tmidsem = new JTextField(15);
        tmidsem.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1;
        p2.add(tmidsem, gbc);

        JLabel suffixMidsem = new JLabel("/ 10");
        suffixMidsem.setFont(labelFont);
        suffixMidsem.setForeground(labelColor);
        gbc.gridx = 2; gbc.gridy = 4; gbc.weightx = 0;
        p2.add(suffixMidsem, gbc);
        
        JLabel endsemLabel = new JLabel("Endsem Marks:");
        endsemLabel.setFont(labelFont);
        endsemLabel.setForeground(labelColor);
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        p2.add(endsemLabel, gbc);

        JTextField tendsem = new JTextField(15);
        tendsem.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 1;
        p2.add(tendsem, gbc);

        JLabel suffixEndsem = new JLabel("/ 10");
        suffixEndsem.setFont(labelFont);
        suffixEndsem.setForeground(labelColor);
        gbc.gridx = 2; gbc.gridy = 5; gbc.weightx = 0;
        p2.add(suffixEndsem, gbc);

        f.add(p2, BorderLayout.CENTER);

        // ---- BOTTOM ----
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        p3.setBackground(Color.decode("#dbd3c5"));
        
        JButton bBack = new JButton("Back");
        bBack.setBackground(Color.decode("#2f77b1"));
        bBack.setForeground(Color.WHITE);
        bBack.setFont(breatheFont.deriveFont(Font.PLAIN, 35f));
        bBack.setMargin(new Insets(10, 30, 5, 30));
        p3.add(bBack);

        JButton add = new JButton("Add Grade");
        add.setBackground(Color.decode("#2f77b1"));
        add.setForeground(Color.WHITE);
        add.setFont(breatheFont.deriveFont(Font.PLAIN, 35f));
        add.setMargin(new Insets(10, 30, 5, 30));
        p3.add(add);

        f.add(p3, BorderLayout.SOUTH);
        f.setVisible(true);
        
        // ---- Action Listeners ----
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (studentRollNo_in.getText().trim().isEmpty() || code.getText().trim().isEmpty() || sectionField.getText().trim().isEmpty() || tquizMarks.getText().trim().isEmpty() || tmidsem.getText().trim().isEmpty() || tendsem.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String stdRollNo_in = studentRollNo_in.getText().trim();
                String courseCode_in = code.getText().trim();
                String section_in = sectionField.getText().trim();
                String quizMarks = tquizMarks.getText().trim();
                String midsemMarks = tmidsem.getText().trim();
                String endsemMarks = tendsem.getText().trim();

                InstructorService service = new InstructorService();
                String result = null;
                try {
                    result = service.computeAndAssignGrade(insRollno, stdRollNo_in, courseCode_in, section_in, quizMarks, midsemMarks, endsemMarks);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database error." + ex, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (result.equals("-3")) {
                    System.out.println("Registration Failed: Maintenance Mode is ON.");
                    JOptionPane.showMessageDialog(null, "Registration Failed: Maintenance Mode is ON.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
                else if ("Success".equals(result)) {
                    JOptionPane.showMessageDialog(null, "Graded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new InstructorDashboard(username, role, password, insRollno);
                    f.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        bBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new InstructorDashboard(username, role, password, insRollno);
                f.dispose();
            }
        });
    }
}
