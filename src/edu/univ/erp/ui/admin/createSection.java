package edu.univ.erp.ui.admin;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import edu.univ.erp.data.DatabaseConnector;
import edu.univ.erp.ui.admin.adminDashboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;


public class createSection {

    public createSection(String roll_no) {
    // public static void main(String[] args) {


        JFrame f = new JFrame("Create Course");;
        f.setSize(800, 600); 
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));
        
        JLabel l0 = new JLabel("CREATE SECTION");
        l0.setBounds(0, 0, 800, 60); 
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#d8d0c4"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);
        // course_code ,section ,roll_no ,day_time ,room ,capacity,semester,year 
        JLabel l1 = new JLabel("Course code: ");
        l1.setBounds(145, 100, 100, 30);
        f.add(l1);
        JTextField t1 = new JTextField(50);
        t1.setBounds(275, 100, 350, 30);
        f.add(t1);
        
        JLabel l2 = new JLabel("Section: ");
        l2.setBounds(145, 150, 100, 30);
        f.add(l2);
        JTextField t2 = new JTextField();
        t2.setBounds(275, 150, 350, 30);
        f.add(t2);

        JLabel l3 = new JLabel("Instructor's Roll no : ");
        l3.setBounds(145, 200, 120, 30);
        f.add(l3);

        JTextField t3 = new JTextField();
        t3.setBounds(275, 200, 350, 30);
        f.add(t3);

        JLabel l4 = new JLabel("Day & Time : ");
        l4.setBounds(145, 250, 100, 30);
        f.add(l4);
        JTextField t4 = new JTextField();
        t4.setBounds(275, 250, 350, 30);
        f.add(t4);
        
        JLabel l5 = new JLabel("Room No : ");
        l5.setBounds(145, 300, 100, 30);
        f.add(l5);
        JTextField t5 = new JTextField();
        t5.setBounds(275, 300, 350, 30);
        f.add(t5);
        
        JLabel l6 = new JLabel("Capacity : ");
        l6.setBounds(145, 350, 100, 30);
        f.add(l6);
        JTextField t6 = new JTextField();
        t6.setBounds(275, 350, 350, 30);
        f.add(t6);

        JLabel l7 = new JLabel("Semester : ");
        l7.setBounds(145, 400, 100, 30);
        f.add(l7);
        JTextField t7 = new JTextField();
        t7.setBounds(275, 400, 350, 30);
        f.add(t7);

        JLabel l8 = new JLabel("Year : ");
        l8.setBounds(145, 450, 100, 30);
        f.add(l8);
        JTextField t8 = new JTextField();
        t8.setBounds(275, 450, 350, 30);
        f.add(t8);

        JButton assignButton = new JButton("Create Section");
        assignButton.setBackground(Color.decode("#2f77b1"));
        assignButton.setForeground(Color.WHITE);
        assignButton.setFont(new Font("Arial", Font.BOLD, 14));
        assignButton.setBounds(190, 500, 150, 50); 
        f.add(assignButton);

        JButton backButton = new JButton("<- BACK");
        backButton.setBackground(Color.decode("#2f77b1"));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBounds(440, 500, 150, 50); 
        f.add(backButton);

        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // --- Action Listeners ---
        assignButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DatabaseConnector.getErpConnection()) {
                    try (PreparedStatement statement = connection.prepareStatement("""
                                INSERT INTO sections(course_code ,section ,roll_no ,day_time ,room ,capacity,semester,year ) VALUES (?,?, ?,?,?, ?, ?, ?);
                            """)) {
                        statement.setString(1, t1.getText());
                        statement.setString(2, t2.getText());
                        statement.setString(3, t3.getText());
                        statement.setString(4, t4.getText());
                        statement.setString(5, t5.getText());
                        statement.setString(6, t6.getText());
                        statement.setString(7, t7.getText());
                        statement.setString(8, t8.getText());

                        int rowsInsreted = statement.executeUpdate();
                        if (rowsInsreted == 0) {
                            JOptionPane.showMessageDialog(null, "Error: Couldn't register in database.", "Error", JOptionPane.ERROR_MESSAGE);
                            System.out.println("Error: Couldn't register in database.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Registered successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                            System.out.println("Registered Successfully.");
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error registering: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
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
        
    
}

