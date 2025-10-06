import javax.swing.*;
import java.awt.Color; 

class loginError {
    loginError() {
        JFrame f = new JFrame("Password error");

        JLabel l1 = new JLabel(
            "<html><center>Check the account information you entered<br>and try again.</center></html>"
        );


        f.getContentPane().setBackground(new Color(237, 217, 132));

        l1.setBounds(80, 30, 250, 50);
        f.add(l1);

        f.setSize(400, 120);
        f.setLayout(null);
        f.setLocationRelativeTo(null); 
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Good practice
    }

    public static void main(String[] args) {
        new loginError();
    }
}