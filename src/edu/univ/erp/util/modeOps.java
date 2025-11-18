package edu.univ.erp.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import edu.univ.erp.data.DatabaseConnector;

public class modeOps {
    public static String getMaintainMode() {
        String maintain_mode = "failure";
        String failure = "failure";
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        Select settings_value FROM settings WHERE settings_key = ?;
                    """)) {
                statement.setString(1, "maintain_mode");
                try (ResultSet resultSet = statement.executeQuery()) {
                    boolean empty = true;
                    while (resultSet.next()) {
                        empty = false;
                        maintain_mode = resultSet.getString("settings_value");
                    }
                    if (empty) {
                        JOptionPane.showMessageDialog(null, "Error: No key value exists in database", "Error", JOptionPane.ERROR_MESSAGE);
                        System.out.println("\t (No key value exists in database)");
                        return failure;
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "A database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: A database error occurred: " + ex);
            ex.printStackTrace();
            return failure;
        }
        return maintain_mode;
    }
    
    public static String setMaintainMode(String value_in) {
        String maintain_mode = "failure";
        String failure = "failure";
        try (Connection connection = DatabaseConnector.getErpConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        update settings set settings_value = ? WHERE settings_key = ?;
                    """)) {
                statement.setString(1, value_in);
                statement.setString(2, "maintain_mode");
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected == 0) {
                    JOptionPane.showMessageDialog(null, "Error: No key value exists in database", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("\t(No key value exists in database)");
                    return failure;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "A database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: A database error occurred: " + ex);
            ex.printStackTrace();
            return failure;
        }
        maintain_mode = "suckcess";
        return maintain_mode;
    }
}
