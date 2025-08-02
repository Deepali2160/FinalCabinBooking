package com.yash.cabinbooking.controller;

import com.yash.cabinbooking.service.impl.CabinServiceImpl;
import com.yash.cabinbooking.model.Cabin;
import com.yash.cabinbooking.service.CabinService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {
    private CabinService cabinService = new CabinServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get all cabins
            List<Cabin> allCabins = cabinService.getAllCabins();
            System.out.println("[DEBUG] Total cabins fetched: " + allCabins.size());

            // Debug print all cabin details
            allCabins.forEach(c -> {
                System.out.println("[DEBUG] Cabin ID: " + c.getId());
                System.out.println("[DEBUG] Name: " + c.getName());
                System.out.println("[DEBUG] Created At: " + c.getCreatedAt());
                System.out.println("[DEBUG] Image URL: " + c.getImageUrl());
                System.out.println("[DEBUG] Available: " + c.isAvailable());
                System.out.println("[DEBUG] ----------------------");
            });

            // Set total cabins count
            request.setAttribute("totalCabins", allCabins.size());

            // Get 3 most recent cabins
            List<Cabin> recentCabins = allCabins.stream()
                    .limit(3)
                    .collect(Collectors.toList());

            System.out.println("[DEBUG] Sending " + recentCabins.size() + " recent cabins to view");

            // Set attributes
            request.setAttribute("recentCabins", recentCabins);
            // Right before forwarding to JSP, add:
            System.out.println("[SERVLET DEBUG] Attributes being sent:");
            System.out.println("totalCabins: " + request.getAttribute("totalCabins"));
            System.out.println("recentCabins: " + ((List<?>)request.getAttribute("recentCabins")).size());

            // Forward to JSP
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("[ERROR] in DashboardServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error loading dashboard");
            // Right before forwarding to JSP, add:
            System.out.println("[SERVLET DEBUG] Attributes being sent:");
            System.out.println("totalCabins: " + request.getAttribute("totalCabins"));
            System.out.println("recentCabins: " + ((List<?>)request.getAttribute("recentCabins")).size());
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        }
    }
}