package edu.univ.erp.ui.instructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import edu.univ.erp.data.DatabaseConnector;

public class ComputeFinalFrame {
    public ComputeFinalFrame(String roll_no) {
        JFrame f ;
        f = new JFrame("Compute Final Grades");
        f.setSize(600, 600);
        f.setLayout(null);
        f.setLocationRelativeTo(null); 
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        JLabel student_no = new JLabel("Roll number : ");
        student_no.setBounds(50, 50, 100, 30);
        f.add(student_no);
        JTextField num = new JTextField(50);
        num.setBounds(150, 50, 250, 30);
        f.add(num);
        
        JLabel course_code = new JLabel("course code : ");
        course_code.setBounds(50, 100, 100, 30);
        f.add(course_code);
        JTextField code = new JTextField();
        code.setBounds(150, 100, 250, 30);
        f.add(code);
        
        JLabel sectionLabel = new JLabel( " Section  : ");
        sectionLabel.setBounds(50, 150, 100, 30);
        f.add(sectionLabel);
        JTextField sectionField = new JTextField();
        sectionField.setBounds(150, 150, 250, 30);
        f.add(sectionField);
        
        JLabel quizLabel = new JLabel("quiz total ");
        quizLabel.setBounds(50, 200, 100, 30);
        f.add(quizLabel);
        JTextField quizMarks = new JTextField();
        quizMarks.setBounds(150, 200, 250, 30);
        f.add(quizMarks);
        JLabel suffixQuiz = new JLabel("/ 10 ");
        suffixQuiz.setBounds(400, 200, 50, 30);
        f.add(suffixQuiz);
        
        JLabel midsemLabel = new JLabel(" Midsem Marks : ");
        midsemLabel.setBounds(50, 250, 100, 30);
        f.add(midsemLabel);
        JTextField midsemField = new JTextField();
        midsemField.setBounds(150, 250, 250, 30);
        f.add(midsemField);
        JLabel suffixMidsem = new JLabel("/ 10 ");
        suffixMidsem.setBounds(400, 250, 50, 30);
        f.add(suffixMidsem);
        
        JLabel endsemLabel = new JLabel("Endsem Marks: ");
        endsemLabel.setBounds(50, 300, 100, 30);
        f.add(endsemLabel);
        JTextField endsemField = new JTextField();
        endsemField.setBounds(150, 300, 250, 30);
        f.add(endsemField);
        JLabel suffixEndsem = new JLabel("/ 10 ");
        suffixEndsem.setBounds(400, 300, 50, 30);
        f.add(suffixEndsem);


        JButton backButton = new JButton("<-- Back to Dashboard");
        backButton.setBounds(210, 400, 200, 30);
        f.add(backButton);

        JButton add = new JButton("Add +");
        add.setBounds(100, 400, 100, 30);
        f.add(add);
        
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 1. Get text from the fields
                String userRollno_input = num.getText();
                String courseCode_input = code.getText();
                String section_input = sectionField.getText();
                String quizMarks_input = quizMarks.getText();
                String midsemMarks_input = midsemField.getText();
                String endsemMarks_input = endsemField.getText();
                char finalGrade=computeGrade( quizMarks_input, midsemMarks_input, endsemMarks_input);
                try (Connection connection = DatabaseConnector.getErpConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO grades (roll_no, course_code, section, grade) VALUES
                        (?,?,?,?);     """)) {
                        statement.setString(1, userRollno_input);
                        statement.setString(2, courseCode_input);
                        statement.setString(3,section_input );
                        statement.setString(4, finalGrade+"");
                        statement.executeQuery();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
                JOptionPane.showMessageDialog(null, " grades updated successfully.");
                new InstructorDashboard(roll_no);
                f.dispose();
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new InstructorDashboard(roll_no);
                f.dispose();
            }
        });
    }
    

    

    private char computeGrade(String quizMarks, String midsemMarks, String endsemMarks) {
    

        int quiz = Integer.parseInt(quizMarks);
        int midsem = Integer.parseInt(midsemMarks); 
        int endsem = Integer.parseInt(endsemMarks);

        int finalGrade = (int)(0.2 * quiz + 0.3 * midsem + 0.5 * endsem); 
        // all mid sem ,end sem, quiz are of 10 marks each
        if (finalGrade <= 10 && finalGrade >= 8) {
            return 'A';
        } else if (finalGrade < 8 && finalGrade >= 6) {
            return 'B';
        } else if (finalGrade < 6 && finalGrade >= 4) {
            return 'C';
        } else {
            return 'F';
        }

        

    }

}