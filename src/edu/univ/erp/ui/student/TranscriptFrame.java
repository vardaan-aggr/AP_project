package edu.univ.erp.ui.student;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TranscriptFrame {

    private JFrame frame;

    public TranscriptFrame() {
        frame = new JFrame("Download Transcript");
        frame.setSize(600, 500);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("Unofficial Transcript");
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        titleLabel.setBounds(200, 20, 200, 20);
        frame.add(titleLabel);

        // Transcript Preview Area
        JTextArea transcriptArea = new JTextArea();
        transcriptArea.setEditable(false);
        transcriptArea.setText(
            "--- Unofficial Transcript ---\n\n" +
            "Student: [Your Name]\n" +
            "Program: B.Tech Computer Science\n\n" +
            "--- Completed Courses ---\n" +
            "CS101\tIntro to CS\tA-\n" +
            "MATH101\tCalculus I\tA\n" +
            "PHY101\tPhysics I\tB+\n\n" +
            "--- In Progress ---\n" +
            "CS201\tData Structures\tIP\n\n" +
            "CGPA: 3.80 / 4.00"
        );
        JScrollPane scrollPane = new JScrollPane(transcriptArea);
        scrollPane.setBounds(20, 50, 540, 350);
        frame.add(scrollPane);

        // Buttons
        JButton downloadButton = new JButton("Download as CSV");
        downloadButton.setBounds(150, 420, 150, 30);
        frame.add(downloadButton);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setBounds(320, 420, 150, 30);
        frame.add(backButton);

        frame.setVisible(true);

        // Action Listeners
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new studentDashboard(); // Open the dashboard
                frame.dispose(); // Close this window
            }
        });

        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // In a real app, this would call your service layer
                // which uses JFileChooser to save a CSV/PDF file.
                // For now, just show a confirmation.
                JOptionPane.showMessageDialog(frame, "Transcript downloaded! (Mockup)");
            }
        });
    }
}

