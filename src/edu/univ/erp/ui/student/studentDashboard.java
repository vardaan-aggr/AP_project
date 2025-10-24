package edu.univ.erp.ui.student;

import javax.swing.*;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;  

class studentDashboard {
    public static void main(String[] args) {
        JFrame studentDashboard = new JFrame("Dashboard"); 


        studentDashboard.setSize(450, 300); 
        studentDashboard.setLayout(null);
        

        JButton courseBtn = new JButton("Course Catalog");
        courseBtn.setBounds(900, 200, 200, 100);
        studentDashboard.add(courseBtn);

        JButton timetableBtn = new JButton("My Time Table");
        timetableBtn.setBounds(650, 200, 200, 100);
        studentDashboard.add(timetableBtn);

        JButton registrationBtn = new JButton("My Time Table");
        registrationBtn.setBounds(400, 200, 200, 100);
        studentDashboard.add(registrationBtn);

        JButton transcriptBtn = new JButton("Transcript");
        transcriptBtn.setBounds(250, 200, 100, 100);
        studentDashboard.add(transcriptBtn);

        JButton gradesBtn = new JButton("Grades");
        gradesBtn.setBounds(100, 200, 100, 100);
        studentDashboard.add(gradesBtn);

        studentDashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        studentDashboard.setLocationRelativeTo(null); 
        studentDashboard.setVisible(true);


                // actionlisteners
        courseBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        timetableBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        registrationBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        transcriptBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        gradesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

    }
}