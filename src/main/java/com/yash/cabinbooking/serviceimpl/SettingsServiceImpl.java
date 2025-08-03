// src/main/java/com/yash/cabinbooking/serviceimpl/SettingsServiceImpl.java
package com.yash.cabinbooking.serviceimpl;

import com.yash.cabinbooking.dao.SettingsDao;
import com.yash.cabinbooking.daoimpl.SettingsDaoImpl;
import com.yash.cabinbooking.service.SettingsService;
import java.util.Map;

public class SettingsServiceImpl implements SettingsService {
    private SettingsDao settingsDao = new SettingsDaoImpl();

    @Override
    public Map<String, String> getAllSettings() {
        return settingsDao.getAllSettings();
    }

    @Override
    public void updateSetting(String name, String value) {
        settingsDao.updateSetting(name, value);
    }
}