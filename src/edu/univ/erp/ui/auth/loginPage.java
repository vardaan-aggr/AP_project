package edu.univ.erp.ui.auth;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import edu.univ.erp.util.BREATHEFONT;
import edu.univ.erp.auth.changePassword;
import edu.univ.erp.service.LoginService;
import edu.univ.erp.ui.student.studentDashboard;
import edu.univ.erp.ui.admin.adminDashboard;
import edu.univ.erp.ui.instructor.InstructorDashboard;

import java.awt.*;
import java.awt.event.ActionEvent;   
import java.awt.event.ActionListener;

public class loginPage {
    
    public loginPage() {
        Font breatheFont = BREATHEFONT.fontGen();
        Font gFont = BREATHEFONT.gFontGen();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame f = new JFrame("University ERP");
        f.setSize(800,600);
        f.setLayout(new BorderLayout());
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        
        // ---- Top ----
        JPanel p1 = new JPanel();    
        p1.setBackground(Color.decode("#051072")); 
        p1.setOpaque(true); 
        JLabel l0 = new JLabel("ERP LOGIN");
        l0.setForeground(Color.decode("#dbd3c5"));
        l0.setFont(breatheFont.deriveFont(Font.BOLD, 100));
        l0.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        p1.add(l0);
        f.add(p1, BorderLayout.NORTH);

        // ---- Middle ----
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setBackground(Color.decode("#dbd3c5")); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel l1 = new JLabel("Username:");
        l1.setFont(gFont.deriveFont(Font.PLAIN, 24));
        gbc.gridx = 0; gbc.gridy = 0;
        p2.add(l1, gbc);

        JTextField tUsername = new JTextField(20);
        tUsername.setFont(gFont.deriveFont(Font.PLAIN, 21));
        gbc.gridx = 1; gbc.gridy = 0;
        p2.add(tUsername, gbc);
        
        JLabel l2 = new JLabel("Password:");
        l2.setFont(gFont.deriveFont(Font.PLAIN, 24));
        gbc.gridx = 0; gbc.gridy = 1;
        p2.add(l2, gbc);

        JPasswordField tPassword = new JPasswordField(20);
        tPassword.setFont(gFont.deriveFont(Font.PLAIN, 22));
        gbc.gridx = 1; gbc.gridy = 1;
        p2.add(tPassword, gbc);
        
        f.add(p2, BorderLayout.CENTER);
        
        // ---- Bottom ----
        JPanel p3 = new JPanel();
        p3.setBackground(Color.decode("#dbd3c5"));
        p3.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        
        JButton bLogin = new JButton("LOGIN");
        bLogin.setBackground(Color.decode("#2f77b1")); 
        bLogin.setForeground(Color.WHITE); 
        bLogin.setFont(breatheFont.deriveFont(Font.PLAIN, 35f));
        bLogin.setMargin(new Insets(10, 30, 5, 30));

        JButton bChangePass = new JButton("Change Password");
        bChangePass.setBackground(Color.decode("#87c3fa")); 
        bChangePass.setForeground(Color.WHITE); 
        bChangePass.setFont(breatheFont.deriveFont(Font.PLAIN, 30f));
        bChangePass.setMargin(new Insets(10, 30, 5, 30));

        p3.add(bLogin);
        p3.add(Box.createHorizontalStrut(20)); 
        p3.add(bChangePass);
        f.add(p3, BorderLayout.SOUTH);

        f.setVisible(true);
        
        // Default text
        tUsername.setText("adm1");
        tPassword.setText("mdo1");

        // --- ACTIONS ---
        bLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = tUsername.getText().trim();
                String pass = new String(tPassword.getPassword());

                LoginService service = new LoginService();
                int status = service.login(user, pass);

                if (status == LoginService.SUCCESS) {
                    String role = service.loggedInRole;
                    String rollNo = service.loggedInRollNo;
                    
                    System.out.println("Login Success: " + role);

                    if (role.equals("student")) {
                        new studentDashboard(user, role, pass, rollNo);
                    } else if (role.equals("instructor")) {
                        new InstructorDashboard(user, role, pass, rollNo);
                    } else if (role.equals("admin")) {
                        new adminDashboard(rollNo);
                    }
                    f.dispose();
                } 
                else if (status == LoginService.INVALID) {
                    JOptionPane.showMessageDialog(f, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                } 
                else if (status == LoginService.LOCKED) {
                    JOptionPane.showMessageDialog(f, "Account Locked! Too many failed attempts.", "Locked", JOptionPane.WARNING_MESSAGE);
                } 
                else {
                    JOptionPane.showMessageDialog(f, "Database Error.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        bChangePass.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = tUsername.getText();
                if (user.isEmpty()) {
                     JOptionPane.showMessageDialog(f, "Enter username first.", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    new changePassword(user);
                    f.dispose();
                }
            }
        });
    }

    public static void main(String[] args) {
        new loginPage();
    }
}