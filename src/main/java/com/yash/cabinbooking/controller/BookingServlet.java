package com.yash.cabinbooking.controller;

import com.yash.cabinbooking.model.Booking;
import com.yash.cabinbooking.model.Cabin;
import com.yash.cabinbooking.service.BookingService;
import com.yash.cabinbooking.service.CabinService;
import com.yash.cabinbooking.serviceimpl.BookingServiceImpl;
import com.yash.cabinbooking.serviceimpl.CabinServiceImpl;
import com.yash.cabinbooking.util.DBConnection;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CabinService cabinService;

    @Override
    public void init() throws ServletException {
        cabinService = new CabinServiceImpl();
        System.out.println("BookingServlet initialized successfully");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        System.out.println("DEBUG: BookingServlet doGet - Action: " + action);

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Disable auto-commit for better control
            BookingService bookingService = new BookingServiceImpl(conn);

            switch (action) {
                case "list":
                    listBookings(request, response, bookingService);
                    break;
                case "view":
                    viewBooking(request, response, bookingService);
                    break;
                case "new":
                    showBookingForm(request, response);
                    break;
                case "edit":
                    editBooking(request, response, bookingService);
                    break;
                case "cancel":
                    cancelBooking(request, response, bookingService);
                    break;
                default:
                    listBookings(request, response, bookingService);
            }
            conn.commit(); // Commit successful operations
        } catch (SQLException e) {
            System.err.println("Database connection error in BookingServlet: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
                }
            }
            throw new ServletException("Database connection problem", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restore auto-commit
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing database connection: " + e.getMessage());
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        System.out.println("DEBUG: BookingServlet doPost - Action: " + action);

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Disable auto-commit for transaction control
            BookingService bookingService = new BookingServiceImpl(conn);

            switch (action) {
                case "create":
                    createBooking(request, response, bookingService, conn);
                    break;
                case "update":
                    updateBooking(request, response, bookingService);
                    break;
                case "updateStatus":
                    updateBookingStatus(request, response, bookingService);
                    break;
                case "updatePayment":
                    updatePaymentStatus(request, response, bookingService);
                    break;
                default:
                    System.out.println("DEBUG: Unknown action, redirecting to list");
                    conn.commit(); // Commit before redirect
                    response.sendRedirect("booking?action=list");
                    return;
            }

            // Don't commit here as individual methods handle their own commits

        } catch (SQLException e) {
            System.err.println("Database connection error in BookingServlet POST: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("DEBUG: Main transaction rolled back");
                } catch (SQLException rollbackEx) {
                    System.err.println("ERROR: Main rollback failed - " + rollbackEx.getMessage());
                }
            }
            throw new ServletException("Database connection problem", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restore auto-commit
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing database connection: " + e.getMessage());
                }
            }
        }
    }

    private void createBooking(HttpServletRequest request, HttpServletResponse response,
                               BookingService bookingService, Connection conn) throws IOException, ServletException {

        System.out.println("DEBUG: Creating booking...");

        try {
            // Extract parameters
            String userIdStr = request.getParameter("userId");
            String cabinIdStr = request.getParameter("cabinId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String guestsStr = request.getParameter("guests");

            System.out.println("DEBUG: Parameters - userId: " + userIdStr +
                    ", cabinId: " + cabinIdStr +
                    ", startDate: " + startDateStr +
                    ", endDate: " + endDateStr +
                    ", guests: " + guestsStr);

            // Validate parameters
            if (userIdStr == null || cabinIdStr == null || startDateStr == null ||
                    endDateStr == null || guestsStr == null) {
                System.out.println("DEBUG: Missing required parameters");
                conn.rollback();
                response.sendRedirect("cabins?error=missing_parameters");
                return;
            }

            Booking booking = new Booking();
            booking.setUserId(Integer.parseInt(userIdStr));
            booking.setCabinId(Integer.parseInt(cabinIdStr));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

            booking.setStartDate(startDate);
            booking.setEndDate(endDate);
            booking.setGuests(Integer.parseInt(guestsStr));
            booking.setStatus("pending");
            booking.setPaymentStatus("unpaid");

            System.out.println("DEBUG: Booking object created - User: " + booking.getUserId() +
                    ", Cabin: " + booking.getCabinId() +
                    ", Guests: " + booking.getGuests());

            // Get cabin details to calculate amount
            Cabin cabin = cabinService.getCabinById(booking.getCabinId());
            if (cabin == null) {
                System.out.println("DEBUG: Cabin not found for ID: " + booking.getCabinId());
                conn.rollback();
                response.sendRedirect("cabins?error=cabin_not_found");
                return;
            }

            System.out.println("DEBUG: Cabin found - Name: " + cabin.getName() +
                    ", Rate: " + cabin.getHourlyRate() +
                    ", Capacity: " + cabin.getCapacity());

            // Calculate amount based on cabin's hourly rate and duration
            Duration duration = Duration.between(startDate, endDate);
            long hours = Math.max(1, duration.toHours()); // Minimum 1 hour billing
            double totalAmount = hours * cabin.getHourlyRate();
            booking.setAmount(totalAmount);

            System.out.println("DEBUG: Amount calculated - Hours: " + hours +
                    ", Rate: " + cabin.getHourlyRate() +
                    ", Total: " + totalAmount);

            // Validate booking dates
            if (startDate.isAfter(endDate)) {
                System.out.println("DEBUG: Start date is after end date");
                conn.rollback();
                response.sendRedirect("cabins?action=book&id=" + booking.getCabinId() + "&error=invalid_dates");
                return;
            }

            if (startDate.isBefore(LocalDateTime.now().minusMinutes(30))) { // Allow 30 min buffer
                System.out.println("DEBUG: Start date is in the past");
                conn.rollback();
                response.sendRedirect("cabins?action=book&id=" + booking.getCabinId() + "&error=past_date");
                return;
            }

            // Check if cabin is still available
            if (!cabin.isAvailable()) {
                System.out.println("DEBUG: Cabin is not available");
                conn.rollback();
                response.sendRedirect("cabins?action=book&id=" + booking.getCabinId() + "&error=cabin_not_available");
                return;
            }

            // Check for booking conflicts
            if (bookingService.isBookingConflict(booking.getCabinId(), startDate, endDate, 0)) {
                System.out.println("DEBUG: Booking conflict detected");
                conn.rollback();
                response.sendRedirect("cabins?action=book&id=" + booking.getCabinId() + "&error=time_conflict");
                return;
            }

            // Create the booking
            boolean success = bookingService.createBooking(booking);
            System.out.println("DEBUG: Booking creation result: " + success);

            if (success) {
                // Explicit commit to ensure booking is saved
                conn.commit();
                System.out.println("DEBUG: Booking committed to database with ID: " + booking.getId());

                // Try to retrieve the booking immediately
                Booking createdBooking = bookingService.getBookingById(booking.getId());
                if (createdBooking != null) {
                    System.out.println("DEBUG: Created booking retrieved successfully with ID: " + createdBooking.getId());

                    // Store booking info in session for payment page
                    HttpSession session = request.getSession();
                    session.setAttribute("currentBooking", createdBooking);
                    session.setAttribute("currentCabin", cabin);

                    // Calculate duration for display
                    long displayHours = Duration.between(createdBooking.getStartDate(),
                            createdBooking.getEndDate()).toHours();
                    session.setAttribute("duration", displayHours);

                    // Redirect to payment page
                    System.out.println("DEBUG: Redirecting to payment page");
                    response.sendRedirect("payment?action=show&bookingId=" + createdBooking.getId());
                    return;
                } else {
                    // Fallback: Try getting by user bookings
                    System.out.println("DEBUG: Direct retrieval failed, trying user bookings");
                    List<Booking> userBookings = bookingService.getUserBookings(booking.getUserId());
                    if (!userBookings.isEmpty()) {
                        // Find the most recent booking (should be the one we just created)
                        Booking fallbackBooking = userBookings.stream()
                                .filter(b -> b.getCabinId() == booking.getCabinId() &&
                                        b.getStartDate().equals(booking.getStartDate()) &&
                                        Math.abs(b.getAmount() - booking.getAmount()) < 0.01)
                                .findFirst()
                                .orElse(userBookings.get(0));

                        System.out.println("DEBUG: Fallback booking found with ID: " + fallbackBooking.getId());

                        // Store booking info in session for payment page
                        HttpSession session = request.getSession();
                        session.setAttribute("currentBooking", fallbackBooking);
                        session.setAttribute("currentCabin", cabin);

                        // Calculate duration for display
                        long displayHours = Duration.between(fallbackBooking.getStartDate(),
                                fallbackBooking.getEndDate()).toHours();
                        session.setAttribute("duration", displayHours);

                        // Redirect to payment page
                        System.out.println("DEBUG: Redirecting to payment page via fallback");
                        response.sendRedirect("payment?action=show&bookingId=" + fallbackBooking.getId());
                        return;
                    } else {
                        System.out.println("DEBUG: No user bookings found after creation");
                        response.sendRedirect("booking?action=list&success=created");
                        return;
                    }
                }
            } else {
                System.out.println("DEBUG: Booking creation failed");
                conn.rollback();
                response.sendRedirect("cabins?action=book&id=" + booking.getCabinId() + "&error=booking_failed");
                return;
            }

        } catch (NumberFormatException e) {
            System.err.println("Number format error: " + e.getMessage());
            try { conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            response.sendRedirect("cabins?error=invalid_data");
        } catch (DateTimeParseException e) {
            System.err.println("Date parsing error: " + e.getMessage());
            try { conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            response.sendRedirect("cabins?error=invalid_date_format");
        } catch (Exception e) {
            System.err.println("Unexpected error in createBooking: " + e.getMessage());
            e.printStackTrace();
            try { conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            response.sendRedirect("cabins?error=booking_error");
        }
    }

    private void listBookings(HttpServletRequest request, HttpServletResponse response,
                              BookingService bookingService) throws ServletException, IOException {

        System.out.println("DEBUG: Listing bookings...");

        try {
            String userIdParam = request.getParameter("userId");
            List<Booking> bookings;

            if (userIdParam != null && !userIdParam.isEmpty()) {
                int userId = Integer.parseInt(userIdParam);
                bookings = bookingService.getUserBookings(userId);
                System.out.println("DEBUG: Retrieved " + bookings.size() + " bookings for user " + userId);
            } else {
                bookings = bookingService.getAllBookings();
                System.out.println("DEBUG: Retrieved " + bookings.size() + " total bookings");
            }

            // Enhance bookings with cabin information
            for (Booking booking : bookings) {
                try {
                    Cabin cabin = cabinService.getCabinById(booking.getCabinId());
                    if (cabin != null) {
                        request.setAttribute("cabin_" + booking.getCabinId(), cabin);
                    }
                } catch (Exception e) {
                    System.err.println("Error fetching cabin for booking " + booking.getId() + ": " + e.getMessage());
                }
            }

            request.setAttribute("bookings", bookings);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/booking-list.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            System.err.println("Error in listBookings: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error loading bookings: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void viewBooking(HttpServletRequest request, HttpServletResponse response,
                             BookingService bookingService) throws ServletException, IOException {

        try {
            String idParam = request.getParameter("id");
            System.out.println("DEBUG: Viewing booking with ID: " + idParam);

            if (idParam == null || idParam.isEmpty()) {
                response.sendRedirect("booking?action=list&error=missing_id");
                return;
            }

            int bookingId = Integer.parseInt(idParam);
            Booking booking = bookingService.getBookingById(bookingId);

            if (booking != null) {
                System.out.println("DEBUG: Booking found - ID: " + booking.getId() +
                        ", Status: " + booking.getStatus());

                // Get associated cabin details
                Cabin cabin = cabinService.getCabinById(booking.getCabinId());

                request.setAttribute("booking", booking);
                request.setAttribute("cabin", cabin);

                // Calculate duration for display
                if (booking.getStartDate() != null && booking.getEndDate() != null) {
                    Duration duration = Duration.between(booking.getStartDate(), booking.getEndDate());
                    request.setAttribute("duration", duration.toHours());
                }

                RequestDispatcher dispatcher = request.getRequestDispatcher("/booking-details.jsp");
                dispatcher.forward(request, response);
            } else {
                System.out.println("DEBUG: Booking not found for ID: " + bookingId);
                response.sendRedirect("booking?action=list&error=notfound");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid booking ID format");
            response.sendRedirect("booking?action=list&error=invalid_id");
        } catch (Exception e) {
            System.err.println("Error in viewBooking: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("booking?action=list&error=view_error");
        }
    }

    private void showBookingForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("DEBUG: Showing booking form...");

        String cabinIdParam = request.getParameter("cabinId");
        System.out.println("DEBUG: Cabin ID parameter: " + cabinIdParam);

        if (cabinIdParam != null && !cabinIdParam.isEmpty()) {
            try {
                int cabinId = Integer.parseInt(cabinIdParam);
                Cabin cabin = cabinService.getCabinById(cabinId);

                if (cabin != null) {
                    System.out.println("DEBUG: Cabin found for booking form - Name: " + cabin.getName() +
                            ", Capacity: " + cabin.getCapacity() +
                            ", Rate: " + cabin.getHourlyRate());
                    request.setAttribute("cabin", cabin);
                } else {
                    System.out.println("DEBUG: Cabin not found for ID: " + cabinId);
                    request.setAttribute("error", "Cabin not found");
                }
            } catch (NumberFormatException e) {
                System.err.println("DEBUG: Invalid cabin ID format: " + cabinIdParam);
                request.setAttribute("error", "Invalid cabin ID");
            } catch (Exception e) {
                System.err.println("DEBUG: Error getting cabin: " + e.getMessage());
                request.setAttribute("error", "Error loading cabin details");
            }
        } else {
            System.out.println("DEBUG: No cabin ID provided for booking form");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/book-cabin.jsp");
        dispatcher.forward(request, response);
    }

    private void editBooking(HttpServletRequest request, HttpServletResponse response,
                             BookingService bookingService) throws ServletException, IOException {

        try {
            String idParam = request.getParameter("id");
            System.out.println("DEBUG: Editing booking with ID: " + idParam);

            if (idParam == null || idParam.isEmpty()) {
                response.sendRedirect("booking?action=list&error=missing_id");
                return;
            }

            int bookingId = Integer.parseInt(idParam);
            Booking booking = bookingService.getBookingById(bookingId);

            if (booking != null) {
                // Get cabin details for the edit form
                Cabin cabin = cabinService.getCabinById(booking.getCabinId());

                System.out.println("DEBUG: Booking and cabin loaded for editing");

                request.setAttribute("booking", booking);
                request.setAttribute("cabin", cabin);

                RequestDispatcher dispatcher = request.getRequestDispatcher("/booking-form.jsp");
                dispatcher.forward(request, response);
            } else {
                System.out.println("DEBUG: Booking not found for editing: " + bookingId);
                response.sendRedirect("booking?action=list&error=notfound");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid booking ID for editing");
            response.sendRedirect("booking?action=list&error=invalid_id");
        } catch (Exception e) {
            System.err.println("Error in editBooking: " + e.getMessage());
            response.sendRedirect("booking?action=list&error=edit_error");
        }
    }

    private void updateBooking(HttpServletRequest request, HttpServletResponse response,
                               BookingService bookingService) throws IOException {

        System.out.println("DEBUG: Updating booking...");

        try {
            Booking booking = new Booking();
            booking.setId(Integer.parseInt(request.getParameter("id")));
            booking.setUserId(Integer.parseInt(request.getParameter("userId")));
            booking.setCabinId(Integer.parseInt(request.getParameter("cabinId")));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime startDate = LocalDateTime.parse(request.getParameter("startDate"), formatter);
            LocalDateTime endDate = LocalDateTime.parse(request.getParameter("endDate"), formatter);

            booking.setStartDate(startDate);
            booking.setEndDate(endDate);
            booking.setGuests(Integer.parseInt(request.getParameter("guests")));

            // Recalculate amount if dates changed
            Cabin cabin = cabinService.getCabinById(booking.getCabinId());
            if (cabin != null) {
                Duration duration = Duration.between(startDate, endDate);
                long hours = Math.max(1, duration.toHours());
                double totalAmount = hours * cabin.getHourlyRate();
                booking.setAmount(totalAmount);
                System.out.println("DEBUG: Recalculated amount: " + totalAmount);
            } else {
                booking.setAmount(Double.parseDouble(request.getParameter("amount")));
            }

            String status = request.getParameter("status");
            String paymentStatus = request.getParameter("paymentStatus");

            booking.setStatus(status != null ? status : "pending");
            booking.setPaymentStatus(paymentStatus != null ? paymentStatus : "unpaid");

            // Check for conflicts excluding current booking
            if (bookingService.isBookingConflict(booking.getCabinId(), startDate, endDate, booking.getId())) {
                System.out.println("DEBUG: Update failed - time conflict");
                response.sendRedirect("booking?action=edit&id=" + booking.getId() + "&error=time_conflict");
                return;
            }

            boolean success = bookingService.updateBooking(booking);
            System.out.println("DEBUG: Booking update result: " + success);

            if (success) {
                response.sendRedirect("booking?action=view&id=" + booking.getId() + "&success=updated");
            } else {
                response.sendRedirect("booking?action=edit&id=" + booking.getId() + "&error=update_failed");
            }
        } catch (Exception e) {
            System.err.println("Error in updateBooking: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("booking?action=list&error=update_error");
        }
    }

    private void updateBookingStatus(HttpServletRequest request, HttpServletResponse response,
                                     BookingService bookingService) throws IOException {

        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            String status = request.getParameter("status");

            System.out.println("DEBUG: Updating booking status - ID: " + bookingId + ", Status: " + status);

            boolean success = false;
            switch (status) {
                case "confirmed":
                    success = bookingService.confirmBooking(bookingId);
                    break;
                case "cancelled":
                    success = bookingService.cancelBooking(bookingId);
                    break;
                case "completed":
                    success = bookingService.completeBooking(bookingId);
                    break;
                default:
                    System.out.println("DEBUG: Invalid status: " + status);
                    response.sendRedirect("booking?action=view&id=" + bookingId + "&error=invalid_status");
                    return;
            }

            System.out.println("DEBUG: Status update result: " + success);

            if (success) {
                response.sendRedirect("booking?action=view&id=" + bookingId + "&success=status_updated");
            } else {
                response.sendRedirect("booking?action=view&id=" + bookingId + "&error=status_update_failed");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid booking ID for status update");
            response.sendRedirect("booking?action=list&error=invalid_id");
        } catch (Exception e) {
            System.err.println("Error in updateBookingStatus: " + e.getMessage());
            response.sendRedirect("booking?action=list&error=status_update_error");
        }
    }

    private void updatePaymentStatus(HttpServletRequest request, HttpServletResponse response,
                                     BookingService bookingService) throws IOException {

        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            String paymentStatus = request.getParameter("paymentStatus");

            System.out.println("DEBUG: Updating payment status - ID: " + bookingId + ", Status: " + paymentStatus);

            boolean success = bookingService.updatePaymentStatus(bookingId, paymentStatus);
            System.out.println("DEBUG: Payment status update result: " + success);

            if (success) {
                response.sendRedirect("booking?action=view&id=" + bookingId + "&success=payment_updated");
            } else {
                response.sendRedirect("booking?action=view&id=" + bookingId + "&error=payment_update_failed");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid booking ID for payment status update");
            response.sendRedirect("booking?action=list&error=invalid_id");
        } catch (Exception e) {
            System.err.println("Error in updatePaymentStatus: " + e.getMessage());
            response.sendRedirect("booking?action=list&error=payment_update_error");
        }
    }

    private void cancelBooking(HttpServletRequest request, HttpServletResponse response,
                               BookingService bookingService) throws IOException {

        try {
            String idParam = request.getParameter("id");
            System.out.println("DEBUG: Cancelling booking with ID: " + idParam);

            if (idParam == null || idParam.isEmpty()) {
                response.sendRedirect("booking?action=list&error=missing_id");
                return;
            }

            int bookingId = Integer.parseInt(idParam);
            boolean success = bookingService.cancelBooking(bookingId);
            System.out.println("DEBUG: Booking cancellation result: " + success);

            if (success) {
                response.sendRedirect("booking?action=list&success=cancelled");
            } else {
                response.sendRedirect("booking?action=list&error=cancel_failed");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid booking ID for cancellation");
            response.sendRedirect("booking?action=list&error=invalid_id");
        } catch (Exception e) {
            System.err.println("Error in cancelBooking: " + e.getMessage());
            response.sendRedirect("booking?action=list&error=cancel_error");
        }
    }

    @Override
    public void destroy() {
        System.out.println("BookingServlet destroyed");
        super.destroy();
    }
}
