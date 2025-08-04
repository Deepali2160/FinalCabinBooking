package com.yash.cabinbooking.controller;


import com.yash.cabinbooking.model.Booking;
import com.yash.cabinbooking.service.BookingService;
import com.yash.cabinbooking.serviceimpl.BookingServiceImpl;
import com.yash.cabinbooking.util.DBConnection;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = 1; // Example: Session se le sakte ho actual user ka ID

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        BookingService bookingService = new BookingServiceImpl(conn);

        List<Booking> bookings = bookingService.getBookingsForUser(userId);

        request.setAttribute("bookings", bookings);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/user-dashboard.jsp");
        dispatcher.forward(request, response);
    }
}
