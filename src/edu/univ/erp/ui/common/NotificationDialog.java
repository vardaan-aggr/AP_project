package edu.univ.erp.ui.common;

import edu.univ.erp.domain.Notification;
import edu.univ.erp.data.NotificationCommandRunner; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NotificationDialog {

    public NotificationDialog(JFrame parent, String userRollNo_in) {
        
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JDialog d = new JDialog(parent, "Notifications", true); 
        d.setSize(400, 300);
        d.setLocationRelativeTo(parent);
        d.setLayout(new BorderLayout());

        // Simple Table: Just one column
        String[] columns = {"Messages"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        
        // Load Data
        try {
            int userRollNo = Integer.parseInt(userRollNo_in);
            List<Notification> notifs = NotificationCommandRunner.getNotifications(userRollNo);
            
            for (Notification n : notifs) {
                model.addRow(new Object[]{n.getMessage()});
            }
            if (notifs.isEmpty()) {
                model.addRow(new Object[]{"No new messages."});
            }
        } catch (NumberFormatException e) {
            model.addRow(new Object[]{"Error: Invalid User ID"});
        }

        d.add(new JScrollPane(table), BorderLayout.CENTER);

        // Close Button
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(Color.decode("#2f77b1"));
        closeBtn.setForeground(Color.WHITE);
        
        closeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                d.dispose();
            }
        });

        d.add(closeBtn, BorderLayout.SOUTH);

        d.setVisible(true);
    }
}