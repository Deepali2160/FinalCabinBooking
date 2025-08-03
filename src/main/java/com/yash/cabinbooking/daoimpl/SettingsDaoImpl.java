// src/main/java/com/yash/cabinbooking/daoimpl/SettingsDaoImpl.java
package com.yash.cabinbooking.daoimpl;

import com.yash.cabinbooking.dao.SettingsDao;
import com.yash.cabinbooking.util.DBConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SettingsDaoImpl implements SettingsDao {
    @Override
    public Map<String, String> getAllSettings() {
        Map<String, String> settings = new HashMap<>();
        String query = "SELECT * FROM system_settings";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                settings.put(rs.getString("setting_name"), rs.getString("setting_value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return settings;
    }

    @Override
    public void updateSetting(String name, String value) {
        String query = "UPDATE system_settings SET setting_value = ? WHERE setting_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, value);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}