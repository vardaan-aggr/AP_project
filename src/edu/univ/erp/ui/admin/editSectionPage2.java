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


public class editSectionPage2 {

    public editSectionPage2(String roll_no,String arrgs[]) {
    // public editSectionPage2(String arrgs[]) {


        JFrame f = new JFrame("Edit Section Details");;
        f.setSize(800, 600); 
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));
        
        JLabel l0 = new JLabel("EDIT SECTION");
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
        t1.setText(arrgs[0]);
        t1.setEditable(false);
        f.add(t1);
        
        JLabel l2 = new JLabel("Section: ");
        l2.setBounds(145, 150, 100, 30);
        f.add(l2);
        JTextField t2 = new JTextField();
        t2.setBounds(275, 150, 350, 30);
        t2.setText(arrgs[1]);
        t2.setEditable(false);
        f.add(t2);

        JLabel l3 = new JLabel("Instructor's Roll no : ");
        l3.setBounds(145, 200, 120, 30);
        f.add(l3);
        JTextField t3 = new JTextField();
        t3.setBounds(275, 200, 350, 30);
        t3.setText(arrgs[2]);
        f.add(t3);

        JLabel l4 = new JLabel("Day & Time : ");
        l4.setBounds(145, 250, 100, 30);
        f.add(l4);
        JTextField t4 = new JTextField();
        t4.setBounds(275, 250, 350, 30);
        t4.setText(arrgs[3]);
        f.add(t4);
        
        JLabel l5 = new JLabel("Room No : ");
        l5.setBounds(145, 300, 100, 30);
        f.add(l5);
        JTextField t5 = new JTextField();
        t5.setBounds(275, 300, 350, 30);
        t5.setText(arrgs[4]);
        f.add(t5);
        
        JLabel l6 = new JLabel("Capacity : ");
        l6.setBounds(145, 350, 100, 30);
        f.add(l6);
        JTextField t6 = new JTextField();
        t6.setBounds(275, 350, 350, 30);
        t6.setText(arrgs[5]);
        f.add(t6);

        JLabel l7 = new JLabel("Semester : ");
        l7.setBounds(145, 400, 100, 30);
        f.add(l7);
        JTextField t7 = new JTextField();
        t7.setBounds(275, 400, 350, 30);
        t7.setText(arrgs[6]);
        f.add(t7);

        JLabel l8 = new JLabel("Year : ");
        l8.setBounds(145, 450, 100, 30);
        f.add(l8);
        JTextField t8 = new JTextField();
        t8.setBounds(275, 450, 350, 30);
        t8.setText(arrgs[7]);
        f.add(t8);

        JButton assignButton = new JButton("Save Changes");
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
                // Use an UPDATE statement
                try (PreparedStatement statement = connection.prepareStatement(
                        """
                        UPDATE sections 
                        SET roll_no = ?, day_time = ?, room = ?, capacity = ?, semester = ?, year = ?
                        WHERE course_code = ? AND section = ?;
                        """)) {
                    
                    // Set the new values
                    statement.setString(1, t3.getText()); // roll_no
                    statement.setString(2, t4.getText()); // day_time
                    statement.setString(3, t5.getText()); // room
                    statement.setString(4, t6.getText()); // capacity
                    statement.setString(5, t7.getText()); // semester
                    statement.setString(6, t8.getText()); // year

                    // Use the original course_code and section from the text fields for the WHERE clause
                    // Note: It's better to make t1 (course_code) and t2 (section) non-editable
                    // to prevent users from changing the primary key during an edit.
                    statement.setString(7, t1.getText()); // WHERE course_code
                    statement.setString(8, t2.getText()); // WHERE section

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated == 0) {
                        JOptionPane.showMessageDialog(null, "Error: Couldn't update section.", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("Error: Couldn't update section.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Section updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        System.out.println("Update Successful.");
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error updating: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
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

