import javax.swing.*;

class loginPage {
    public static void main(String[] args) {
        // Create labels, text fields, button, and frame
        JLabel userLabel, passLabel;
        JTextField userText;
        JPasswordField passText;
        JButton loginBtn;
        JFrame loginFrame = new JFrame();
        
        // Set frame size and layout
        loginFrame.setSize(500, 600);
        loginFrame.setLayout(null); // Using null layout
        
        // Username label
        userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 60, 100, 30);
        loginFrame.add(userLabel);
        
        // Username text field
        userText = new JTextField();
        userText.setBounds(150, 60, 250, 30);
        loginFrame.add(userText);
        
        // Password label
        passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 110, 100, 30);
        loginFrame.add(passLabel);
        
        // Password text field
        passText = new JPasswordField();
        passText.setBounds(150, 110, 250, 30);
        loginFrame.add(passText);
        
        // Login button
        loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 170, 100, 30);
        loginFrame.add(loginBtn);
        
        // Configure the frame
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);
    }
}
