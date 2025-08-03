// src/main/java/com/yash/cabinbooking/service/SettingsService.java
package com.yash.cabinbooking.service;

import java.util.Map;

public interface SettingsService {
    Map<String, String> getAllSettings();
    void updateSetting(String name, String value);
}