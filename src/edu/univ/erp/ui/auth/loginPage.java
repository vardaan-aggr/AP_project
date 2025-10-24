package edu.univ.erp.ui.auth;

import javax.swing.*;

import edu.univ.erp.auth.AuthService;

import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;  

class loginPage {
    public static void main(String[] args) {
        JFrame loginFrame = new JFrame("University ERP Login"); 

        loginFrame.setSize(450, 300); 
        loginFrame.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 60, 100, 30);
        loginFrame.add(userLabel);
        
        JTextField userText = new JTextField();
        userText.setBounds(150, 60, 250, 30);
        loginFrame.add(userText);
        
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 110, 100, 30);
        loginFrame.add(passLabel);
        
        JPasswordField passText = new JPasswordField();
        passText.setBounds(150, 110, 250, 30);
        loginFrame.add(passText);
        
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 170, 100, 30);
        loginFrame.add(loginBtn);

        // Final configuration
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null); 
        loginFrame.setVisible(true);

        // action listener
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 1. Get text from the fields
                String username = userText.getText();
                String password = new String(passText.getPassword());

                // 2. Create the login "brain"
                AuthService authService = new AuthService();

                // 3. Check the password by calling the login() method
                if (authService.login(username, password)) {
                    // Success!
                    System.out.println("Login Successful!");
                    // Later, you will close this window and open a dashboard
                } else {
                    // Failure
                    System.out.println("Login Failed: Incorrect username or password.");
                    // Later, you will show a pop-up error message
                }
            }
        });
    }
}