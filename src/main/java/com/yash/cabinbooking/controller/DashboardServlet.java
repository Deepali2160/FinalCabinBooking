package com.yash.cabinbooking.controller;

import com.yash.cabinbooking.model.Cabin;
import com.yash.cabinbooking.service.BookingService;
import com.yash.cabinbooking.service.CabinService;
import com.yash.cabinbooking.serviceimpl.BookingServiceImpl;
import com.yash.cabinbooking.serviceimpl.CabinServiceImpl;
import com.yash.cabinbooking.util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(DashboardServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("DashboardServlet: Starting to process dashboard data request");

        try (Connection connection = DBConnection.getConnection()) {
            if (connection == null) {
                logger.severe("Failed to obtain database connection");
                throw new ServletException("Database connection not available");
            }

            // Instantiate services with connection parameter (important!)
            BookingService bookingService = new BookingServiceImpl(connection);
            CabinService cabinService = new CabinServiceImpl();

            // Fetch all cabins and sort by createdAt descending (newest first)
            List<Cabin> allCabins = cabinService.getAllCabins();
            allCabins.sort(Comparator.comparing(
                    c -> c.getCreatedAt() != null ? c.getCreatedAt() : LocalDateTime.MIN,
                    Comparator.reverseOrder()
            ));

            int pendingApprovals = 0;
            int approvedBookingsThisMonth = 0;
            int rejectedBookings = 0;
            double revenueThisMonth = 0.0;

            try {
                pendingApprovals = bookingService.countBookingsByApprovalStatus("pending_approval");
                System.out.println("DEBUG: Pending Approvals count = " + pendingApprovals);
                logger.info("Pending Approvals count = " + pendingApprovals);

                approvedBookingsThisMonth = bookingService.countBookingsApprovedThisMonth();
                rejectedBookings = bookingService.countBookingsByApprovalStatus("rejected");
                revenueThisMonth = bookingService.calculateMonthlyRevenue();

            } catch (Exception e) {
                System.err.println("ERROR fetching booking stats:");
                e.printStackTrace();
                logger.log(Level.SEVERE, "Error fetching booking stats", e);
            }

            // Set attributes for JSP rendering
            request.setAttribute("totalCabins", allCabins.size());
            request.setAttribute("recentCabins", allCabins.stream().limit(3).collect(Collectors.toList()));

            request.setAttribute("pendingApprovals", pendingApprovals);
            request.setAttribute("approvedBookingsThisMonth", approvedBookingsThisMonth);
            request.setAttribute("rejectedBookings", rejectedBookings);
            request.setAttribute("revenueThisMonth", revenueThisMonth);

            logger.info("DashboardServlet: Data fetched successfully, forwarding to JSP");

            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "DashboardServlet: Error processing request", e);
            e.printStackTrace();

            // On error, forward with empty/default values but page should not crash
            request.setAttribute("totalCabins", 0);
            request.setAttribute("recentCabins", Collections.emptyList());

            request.setAttribute("pendingApprovals", 0);
            request.setAttribute("approvedBookingsThisMonth", 0);
            request.setAttribute("rejectedBookings", 0);
            request.setAttribute("revenueThisMonth", 0.0);

            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        }
    }
}
