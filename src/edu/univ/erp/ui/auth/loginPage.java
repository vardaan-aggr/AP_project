import javax.swing.*;

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
    }
}