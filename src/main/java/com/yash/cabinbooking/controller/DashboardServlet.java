package com.yash.cabinbooking.controller;

import com.yash.cabinbooking.serviceimpl.CabinServiceImpl;
import com.yash.cabinbooking.model.Cabin;
import com.yash.cabinbooking.service.CabinService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(DashboardServlet.class.getName());
    private final CabinService cabinService = new CabinServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("DashboardServlet: Fetching cabins...");

        try {
            List<Cabin> allCabins = cabinService.getAllCabins();
            logger.info("Total cabins fetched: " + allCabins.size());

            // Handle null createdAt values in sorting
            allCabins.sort(Comparator.comparing(
                    c -> c.getCreatedAt() != null ? c.getCreatedAt() : LocalDateTime.MIN,
                    Comparator.reverseOrder()
            ));

            request.setAttribute("totalCabins", allCabins.size());
            request.setAttribute("recentCabins", allCabins.stream().limit(3).collect(Collectors.toList()));

            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in DashboardServlet", e);
            request.setAttribute("totalCabins", 0);
            request.setAttribute("recentCabins", Collections.emptyList());
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        }
    }
}