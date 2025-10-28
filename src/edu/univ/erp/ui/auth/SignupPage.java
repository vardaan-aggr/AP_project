package edu.univ.erp.ui.auth;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JOptionPane;  // <-- IMPORT ADDED
import javax.swing.SwingUtilities; // <-- IMPORT ADDED
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import edu.univ.erp.auth.AuthService; // (Make sure this class has a public boolean register(String user, String pass, String role) method)

// --- Renamed class to follow Java conventions ---
public class SignupPage { 

    public static void main(String[] args) {
        // --- Run all Swing code on the Event Dispatch Thread (EDT) ---
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        // --- Renamed variables to "SignUp" ---
        JFrame signupFrame = new JFrame("University ERP SignUp");

        signupFrame.setSize(450, 300);
        signupFrame.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 60, 100, 30);
        signupFrame.add(userLabel);

        JTextField userText = new JTextField();
        userText.setBounds(150, 60, 250, 30);
        signupFrame.add(userText);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(50, 110, 100, 30);
        signupFrame.add(roleLabel);

        JTextField roleText = new JTextField();
        roleText.setBounds(150, 110, 250, 30);
        signupFrame.add(roleText);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 160, 100, 30);
        signupFrame.add(passLabel);

        JPasswordField passText = new JPasswordField();
        passText.setBounds(150, 160, 250, 30);
        signupFrame.add(passText);

        JButton signupBtn = new JButton("SignUp"); // <-- Renamed
        signupBtn.setBounds(150, 220, 100, 30);
        signupFrame.add(signupBtn);

        signupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signupFrame.setLocationRelativeTo(null);
        signupFrame.setVisible(true);

        // --- UPDATED ACTION LISTENER ---
        signupBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 1. Get text from all fields
                String username = userText.getText();
                String role = roleText.getText();
                String password = new String(passText.getPassword());

                // 2. Simple validation
                if (username.isEmpty() || role.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(signupFrame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Stop
                }

                // 3. Create the auth service
                AuthService authService = new AuthService();

                // 4. Attempt to REGISTER the new user
                try {
                    // Assumes a method: public boolean register(String username, String password, String role)
                    if (authService.register(username, password, role)) {
                        // Success!
                        JOptionPane.showMessageDialog(signupFrame, "Registration Successful!");
                        signupFrame.dispose(); // Close this window

                        // Optionally, open the login window here
                        // new LoginPage()...

                    } else {
                        // Failure (e.g., username already exists)
                        JOptionPane.showMessageDialog(signupFrame, "Registration Failed. Username might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    // Handle other errors (like database failure)
                     JOptionPane.showMessageDialog(signupFrame, "An unexpected error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                     ex.printStackTrace(); // Good for debugging
                }
            }
        });
    }
}