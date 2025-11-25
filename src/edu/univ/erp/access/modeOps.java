package edu.univ.erp.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.univ.erp.data.DatabaseConnector;
import edu.univ.erp.domain.Settings;

public class modeOps {

    // Retrieves a setting from the database by its key.
    public static Settings getSetting(String key) {
        Settings setting = null;
        String query = "SELECT settings_value FROM settings WHERE settings_key = ?";

        try (Connection connection = DatabaseConnector.getErpConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, key);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String val = resultSet.getString("settings_value");
                    // Create the Domain Object
                    setting = new Settings(key, val);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error fetching setting [" + key + "]: " + ex.getMessage());
            ex.printStackTrace();
        }
        return setting;
    }
    
    //Updates a setting in the database using a Settings object.
    public static boolean updateSetting(Settings setting) {
        String query = "UPDATE settings SET settings_value = ? WHERE settings_key = ?";
        
        try (Connection connection = DatabaseConnector.getErpConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, setting.getValue());
            statement.setString(2, setting.getKey());
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException ex) {
            System.out.println("Error updating setting [" + setting.getKey() + "]: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}