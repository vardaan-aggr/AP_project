package edu.univ.erp.ui.common;

import edu.univ.erp.domain.Notification;
import edu.univ.erp.data.NotificationCommandRunner; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NotificationDialog extends JDialog {

    public NotificationDialog(JFrame parent, String userIdStr) {
        super(parent, "Notifications", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Simple Table: Just one column
        String[] columns = {"Messages"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        
        try {
            int userId = Integer.parseInt(userIdStr);
            List<Notification> notifs = NotificationCommandRunner.getNotifications(userId);
            
            for (Notification n : notifs) {
                model.addRow(new Object[]{n.getMessage()});
            }
            if (notifs.isEmpty()) {
                model.addRow(new Object[]{"No new messages."});
            }
        } catch (NumberFormatException e) {
            model.addRow(new Object[]{"Error: Invalid User ID"});
        }

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        add(closeBtn, BorderLayout.SOUTH);

        setVisible(true);
    }
}