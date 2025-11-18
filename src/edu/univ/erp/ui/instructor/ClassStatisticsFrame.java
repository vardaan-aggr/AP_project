package edu.univ.erp.ui.instructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;
import java.util.ArrayList;
import edu.univ.erp.data.DatabaseConnector;

public class ClassStatisticsFrame {
    public ClassStatisticsFrame(String roll_no) {

        JFrame f = new JFrame("Class Statistics");
        f.setSize(800, 600);
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        JLabel l0 = new JLabel("Class Statistics");
        l0.setBounds(0, 0, 800, 60);
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);

        JLabel course_code = new JLabel("Course code : ");
        course_code.setBounds(150, 120, 150, 35);
        f.add(course_code);
        JTextField code = new JTextField();
        code.setBounds(150, 120, 150, 35);
        f.add(code);

        JLabel sectionLabel = new JLabel( "Section : ");
        sectionLabel.setBounds(150, 180, 150, 35);
        f.add(sectionLabel);
        JTextField sectionField = new JTextField();
        sectionField.setBounds(300, 180, 300, 35);
        f.add(sectionField);


        JButton seeStats = new JButton("See Statistics");
        seeStats.setBounds(250, 280, 160, 40);
        seeStats.setBackground(Color.decode("#2f77b1"));
        seeStats.setForeground(Color.WHITE);
        seeStats.setFont(new Font("Arial", Font.BOLD, 14));
        f.add(seeStats);

        
        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBackground(Color.decode("#2f77b1"));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBounds(430, 280, 180, 40);
        f.add(backButton);

        seeStats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String courseCode_input = code.getText();
                String section_input = sectionField.getText();
                // Logic to compute statistics can be added here
                double stats =computeStats(courseCode_input, section_input);                
                JOptionPane.showMessageDialog(null, " Average CGPA  = " + stats) ;
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

    private String[] gradesArr(String courseCode, String section) {
        ArrayList<String> arrList = new ArrayList<>();
                    try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select grade FROM grades WHERE  course_code = ? AND section = ?; 
                    """)) {
                statement.setString(1, String.valueOf(courseCode));
                statement.setString(2, String.valueOf(section));
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String grade = resultSet.getString("grade");
                        arrList.add(grade);
                    } 
                    if (empty) {
                        System.out.println("\t (no data)");
                    }
                }
            } 
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        String[] strArr = new String[arrList.size()];
        arrList.toArray(strArr);
        return strArr;
    }
    
    private double computeStats(String courseCode, String section) {
        String[] grades = gradesArr(courseCode, section);
        int totalPoints = 0;
        for (String grade : grades) {
            switch (grade) {
                case "A":
                    totalPoints += 3;
                    break;
                case "B":
                    totalPoints += 2;
                    break;
                case "C":
                    totalPoints += 1;
                    break;
                case "F":
                    totalPoints += 0;
                    break;
            }
        }
        return grades.length == 0 ? 0.0 : (double) totalPoints / grades.length;
    }

}

