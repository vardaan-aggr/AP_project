package edu.univ.erp.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout; // Import MigLayout

/**
 * Ultra-modern professional Java Swing login using FlatLaf.
 * Features: Glass-morphism card, modern spacing, refined colors, smooth interactions.
 * Requires flatlaf-x.x.jar and miglayout-swing-x.x.jar in classpath.
 */
public class trash {

    // --- Modern color palette moved here ---
    static final Color PRIMARY_COLOR = new Color(79, 70, 229); // Indigo
    static final Color PRIMARY_HOVER = new Color(67, 56, 202);
    static final Color BACKGROUND = new Color(249, 250, 251);
    static final Color BORDER_COLOR = new Color(229, 231, 235);
    static final Color TEXT_PRIMARY = new Color(17, 24, 39);

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            
            // --- Global FlatLaf Styling ---
            // This is the key to a professional look.
            
            // Modern rounded corners
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            
            // Set default border and focus colors for text fields
            UIManager.put("TextComponent.borderColor", BORDER_COLOR);
            UIManager.put("TextComponent.focusColor", PRIMARY_COLOR); // Use primary color for focus
            UIManager.put("TextComponent.focusedBorderColor", PRIMARY_COLOR);
            UIManager.put("Component.focusWidth", 1); // Thinner focus ring
            UIManager.put("Component.innerFocusWidth", 0); // No inner ring
            
            // Define the "primary" button style
            UIManager.put("Button.primary.background", PRIMARY_COLOR);
            UIManager.put("Button.primary.hoverBackground", PRIMARY_HOVER);
            UIManager.put("Button.primary.foreground", Color.WHITE);
            UIManager.put("Button.primary.focusedBorderColor", PRIMARY_COLOR.brighter());

        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize FlatLaf.");
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}

class LoginFrame extends JFrame {
    
    // Use the static colors from the main class
    private static final Color PRIMARY_COLOR = trash.PRIMARY_COLOR;
    private static final Color PRIMARY_HOVER = trash.PRIMARY_HOVER;
    private static final Color BACKGROUND = trash.BACKGROUND;
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = trash.TEXT_PRIMARY;
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private static final Color BORDER_COLOR = trash.BORDER_COLOR;

    public LoginFrame() {
        setTitle("University ERP System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Main container with modern background
        JPanel mainContainer = new JPanel(new GridBagLayout()); // GridBagLayout is perfect for centering
        mainContainer.setBackground(BACKGROUND);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Create the login card
        JPanel loginCard = createLoginCard();
        mainContainer.add(loginCard); // Add card to the center

        add(mainContainer);
        pack(); // Pack the frame to fit the card's preferred size
        setLocationRelativeTo(null); // Center after packing
    }

    private JPanel createLoginCard() {
        // We use MigLayout on the card for perfect vertical stacking
        JPanel card = new JPanel(new MigLayout(
            "wrap 1", // One component per line (single column)
            "[fill, grow]", // Column 0: fill and grow to container width
            "[]" // Rows: default (no specific alignment needed)
        ));

        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createEmptyBorder(45, 45, 45, 45));
        
        // Subtle shadow effect
        card.putClientProperty("FlatLaf.style", "arc: 16; border: 1,1,1,1," + 
            BORDER_COLOR.getRed() + "," + BORDER_COLOR.getGreen() + "," + BORDER_COLOR.getBlue());

        // Header section
        card.add(createHeaderSection(), "gapbottom 35"); // Add header with a 35px gap below it

        // Form section
        card.add(createFormSection(), "gapbottom 25"); // Add form with 25px gap below

        // Footer section
        card.add(createFooterSection());

        return card;
    }

    private JPanel createHeaderSection() {
        // MigLayout for the header, single column, all left-aligned
        JPanel header = new JPanel(new MigLayout("wrap 1", "[left]", "[]"));
        header.setBackground(CARD_BG);

        // Brand icon
        JPanel iconContainer = new JPanel(new GridBagLayout());
        iconContainer.setBackground(new Color(238, 242, 255)); // Light indigo
        iconContainer.setPreferredSize(new Dimension(64, 64));
        iconContainer.putClientProperty("FlatLaf.style", "arc: 16");
        
        JLabel icon = new JLabel("🎓");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconContainer.add(icon);
        
        header.add(iconContainer, "gapbottom 20"); // Add icon with 20px gap

        // Welcome text
        JLabel welcome = new JLabel("Welcome back");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcome.setForeground(TEXT_PRIMARY);
        header.add(welcome, "gapbottom 8"); // 8px gap

        // Subtitle
        JLabel subtitle = new JLabel("Sign in to access your ERP dashboard");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        header.add(subtitle); // No gap after last item

        return header;
    }

