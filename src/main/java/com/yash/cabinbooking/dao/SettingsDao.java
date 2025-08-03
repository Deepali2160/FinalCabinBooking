// src/main/java/com/yash/cabinbooking/dao/SettingsDao.java
package com.yash.cabinbooking.dao;

import java.util.Map;

public interface SettingsDao {
    Map<String, String> getAllSettings();
    void updateSetting(String name, String value);
}