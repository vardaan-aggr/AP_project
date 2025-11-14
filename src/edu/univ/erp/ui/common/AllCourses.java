package edu.univ.erp.ui.common;

import javax.swing.*;

import edu.univ.erp.ui.instructor.InstructorDashboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.univ.erp.data.DatabaseConnector;

import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;

public class AllCourses {

    public AllCourses(String roll_no, String role) {

        JFrame f = new JFrame("All Courses");
        f.setSize(600,600);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
        
        String data[][] = dataPull();
        String columName[] = {" course Code ", " Title ", " section ", " credits "};
        JTable t = new JTable(data, columName);
        JScrollPane sp = new JScrollPane(t);
        
        f.add(sp);
        sp.setVisible(true);
        sp.setBounds(30, 70, 520, 400);

        JButton serchButton = new JButton(" 🔍 Search ");
        serchButton.setBounds(200, 20, 100, 30);
        f.add(serchButton);
        JTextField t1 = new JTextField(50);
        t1.setBounds(310, 20, 200, 30);
        f.add(t1);
        
        JButton backButton = new JButton("<- Back");
        backButton.setBounds(450, 500, 100, 30);
        f.add(backButton);
       


        // action listener
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (role.equals("student")) {
                    f.dispose();
                    // new studentDashboard(Username);
                }
                else if (role.equals("instructor")) {
                    f.dispose();
                    new InstructorDashboard(roll_no);
                }
                else if (role.equals("admin")) {
                    f.dispose();
                    // new AdminDashboard(String username);
                }
            }
        });

        serchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String course_code_input = t1.getText();
                int rowNumber = courseFinder(course_code_input);
                if (rowNumber != -1) {
                    t.setRowSelectionInterval(rowNumber - 1, rowNumber-1 );
                    t.scrollRectToVisible(t.getCellRect(rowNumber - 1, 0, true));
                } else {
                    JOptionPane.showMessageDialog(f, "Course not found: " + course_code_input);
                }
            }
        });
    }

    private String[][] dataPull() {
        ArrayList<String[]> arrList = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select course_code, title, section,credits FROM courses; 
                    """)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        String course_code = resultSet.getString("course_code");
                        String title = resultSet.getString("title");
                        String section = resultSet.getString("section");
                        String credits = resultSet.getString("credits");
             
                        arrList.add(new String[]{course_code , title ,  section , credits});
                    } 
                    if (empty) {
                        System.out.println("\t (no data)");
                    }
                }
            } 
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        String[][] strArr = new String[arrList.size()][4];
        arrList.toArray(strArr);
        return strArr;
    }    
    
    private int courseFinder(String course_code) {
        int rowNumber = -1;
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            String sql = """
                SELECT row_num 
                FROM (
                    SELECT course_code, ROW_NUMBER() OVER (ORDER BY course_code) AS row_num
                    FROM courses
                ) AS numbered
                WHERE course_code = ?;
            """;
            
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, course_code);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        rowNumber = resultSet.getInt("row_num");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rowNumber;
    }

}
