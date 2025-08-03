// src/main/java/com/yash/cabinbooking/controller/SettingsServlet.java
package com.yash.cabinbooking.controller;

import com.yash.cabinbooking.service.SettingsService;
import com.yash.cabinbooking.serviceimpl.SettingsServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/settings")
public class SettingsServlet extends HttpServlet {
    private SettingsService settingsService = new SettingsServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Load all settings to display in the form
        request.setAttribute("settings", settingsService.getAllSettings());
        request.getRequestDispatcher("/admin/settings.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Update settings
        String settingName = request.getParameter("settingName");
        String settingValue = request.getParameter("settingValue");

        settingsService.updateSetting(settingName, settingValue);
        response.sendRedirect(request.getContextPath() + "/admin/settings?success=true");
    }
}