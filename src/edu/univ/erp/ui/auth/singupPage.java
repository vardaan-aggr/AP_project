package edu.univ.erp.ui.auth;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;  
import edu.univ.erp.auth.AuthService;

public class singupPage {
    public static void main(String[] args) {
        JFrame singupFrame = new JFrame("University ERP Singup"); 

        singupFrame.setSize(450, 300); 
        singupFrame.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 60, 100, 30);
        singupFrame.add(userLabel);
        
        JTextField userText = new JTextField();
        userText.setBounds(150, 60, 250, 30);
        singupFrame.add(userText);


        
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(50, 110, 100, 30);
        singupFrame.add(roleLabel);
        
        JTextField roleText = new JTextField();
        roleText.setBounds(150, 110, 250, 30);
        singupFrame.add(roleText);


        
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 160, 100, 30);
        singupFrame.add(passLabel);
        
        JPasswordField passText = new JPasswordField();
        passText.setBounds(150, 160, 250, 30);
        singupFrame.add(passText);
        


        JButton singupBtn = new JButton("SingUp");
        singupBtn.setBounds(150, 220, 100, 30);
        singupFrame.add(singupBtn);

        singupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        singupFrame.setLocationRelativeTo(null); 
        singupFrame.setVisible(true);


        singupBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 1. Get text from the fields
                String username = userText.getText();
                String role = roleText.getText();
                String password = new String(passText.getPassword());

                // 2. Create the login "brain"
                AuthService AuthService = new AuthService();

                // 3. Check the password by calling the login() method
                if (AuthService.login(username, password)) {
                    // Success!
                    System.out.println("Login Successful!");
                    // Later, you will close this window and open a dashboard
                } else {
                    // Failure
                    System.out.println("Login Failed: Incorrect username or password.");
                    // Later, you will show a pop-up error message
                }
            }


    }

}
