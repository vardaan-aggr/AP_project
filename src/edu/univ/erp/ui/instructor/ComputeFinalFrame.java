package edu.univ.erp.ui.instructor;

import edu.univ.erp.data.ErpCommandRunner;  
 
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.univ.erp.util.BREATHEFONT;

public class ComputeFinalFrame {
    public ComputeFinalFrame(String username, String role, String password, String roll_no) {

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

        JTextField num = new JTextField(15);
        num.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        p2.add(num, gbc);

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

        JTextField quizMarks = new JTextField(15);
        quizMarks.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1;
        p2.add(quizMarks, gbc);
        
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

        JTextField midsemField = new JTextField(15);
        midsemField.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1;
        p2.add(midsemField, gbc);

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

        JTextField endsemField = new JTextField(15);
        endsemField.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 1;
        p2.add(endsemField, gbc);

        JLabel suffixEndsem = new JLabel("/ 10");
        suffixEndsem.setFont(labelFont);
        suffixEndsem.setForeground(labelColor);
        gbc.gridx = 2; gbc.gridy = 5; gbc.weightx = 0;
        p2.add(suffixEndsem, gbc);

        f.add(p2, BorderLayout.CENTER);

        // ---- BOTTOM ----
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        p3.setBackground(Color.decode("#dbd3c5"));
        
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.decode("#2f77b1"));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(breatheFont.deriveFont(Font.PLAIN, 35f));
        backButton.setMargin(new Insets(10, 30, 5, 30));
        p3.add(backButton);

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
                if (num.getText().trim().isEmpty() || code.getText().trim().isEmpty() || sectionField.getText().trim().isEmpty() || 
                    quizMarks.getText().trim().isEmpty() || midsemField.getText().trim().isEmpty() || endsemField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String userRollno_input = num.getText().trim();
                String courseCode_input = code.getText().trim();
                String section_input = sectionField.getText().trim();
                int rows = -1;
                try {
                    rows = ErpCommandRunner.instructorGradeComputeHelper(quizMarks.getText().trim(), midsemField.getText().trim(), endsemField.getText().trim(), userRollno_input, courseCode_input, section_input);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: ", "Error", JOptionPane.ERROR_MESSAGE);
                }
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Grade computed and updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new InstructorDashboard(username, role, password, roll_no);
                    f.dispose();
                }   
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new InstructorDashboard(username, role, password, roll_no);
                f.dispose();
            }
        });
    }

    
}