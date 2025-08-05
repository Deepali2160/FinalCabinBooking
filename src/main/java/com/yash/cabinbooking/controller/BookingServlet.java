package com.yash.cabinbooking.controller;

import com.yash.cabinbooking.model.Booking;
import com.yash.cabinbooking.service.BookingService;
import com.yash.cabinbooking.serviceimpl.BookingServiceImpl;
import com.yash.cabinbooking.util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/book")
public class BookingServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String cabinName = request.getParameter("cabinName");
            String start = request.getParameter("startDate");
            String end = request.getParameter("endDate");
            int guests = Integer.parseInt(request.getParameter("guests"));
            double amount = Double.parseDouble(request.getParameter("amount"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);

            Booking booking = new Booking();
            booking.setUserId(userId);
            booking.setCabinName(cabinName);
            booking.setStartDate(startDate);
            booking.setEndDate(endDate);
            booking.setGuests(guests);
            booking.setAmount(amount);
            booking.setStatus("Pending");

            Connection conn = DBConnection.getConnection();
            BookingService service = new BookingServiceImpl(conn);
            service.addBooking(booking); // Ye method abhi banate hain

            response.sendRedirect("payment.jsp?userId=" + userId +
                    "&cabinName=" + cabinName +
                    "&startDate=" + start +
                    "&endDate=" + end +
                    "&guests=" + guests +
                    "&amount=" + amount);


        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}