    private JPanel createFormSection() {
        // MigLayout for the form, single column, all left-aligned
        JPanel form = new JPanel(new MigLayout("wrap 1", "[fill, grow]", "[]"));
        form.setBackground(CARD_BG);

        // Username field with label
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(TEXT_PRIMARY);
        form.add(lblUser, "gapbottom 8");

        JTextField txtUsername = new JTextField();
        styleTextField(txtUsername); // Apply the shared style
        txtUsername.putClientProperty("FlatLaf.placeholderText", "Enter your username");
        form.add(txtUsername, "gapbottom 20, h 44!"); // Set height to 44px

        // Password field with label
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(TEXT_PRIMARY);
        form.add(lblPass, "gapbottom 8");

        JPasswordField txtPassword = new JPasswordField();
        styleTextField(txtPassword); // Apply the shared style
        txtPassword.putClientProperty("FlatLaf.placeholderText", "Enter your password");
        form.add(txtPassword, "gapbottom 12, h 44!"); // Set height to 44px

        // Forgot password link
        JLabel lblForgot = new JLabel("<html><u>Forgot password?</u></html>");
        lblForgot.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblForgot.setForeground(PRIMARY_COLOR);
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblForgot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                    "Please contact your system administrator for password recovery.",
                    "Password Recovery",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblForgot.setForeground(PRIMARY_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblForgot.setForeground(PRIMARY_COLOR);
            }
        });
        form.add(lblForgot, "gapbottom 28"); // 28px gap before button

        // Sign in button
        JButton btnLogin = new JButton("Sign In");
        
        // --- This is the professional way ---
        // Let FlatLaf do ALL the styling. No manual colors, borders, or hovers needed.
        btnLogin.putClientProperty("FlatLaf.style", "primary; large");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        form.add(btnLogin, "h 48!, growx"); // Set height to 48px, make it grow to fill width

        // Action listeners
        ActionListener loginAction = e -> 
            performLogin(txtUsername.getText(), new String(txtPassword.getPassword()));
        
        btnLogin.addActionListener(loginAction);
        txtPassword.addActionListener(loginAction);
        txtUsername.addActionListener(loginAction);

        return form;
    }

    private JPanel createFooterSection() {
        // MigLayout for footer, single column, items centered
        JPanel footer = new JPanel(new MigLayout("wrap 1", "[center, grow]", "[]"));
        footer.setBackground(CARD_BG);

        // Divider
        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER_COLOR);
        footer.add(separator, "growx, gapbottom 20"); // Make separator fill width

        // Help text
        JLabel lblHelp = new JLabel("Need help? Contact support@university.edu");
        lblHelp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblHelp.setForeground(TEXT_SECONDARY);
        footer.add(lblHelp, "gapbottom 8");

        // Copyright
        JLabel lblCopyright = new JLabel("© 2024 University ERP System");
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCopyright.setForeground(TEXT_SECONDARY);
        footer.add(lblCopyright);

        return footer;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // We don't need a custom border or focus listener anymore!
        // FlatLaf UIManager properties in main() are handling it.
        // We just add padding using a client property.
        field.putClientProperty("FlatLaf.padding", new Insets(10, 14, 10, 14));
    }

    private void performLogin(String username, String password) {
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        // Simulate authentication
        if (username.equals("admin") && password.equals("admin123")) {
            JOptionPane.showMessageDialog(this,
                "Login successful! Welcome, " + username + ".",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            // In production: launch dashboard
            // new Dashboard().setVisible(true);
            // this.dispose();
        } else {
            showError("Invalid username or password. Please try again.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Authentication Error",
            JOptionPane.ERROR_MESSAGE);
    }
}