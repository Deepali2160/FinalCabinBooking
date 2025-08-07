package com.yash.cabinbooking.controller;

import com.yash.cabinbooking.model.Booking;
import com.yash.cabinbooking.service.BookingService;
import com.yash.cabinbooking.serviceimpl.BookingServiceImpl;
import com.yash.cabinbooking.util.DBConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/admin/booking-approval")
public class AdminBookingApprovalServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AdminBookingApprovalServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                throw new ServletException("Database connection unavailable");
            }

            BookingService bookingService = new BookingServiceImpl(conn);

            if ("list".equals(action)) {
                // List all pending approval bookings
                List<Booking> pendingBookings = bookingService.getBookingsByApprovalStatus("pending_approval");
                request.setAttribute("pendingBookings", pendingBookings);
                RequestDispatcher rd = request.getRequestDispatcher("/admin/pending-bookings.jsp");
                rd.forward(request, response);

            } else if ("view".equals(action)) {
                String idStr = request.getParameter("id");
                if (idStr == null || idStr.isEmpty()) {
                    response.sendRedirect("booking-approval?action=list");
                    return;
                }
                int bookingId;
                try {
                    bookingId = Integer.parseInt(idStr);
                } catch (NumberFormatException e) {
                    logger.warning("Invalid booking ID format: " + idStr);
                    response.sendRedirect("booking-approval?action=list&error=Invalid booking ID");
                    return;
                }

                Booking booking = bookingService.getBookingById(bookingId);
                if (booking == null) {
                    request.setAttribute("error", "Booking not found");
                }
                request.setAttribute("booking", booking);
                RequestDispatcher rd = request.getRequestDispatcher("/admin/booking-approval-view.jsp");
                rd.forward(request, response);

            } else {
                // Unknown action parameter - redirect to list
                response.sendRedirect("booking-approval?action=list");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in AdminBookingApprovalServlet GET", e);
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            // No action specified, redirect to list page
            response.sendRedirect("booking-approval?action=list");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                throw new ServletException("Database connection unavailable");
            }

            BookingService bookingService = new BookingServiceImpl(conn);

            if ("approve".equals(action) || "reject".equals(action)) {
                String bookingIdStr = request.getParameter("bookingId");
                if (bookingIdStr == null || bookingIdStr.isEmpty()) {
                    response.sendRedirect("booking-approval?action=list&error=Missing booking ID");
                    return;
                }

                int bookingId;
                try {
                    bookingId = Integer.parseInt(bookingIdStr);
                } catch (NumberFormatException e) {
                    logger.warning("Invalid booking ID format: " + bookingIdStr);
                    response.sendRedirect("booking-approval?action=list&error=Invalid booking ID");
                    return;
                }

                String adminRemarks = request.getParameter("adminRemarks");
                String rejectionReason = "reject".equals(action) ? request.getParameter("rejectionReason") : null;

                // Retrieve adminId from session to verify admin is logged in
                HttpSession session = request.getSession(false);
                Integer adminId = null;
                if (session == null) {
                    logger.warning("No session found, redirecting to login.");
                } else {
                    Object adminIdObj = session.getAttribute("adminId");
                    if (adminIdObj instanceof Integer) {
                        adminId = (Integer) adminIdObj;
                    } else if (adminIdObj != null) {
                        try {
                            adminId = Integer.parseInt(adminIdObj.toString());
                        } catch (NumberFormatException nfe) {
                            logger.warning("Invalid adminId in session: " + adminIdObj.toString());
                        }
                    }
                }

                if (adminId == null) {
                    logger.warning("Admin not logged in or adminId not found in session. Redirecting to login.");
                    response.sendRedirect(request.getContextPath() + "/login.jsp");
                    return;
                }

                // Set the new approval status
                String newApprovalStatus = "approve".equals(action) ? "approved" : "rejected";

                boolean success = bookingService.updateApprovalStatus(bookingId, newApprovalStatus,
                        adminId, adminRemarks,
                        rejectionReason);

                if (success) {
                    // Update main booking status accordingly
                    if ("approve".equals(action)) {
                        bookingService.confirmBooking(bookingId);  // Sets status = "confirmed"
                    } else {
                        bookingService.cancelBooking(bookingId);   // Sets status = "cancelled"
                    }

                    // TODO: Send notification to user if required

                    response.sendRedirect("booking-approval?action=list&message=Booking " + newApprovalStatus + " successfully");
                } else {
                    response.sendRedirect("booking-approval?action=list&error=Could not update booking status");
                }

            } else {
                // Unsupported or unknown action - redirect to list
                response.sendRedirect("booking-approval?action=list");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing approval action", e);
            throw new ServletException(e);
        }
    }
}
