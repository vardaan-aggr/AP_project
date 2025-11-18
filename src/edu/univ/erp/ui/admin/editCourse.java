package edu.univ.erp.ui.admin;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import edu.univ.erp.data.DatabaseConnector;
import edu.univ.erp.ui.admin.adminDashboard;
import edu.univ.erp.ui.admin.editSectionPage2;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;


public class editCourse {

    public editCourse(String roll_no) {
    // public static void main(String[] args) {
        JFrame f0 = new JFrame("EDIT COURSE");
        f0.setSize(800, 600); 
        f0.setLayout(null);
        f0.getContentPane().setBackground(Color.decode("#d8d0c1"));

        JLabel l0 = new JLabel("EDIT COURSE");
        l0.setBounds(0, 0, 800, 60); 
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#d8d0c4"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f0.add(l0);

        JLabel l1 = new JLabel("Course code: ");
        l1.setBounds(145, 200, 100, 30);
        f0.add(l1);
        JTextField t1 = new JTextField(50);
        t1.setBounds(275, 200, 350, 30);
        f0.add(t1);

        
        JLabel l2 = new JLabel("Section: ");
        l2.setBounds(145, 270, 100, 30);
        f0.add(l2);
        JTextField t2 = new JTextField();
        t2.setBounds(275, 270, 350, 30);
        f0.add(t2);

        JButton continueButton = new JButton("Continue");
        continueButton.setBackground(Color.decode("#2f77b1"));
        continueButton.setForeground(Color.WHITE);
        continueButton.setFont(new Font("Arial", Font.BOLD, 14));
        continueButton.setBounds(190, 350, 150, 50); 
        f0.add(continueButton);

        JButton backButton = new JButton("<- BACK");
        backButton.setBackground(Color.decode("#2f77b1"));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBounds(440, 350, 150, 50); 
        f0.add(backButton);

        f0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f0.setLocationRelativeTo(null);
        f0.setVisible(true);

        // --- Action Listeners ---
        continueButton.addActionListener(new ActionListener() {
            String[] arr = new String[4];
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DatabaseConnector.getErpConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("""
                        Select * FROM courses WHERE course_code = ? AND section = ?; 
                    """)) {
                    statement.setString(1, t1.getText()); 
                    statement.setString(2, t2.getText()); 

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                    
                        String course_code = resultSet.getString("course_code");
                        String title = resultSet.getString("title");
                        String section = resultSet.getString("section");
                        String credits = resultSet.getString("credits");

                        arr[0] = course_code;
                        arr[1] = title;   
                        arr[2] = section;
                        arr[3] = credits;

                        new editCoursePage2(roll_no,arr);
                        f0.dispose();
    
                    }else {
                        JOptionPane.showMessageDialog(null, "Section not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("Section not found.");
                    } 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error fetching section: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Going back to Admin Dashboard..");
                new adminDashboard(roll_no);
                f0.dispose();
            }
        });        

        

        
    }
        
}