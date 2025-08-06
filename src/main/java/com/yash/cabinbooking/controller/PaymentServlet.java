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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CabinService cabinService;

    @Override
    public void init() throws ServletException {
        cabinService = new CabinServiceImpl();
        System.out.println("PaymentServlet initialized successfully");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "show";

        System.out.println("DEBUG: PaymentServlet doGet - Action: " + action);

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            BookingService bookingService = new BookingServiceImpl(conn);

            switch (action) {
                case "show":
                    showPaymentPage(request, response, bookingService);
                    break;
                case "status":
                    checkPaymentStatus(request, response, bookingService);
                    break;
                default:
                    response.sendRedirect("booking?action=list");
            }
            conn.commit();
        } catch (SQLException e) {
            System.err.println("Database connection error in PaymentServlet: " + e.getMessage());
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
                    conn.setAutoCommit(true);
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
        System.out.println("DEBUG: PaymentServlet doPost - Action: " + action);

        // Debug: Print all request parameters
        System.out.println("DEBUG: All request parameters:");
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + Arrays.toString(entry.getValue()));
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            BookingService bookingService = new BookingServiceImpl(conn);

            if (action == null) {
                System.out.println("DEBUG: Action parameter is null, defaulting to list");
                response.sendRedirect("booking?action=list");
                return;
            }

            switch (action) {
                case "process":
                    processPayment(request, response, bookingService, conn);
                    break;
                case "verify":
                    verifyPayment(request, response, bookingService);
                    break;
                case "refund":
                    processRefund(request, response, bookingService);
                    break;
                default:
                    System.out.println("DEBUG: Unknown action: " + action);
                    response.sendRedirect("booking?action=list");
            }

        } catch (SQLException e) {
            System.err.println("Database connection error in PaymentServlet POST: " + e.getMessage());
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
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing database connection: " + e.getMessage());
                }
            }
        }
    }

    private void showPaymentPage(HttpServletRequest request, HttpServletResponse response,
                                 BookingService bookingService) throws ServletException, IOException {

        String bookingIdParam = request.getParameter("bookingId");
        System.out.println("DEBUG: PaymentServlet - bookingId parameter: " + bookingIdParam);

        if (bookingIdParam == null || bookingIdParam.isEmpty()) {
            System.out.println("DEBUG: Missing booking ID, redirecting to booking list");
            response.sendRedirect("booking?action=list&error=missing_booking_id");
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdParam);
            System.out.println("DEBUG: Looking for booking with ID: " + bookingId);

            Booking booking = bookingService.getBookingById(bookingId);
            System.out.println("DEBUG: Retrieved booking: " + (booking != null ? "Found" : "NULL"));

            if (booking != null) {
                System.out.println("DEBUG: Booking details - ID: " + booking.getId() +
                        ", Status: " + booking.getStatus() +
                        ", Amount: " + booking.getAmount() +
                        ", Payment Status: " + booking.getPaymentStatus());
            }

            if (booking == null) {
                System.out.println("DEBUG: Booking not found, redirecting to booking list");
                response.sendRedirect("booking?action=list&error=booking_not_found");
                return;
            }

            // ✅ Allow payment for pending bookings only
            if (!"pending".equals(booking.getStatus())) {
                System.out.println("DEBUG: Booking not pending (Status: " + booking.getStatus() + "), redirecting to booking view");
                response.sendRedirect("booking?action=view&id=" + bookingId + "&error=booking_not_pending");
                return;
            }

            // ✅ Check if payment already completed
            if ("paid".equals(booking.getPaymentStatus())) {
                System.out.println("DEBUG: Payment already completed, redirecting to success page");
                response.sendRedirect("success.jsp?bookingId=" + bookingId + "&transactionId=ALREADY_PAID");
                return;
            }

            // Get cabin details
            Cabin cabin = null;
            try {
                cabin = cabinService.getCabinById(booking.getCabinId());
                System.out.println("DEBUG: Cabin details retrieved: " + (cabin != null ? cabin.getName() : "NULL"));
            } catch (Exception e) {
                System.err.println("Error getting cabin details: " + e.getMessage());
            }

            // Calculate additional details for display
            long hours = java.time.Duration.between(booking.getStartDate(), booking.getEndDate()).toHours();

            // Set all attributes
            request.setAttribute("booking", booking);
            request.setAttribute("cabin", cabin);
            request.setAttribute("duration", hours);

            System.out.println("DEBUG: Forwarding to payment.jsp with booking ID: " + booking.getId() +
                    ", cabin: " + (cabin != null ? cabin.getName() : "NULL") +
                    ", amount: " + booking.getAmount());

            RequestDispatcher dispatcher = request.getRequestDispatcher("/payment.jsp");
            dispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            System.err.println("Invalid booking ID format: " + bookingIdParam);
            response.sendRedirect("booking?action=list&error=invalid_booking_id");
        }
    }

    private void processPayment(HttpServletRequest request, HttpServletResponse response,
                                BookingService bookingService, Connection conn) throws ServletException, IOException {

        System.out.println("DEBUG: Processing payment...");

        try {
            // Extract and validate all parameters first
            String bookingIdParam = request.getParameter("bookingId");
            String amountParam = request.getParameter("amount");
            String paymentMethod = request.getParameter("paymentMethod");

            System.out.println("DEBUG: Raw parameters - bookingId: " + bookingIdParam +
                    ", amount: " + amountParam + ", paymentMethod: " + paymentMethod);

            // Validate required parameters
            if (bookingIdParam == null || bookingIdParam.isEmpty()) {
                System.out.println("DEBUG: Missing booking ID parameter");
                conn.rollback();
                response.sendRedirect("booking?action=list&error=missing_booking_id");
                return;
            }

            if (amountParam == null || amountParam.isEmpty()) {
                System.out.println("DEBUG: Missing amount parameter");
                conn.rollback();
                response.sendRedirect("booking?action=list&error=missing_amount");
                return;
            }

            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                System.out.println("DEBUG: Missing payment method parameter");
                conn.rollback();
                response.sendRedirect("payment?action=show&bookingId=" + bookingIdParam + "&error=missing_payment_method");
                return;
            }

            // Parse parameters safely
            int bookingId;
            double amount;

            try {
                bookingId = Integer.parseInt(bookingIdParam);
            } catch (NumberFormatException e) {
                System.err.println("Invalid booking ID format: " + bookingIdParam);
                conn.rollback();
                response.sendRedirect("booking?action=list&error=invalid_booking_id");
                return;
            }

            try {
                amount = Double.parseDouble(amountParam);
            } catch (NumberFormatException e) {
                System.err.println("Invalid amount format: " + amountParam);
                conn.rollback();
                response.sendRedirect("payment?action=show&bookingId=" + bookingId + "&error=invalid_amount");
                return;
            }

            System.out.println("DEBUG: Payment details - Booking: " + bookingId +
                    ", Amount: " + amount + ", Method: " + paymentMethod);

            // Get booking details
            Booking booking = bookingService.getBookingById(bookingId);
            if (booking == null) {
                System.out.println("DEBUG: Booking not found for payment");
                conn.rollback();
                response.sendRedirect("booking?action=list&error=booking_not_found");
                return;
            }

            // ✅ Check if payment already processed
            if ("paid".equals(booking.getPaymentStatus())) {
                System.out.println("DEBUG: Payment already processed for booking: " + bookingId);
                conn.rollback();
                response.sendRedirect("success.jsp?bookingId=" + bookingId + "&transactionId=ALREADY_PAID");
                return;
            }

            // Validate amount
            if (Math.abs(booking.getAmount() - amount) > 0.01) {
                System.out.println("DEBUG: Amount mismatch - Expected: " + booking.getAmount() + ", Got: " + amount);
                conn.rollback();
                response.sendRedirect("payment?action=show&bookingId=" + bookingId + "&error=amount_mismatch");
                return;
            }

            // Process payment based on method
            PaymentResult result = processPaymentByMethod(paymentMethod, request, booking);
            System.out.println("DEBUG: Payment result - Success: " + result.isSuccess() +
                    ", Transaction: " + result.getTransactionId() +
                    ", Error: " + result.getErrorMessage());

            if (result.isSuccess()) {
                // ✅ Update booking status and payment status with proper error handling
                boolean paymentUpdated = bookingService.updatePaymentStatus(bookingId, "paid");
                boolean bookingConfirmed = bookingService.confirmBooking(bookingId);

                System.out.println("DEBUG: Status updates - Payment: " + paymentUpdated +
                        ", Booking: " + bookingConfirmed);

                if (paymentUpdated && bookingConfirmed) {
                    // Commit the transaction
                    conn.commit();
                    System.out.println("DEBUG: Payment transaction committed successfully");

                    // ✅ Store comprehensive payment details in session for success page
                    HttpSession session = request.getSession();
                    session.setAttribute("paymentResult", result);
                    session.setAttribute("paymentBooking", booking);
                    session.setAttribute("paymentTimestamp", LocalDateTime.now());
                    session.setAttribute("paymentMethod", paymentMethod);

                    // ✅ Redirect to success page with all necessary parameters
                    response.sendRedirect("success.jsp?bookingId=" + bookingId +
                            "&transactionId=" + result.getTransactionId() +
                            "&amount=" + booking.getAmount() +
                            "&status=success");
                } else {
                    System.out.println("DEBUG: Failed to update booking status - Payment: " + paymentUpdated + ", Booking: " + bookingConfirmed);
                    conn.rollback();
                    response.sendRedirect("payment?action=show&bookingId=" + bookingId + "&error=status_update_failed");
                }
            } else {
                System.out.println("DEBUG: Payment failed - " + result.getErrorMessage());
                conn.rollback();
                // URL encode error message to handle special characters
                String encodedError = result.getErrorMessage().replace(" ", "_").toLowerCase();
                response.sendRedirect("payment?action=show&bookingId=" + bookingId + "&error=" + encodedError);
            }

        } catch (Exception e) {
            System.err.println("Unexpected error in payment processing: " + e.getMessage());
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }

            String bookingIdParam = request.getParameter("bookingId");
            if (bookingIdParam != null) {
                response.sendRedirect("payment?action=show&bookingId=" + bookingIdParam + "&error=payment_failed");
            } else {
                response.sendRedirect("booking?action=list&error=payment_error");
            }
        }
    }

    private PaymentResult processPaymentByMethod(String paymentMethod, HttpServletRequest request, Booking booking) {
        System.out.println("DEBUG: Processing payment via method: " + paymentMethod);

        if (paymentMethod == null) {
            return new PaymentResult(false, null, "Payment method not specified");
        }

        try {
            Thread.sleep(1000); // Simulate processing time

            if ("card".equals(paymentMethod)) {
                return processCardPayment(request, booking);
            } else if ("upi".equals(paymentMethod)) {
                return processUpiPayment(request, booking);
            } else if ("netbanking".equals(paymentMethod)) {
                return processNetBankingPayment(request, booking);
            } else if ("wallet".equals(paymentMethod)) {
                return processWalletPayment(request, booking);
            } else {
                System.out.println("DEBUG: Unknown payment method: " + paymentMethod);
                return new PaymentResult(false, null, "Invalid payment method");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new PaymentResult(false, null, "Payment processing interrupted");
        }
    }

    private PaymentResult processCardPayment(HttpServletRequest request, Booking booking) {
        String cardNumber = request.getParameter("cardNumber");
        String expiryDate = request.getParameter("expiryDate");
        String cvv = request.getParameter("cvv");
        String cardHolder = request.getParameter("cardHolder");

        System.out.println("DEBUG: Processing card payment for card ending: " +
                (cardNumber != null && cardNumber.length() >= 4 ?
                        cardNumber.replace(" ", "").substring(cardNumber.replace(" ", "").length() - 4) : "NULL"));

        // Enhanced validation
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return new PaymentResult(false, null, "Card number is required");
        }

        if (cardNumber.replace(" ", "").length() < 16) {
            return new PaymentResult(false, null, "Invalid card number");
        }

        if (expiryDate == null || expiryDate.length() != 5) {
            return new PaymentResult(false, null, "Invalid expiry date");
        }

        if (cvv == null || cvv.length() < 3) {
            return new PaymentResult(false, null, "Invalid CVV");
        }

        if (cardHolder == null || cardHolder.trim().isEmpty()) {
            return new PaymentResult(false, null, "Cardholder name is required");
        }

        // ✅ Enhanced validation for expiry date
        try {
            String[] parts = expiryDate.split("/");
            if (parts.length != 2) {
                return new PaymentResult(false, null, "Invalid expiry date format");
            }
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]) + 2000;

            if (month < 1 || month > 12) {
                return new PaymentResult(false, null, "Invalid expiry month");
            }

            // Check if card is not expired
            LocalDateTime now = LocalDateTime.now();
            if (year < now.getYear() || (year == now.getYear() && month < now.getMonthValue())) {
                return new PaymentResult(false, null, "Card has expired");
            }
        } catch (NumberFormatException e) {
            return new PaymentResult(false, null, "Invalid expiry date format");
        }

        // Simulate payment gateway response
        Random random = new Random();
        if (random.nextInt(10) < 9) { // 90% success rate
            String transactionId = "TXN" + System.currentTimeMillis();
            System.out.println("DEBUG: Card payment successful - Transaction: " + transactionId);
            return new PaymentResult(true, transactionId, null);
        } else {
            System.out.println("DEBUG: Card payment declined");
            return new PaymentResult(false, null, "Card payment declined");
        }
    }

    private PaymentResult processUpiPayment(HttpServletRequest request, Booking booking) {
        String upiId = request.getParameter("upiId");

        System.out.println("DEBUG: Processing UPI payment for ID: " + upiId);

        if (upiId == null || upiId.trim().isEmpty()) {
            return new PaymentResult(false, null, "UPI ID is required");
        }

        if (!upiId.contains("@")) {
            return new PaymentResult(false, null, "Invalid UPI ID format");
        }

        // ✅ Enhanced UPI validation
        String[] parts = upiId.split("@");
        if (parts.length != 2 || parts[0].length() < 3 || parts[1].length() < 3) {
            return new PaymentResult(false, null, "Invalid UPI ID format");
        }

        // Simulate UPI payment
        Random random = new Random();
        if (random.nextInt(10) < 9) { // 90% success rate
            String transactionId = "UPI" + System.currentTimeMillis();
            System.out.println("DEBUG: UPI payment successful - Transaction: " + transactionId);
            return new PaymentResult(true, transactionId, null);
        } else {
            System.out.println("DEBUG: UPI payment failed");
            return new PaymentResult(false, null, "UPI payment failed");
        }
    }

    private PaymentResult processNetBankingPayment(HttpServletRequest request, Booking booking) {
        String bank = request.getParameter("bank");
        System.out.println("DEBUG: Processing net banking payment via bank: " + bank);

        if (bank == null || bank.trim().isEmpty()) {
            return new PaymentResult(false, null, "Please select a bank");
        }

        // Simulate net banking payment
        Random random = new Random();
        if (random.nextInt(10) < 8) { // 80% success rate
            String transactionId = "NB" + System.currentTimeMillis();
            System.out.println("DEBUG: Net banking payment successful - Transaction: " + transactionId);
            return new PaymentResult(true, transactionId, null);
        } else {
            System.out.println("DEBUG: Net banking payment failed");
            return new PaymentResult(false, null, "Net banking payment failed");
        }
    }

    private PaymentResult processWalletPayment(HttpServletRequest request, Booking booking) {
        String walletType = request.getParameter("walletType");
        System.out.println("DEBUG: Processing wallet payment via: " + walletType);

        if (walletType == null || walletType.trim().isEmpty()) {
            return new PaymentResult(false, null, "Please select a wallet");
        }

        // Simulate wallet payment
        Random random = new Random();
        if (random.nextInt(10) < 9) { // 90% success rate
            String transactionId = "WAL" + System.currentTimeMillis();
            System.out.println("DEBUG: Wallet payment successful - Transaction: " + transactionId);
            return new PaymentResult(true, transactionId, null);
        } else {
            System.out.println("DEBUG: Wallet payment failed");
            return new PaymentResult(false, null, "Wallet payment failed");
        }
    }

    private void checkPaymentStatus(HttpServletRequest request, HttpServletResponse response,
                                    BookingService bookingService) throws ServletException, IOException {

        String transactionId = request.getParameter("transactionId");
        String bookingIdParam = request.getParameter("bookingId");

        System.out.println("DEBUG: Checking payment status for booking: " + bookingIdParam +
                ", transaction: " + transactionId);

        if (bookingIdParam == null) {
            response.sendRedirect("booking?action=list&error=missing_booking_id");
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdParam);
            Booking booking = bookingService.getBookingById(bookingId);

            if (booking != null && "paid".equals(booking.getPaymentStatus())) {
                System.out.println("DEBUG: Payment confirmed, redirecting to success page");
                response.sendRedirect("success.jsp?bookingId=" + bookingId + "&transactionId=" + transactionId);
            } else {
                System.out.println("DEBUG: Payment not confirmed");
                response.sendRedirect("payment?action=show&bookingId=" + bookingId + "&error=payment_not_confirmed");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("booking?action=list&error=invalid_booking_id");
        }
    }

    private void verifyPayment(HttpServletRequest request, HttpServletResponse response,
                               BookingService bookingService) throws ServletException, IOException {

        String transactionId = request.getParameter("transactionId");
        String bookingIdParam = request.getParameter("bookingId");

        System.out.println("DEBUG: Verifying payment - Booking: " + bookingIdParam +
                ", Transaction: " + transactionId);

        try {
            int bookingId = Integer.parseInt(bookingIdParam);
            boolean isVerified = transactionId != null && transactionId.length() > 10;

            if (isVerified) {
                bookingService.updatePaymentStatus(bookingId, "paid");
                bookingService.confirmBooking(bookingId);
                response.getWriter().write("{\"status\":\"success\",\"message\":\"Payment verified\"}");
                System.out.println("DEBUG: Payment verification successful");
            } else {
                response.getWriter().write("{\"status\":\"failed\",\"message\":\"Payment verification failed\"}");
                System.out.println("DEBUG: Payment verification failed");
            }

            response.setContentType("application/json");
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid booking ID\"}");
            response.setContentType("application/json");
        }
    }

    private void processRefund(HttpServletRequest request, HttpServletResponse response,
                               BookingService bookingService) throws ServletException, IOException {

        String bookingIdParam = request.getParameter("bookingId");
        System.out.println("DEBUG: Processing refund for booking: " + bookingIdParam);

        if (bookingIdParam == null) {
            response.sendRedirect("booking?action=list&error=missing_booking_id");
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdParam);
            Booking booking = bookingService.getBookingById(bookingId);

            if (booking != null && "paid".equals(booking.getPaymentStatus())) {
                boolean refundSuccess = true; // Simulate refund
                System.out.println("DEBUG: Refund simulation successful");

                if (refundSuccess) {
                    bookingService.updatePaymentStatus(bookingId, "refunded");
                    bookingService.cancelBooking(bookingId);
                    response.sendRedirect("booking?action=view&id=" + bookingId + "&success=refunded");
                } else {
                    response.sendRedirect("booking?action=view&id=" + bookingId + "&error=refund_failed");
                }
            } else {
                System.out.println("DEBUG: Invalid refund request for booking: " + bookingId);
                response.sendRedirect("booking?action=view&id=" + bookingId + "&error=invalid_refund_request");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("booking?action=list&error=invalid_booking_id");
        }
    }

    // Inner class for payment result
    private static class PaymentResult {
        private boolean success;
        private String transactionId;
        private String errorMessage;

        public PaymentResult(boolean success, String transactionId, String errorMessage) {
            this.success = success;
            this.transactionId = transactionId;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    @Override
    public void destroy() {
        System.out.println("PaymentServlet destroyed");
        super.destroy();
    }
}
