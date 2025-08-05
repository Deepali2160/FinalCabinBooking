package com.yash.cabinbooking.controller;

import com.yash.cabinbooking.dao.BookingDao;
import com.yash.cabinbooking.daoimpl.BookingDaoImpl;
import com.yash.cabinbooking.model.Booking;
import com.yash.cabinbooking.util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;

@WebServlet("/payment")  // ✅ this replaces web.xml mapping
public class PaymentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Step 1: Validate payment fields
            String cardNumber = request.getParameter("cardNumber");
            String expiry = request.getParameter("expiry");
            String cvv = request.getParameter("cvv");

            if (cardNumber == null || cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
                response.getWriter().write("Invalid card number.");
                return;
            }

            if (cvv == null || cvv.length() != 3 || !cvv.matches("\\d+")) {
                response.getWriter().write("Invalid CVV.");
                return;
            }

            // Step 2: Retrieve booking details from form
            int userId = Integer.parseInt(request.getParameter("userId"));
            String cabinName = request.getParameter("cabinName");
            java.util.Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("startDate"));
            java.util.Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("endDate"));
            int guests = Integer.parseInt(request.getParameter("guests"));
            double amount = Double.parseDouble(request.getParameter("amount"));

            // Step 3: Create Booking object
            Booking booking = new Booking();
            booking.setUserId(userId);
            booking.setCabinName(cabinName);
            booking.setStartDate(startDate);
            booking.setEndDate(endDate);
            booking.setGuests(guests);
            booking.setAmount(amount);
            booking.setStatus("Confirmed");

            // Step 4: Save to DB using DAO
            Connection conn = DBConnection.getConnection();
            BookingDao bookingDao = new BookingDaoImpl(conn);

            boolean success = bookingDao.addBooking(booking);
            System.out.println("Booking insert status: " + success);

            if (success) {
                HttpSession session = request.getSession();
                session.setAttribute("cabinName", cabinName);
                session.setAttribute("amount", amount);

                response.sendRedirect("success.jsp");
            }
            else {
                response.getWriter().write("Booking failed. Please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("An error occurred while processing your payment.");
        }
    }
}