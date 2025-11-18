package edu.univ.erp.ui.admin;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class CreateEdit {

    // public CreateEdit(String roll_no) {
    public static void main(String[] args) {

        JFrame f = new JFrame("Create Edit courses");;
        f.setSize(800, 600); 
        f.setLayout(null);
        f.getContentPane().setBackground(Color.decode("#d8d0c1"));
        
        JLabel l0 = new JLabel("CREATE/EDIT/DELETE SECTION");
        l0.setBounds(0, 0, 800, 60); 
        l0.setBackground(Color.decode("#051072"));
        l0.setForeground(Color.decode("#d8d0c4"));
        l0.setFont(new Font("Arial", Font.BOLD, 28));
        l0.setOpaque(true);
        l0.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(l0);

        JButton createNewButton = new JButton("Create New Section");
        createNewButton.setBackground(Color.decode("#2f77b1"));
        createNewButton.setForeground(Color.WHITE);
        createNewButton.setFont(new Font("Arial", Font.BOLD, 14));
        createNewButton.setBounds(200, 100, 200, 50); 
        f.add(createNewButton);

        JButton createNewCourButton = new JButton("Create New Course ");
        createNewCourButton.setBackground(Color.decode("#2f77b1"));
        createNewCourButton.setForeground(Color.WHITE);
        createNewCourButton.setFont(new Font("Arial", Font.BOLD, 14));
        createNewCourButton.setBounds(450, 100, 200, 50); 
        f.add(createNewCourButton);

        JButton editButton = new JButton("Edit Section");
        editButton.setBackground(Color.decode("#2f77b1"));
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.setBounds(200, 200, 200, 50); 
        f.add(editButton);

        JButton editCourButton = new JButton("Edit Course ");
        editCourButton.setBackground(Color.decode("#2f77b1"));
        editCourButton.setForeground(Color.WHITE);
        editCourButton.setFont(new Font("Arial", Font.BOLD, 14));
        editCourButton.setBounds(450, 200, 200, 50); 
        f.add(editCourButton);

        JButton deleteButton = new JButton("Delete Section and Course");
        deleteButton.setBackground(Color.decode("#2f77b1"));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBounds(250, 300, 350, 50); 
        f.add(deleteButton);

        JButton backButton = new JButton("<- BACK");
        backButton.setBackground(Color.decode("#2f77b1"));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBounds(320, 400, 200, 50); 
        f.add(backButton);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        createNewButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // new createSection(roll_no);
                f.dispose();
            }
        }); 

        createNewCourButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                new createCourse();
                f.dispose();;
            }
            
        });

        editButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                new editSection();
                f.dispose();
            }
        });

        editCourButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                new editCourse();
                f.dispose();
            }
        });
        deleteButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                new deleteSectionACourse();
                f.dispose();
            }
        });


        backButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // new adminDashboard(roll_no);
                f.dispose();
            }
        });
        
        
    }
}
