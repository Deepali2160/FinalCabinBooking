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
                // Simulate refund logic
                boolean refundSuccess = true; // (for demo; integrate with gateway if needed)
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
    private PaymentResult processCardPayment(HttpServletRequest request, Booking booking) {
        String cardNumber = request.getParameter("cardNumber");
        String expiryDate = request.getParameter("expiryDate");
        String cvv = request.getParameter("cvv");
        String cardHolder = request.getParameter("cardHolder");

        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return new PaymentResult(false, null, "Card number is required");
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
        // Simulate payment gateway response (almost always success)
        String transactionId = "TXN" + System.currentTimeMillis();
        return new PaymentResult(true, transactionId, null);
    }

    private PaymentResult processUpiPayment(HttpServletRequest request, Booking booking) {
        String upiId = request.getParameter("upiId");
        if (upiId == null || !upiId.contains("@")) {
            return new PaymentResult(false, null, "Invalid UPI ID format");
        }
        String transactionId = "UPI" + System.currentTimeMillis();
        return new PaymentResult(true, transactionId, null);
    }

    private PaymentResult processNetBankingPayment(HttpServletRequest request, Booking booking) {
        String bank = request.getParameter("bank");
        if (bank == null || bank.trim().isEmpty()) {
            return new PaymentResult(false, null, "Please select a bank");
        }
        String transactionId = "NB" + System.currentTimeMillis();
        return new PaymentResult(true, transactionId, null);
    }

    private PaymentResult processWalletPayment(HttpServletRequest request, Booking booking) {
        String walletType = request.getParameter("walletType");
        if (walletType == null || walletType.trim().isEmpty()) {
            return new PaymentResult(false, null, "Please select a wallet");
        }
        String transactionId = "WAL" + System.currentTimeMillis();
        return new PaymentResult(true, transactionId, null);
    }

    private PaymentResult processPaymentByMethod(String paymentMethod, HttpServletRequest request, Booking booking) {
        System.out.println("DEBUG: Processing payment via method: " + paymentMethod);

        if (paymentMethod == null) {
            return new PaymentResult(false, null, "Payment method not specified");
        }
        try {
            Thread.sleep(1000); // Simulate processing time

            if ("card".equalsIgnoreCase(paymentMethod)) {
                return processCardPayment(request, booking);
            } else if ("upi".equalsIgnoreCase(paymentMethod)) {
                return processUpiPayment(request, booking);
            } else if ("netbanking".equalsIgnoreCase(paymentMethod)) {
                return processNetBankingPayment(request, booking);
            } else if ("wallet".equalsIgnoreCase(paymentMethod)) {
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

    private void verifyPayment(HttpServletRequest request, HttpServletResponse response, BookingService bookingService)
            throws ServletException, IOException {
        String transactionId = request.getParameter("transactionId");
        String bookingIdParam = request.getParameter("bookingId");

        System.out.println("DEBUG: Verifying payment - Booking: " + bookingIdParam +
                ", Transaction: " + transactionId);

        try {
            int bookingId = Integer.parseInt(bookingIdParam);
            // Simulate verification: suppose a transactionId with len > 10 is considered verified
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

    private void checkPaymentStatus(HttpServletRequest request, HttpServletResponse response, BookingService bookingService)
            throws ServletException, IOException {
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
            Booking booking = bookingService.getBookingById(bookingId);

            if (booking == null) {
                response.sendRedirect("booking?action=list&error=booking_not_found");
                return;
            }

            // *** Admin approval check ***
            if (!"approved".equalsIgnoreCase(booking.getApprovalStatus())) {
                System.out.println("DEBUG: Booking not approved yet (approvalStatus: " + booking.getApprovalStatus() + ")");
                response.sendRedirect("booking?action=view&id=" + bookingId + "&error=not_approved");
                return;
            }
            // *** End check ***

            if ("paid".equals(booking.getPaymentStatus())) {
                response.sendRedirect("success.jsp?bookingId=" + bookingId + "&transactionId=ALREADY_PAID");
                return;
            }

            Cabin cabin = cabinService.getCabinById(booking.getCabinId());
            long hours = java.time.Duration.between(booking.getStartDate(), booking.getEndDate()).toHours();
            request.setAttribute("booking", booking);
            request.setAttribute("cabin", cabin);
            request.setAttribute("duration", hours);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/payment.jsp");
            dispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("booking?action=list&error=invalid_booking_id");
        }
    }

    private void processPayment(HttpServletRequest request, HttpServletResponse response,
                                BookingService bookingService, Connection conn) throws ServletException, IOException {

        System.out.println("DEBUG: Processing payment...");

        try {
            String bookingIdParam = request.getParameter("bookingId");
            String amountParam = request.getParameter("amount");
            String paymentMethod = request.getParameter("paymentMethod");

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

            Booking booking = bookingService.getBookingById(bookingId);
            if (booking == null) {
                System.out.println("DEBUG: Booking not found for payment");
                conn.rollback();
                response.sendRedirect("booking?action=list&error=booking_not_found");
                return;
            }

            if ("paid".equals(booking.getPaymentStatus())) {
                System.out.println("DEBUG: Payment already processed for booking: " + bookingId);
                conn.rollback();
                response.sendRedirect("success.jsp?bookingId=" + bookingId + "&transactionId=ALREADY_PAID");
                return;
            }

            // *** Admin approval check before payment ***
            if (booking.getApprovalStatus() == null || !"approved".equalsIgnoreCase(booking.getApprovalStatus())) {
                System.out.println("DEBUG: Payment attempt for booking not approved by admin");
                conn.rollback();
                response.sendRedirect("payment?action=show&bookingId=" + bookingId + "&error=not_approved_for_payment");
                return;
            }
            // *** End check ***

            if (Math.abs(booking.getAmount() - amount) > 0.01) {
                System.out.println("DEBUG: Amount mismatch - Expected: " + booking.getAmount() + ", Got: " + amount);
                conn.rollback();
                response.sendRedirect("payment?action=show&bookingId=" + bookingId + "&error=amount_mismatch");
                return;
            }

            PaymentResult result = processPaymentByMethod(paymentMethod, request, booking);

            if (result.isSuccess()) {
                boolean paymentUpdated = bookingService.updatePaymentStatus(bookingId, "paid");
                boolean bookingConfirmed = bookingService.confirmBooking(bookingId);

                if (paymentUpdated && bookingConfirmed) {
                    conn.commit();

                    HttpSession session = request.getSession();
                    session.setAttribute("paymentResult", result);
                    session.setAttribute("paymentBooking", booking);
                    session.setAttribute("paymentTimestamp", LocalDateTime.now());
                    session.setAttribute("paymentMethod", paymentMethod);

                    response.sendRedirect("success.jsp?bookingId=" + bookingId +
                            "&transactionId=" + result.getTransactionId() +
                            "&amount=" + booking.getAmount() +
                            "&status=success");
                } else {
                    conn.rollback();
                    response.sendRedirect("payment?action=show&bookingId=" + bookingId + "&error=status_update_failed");
                }
            } else {
                conn.rollback();
                String encodedError = result.getErrorMessage().replace(" ", "_").toLowerCase();
                response.sendRedirect("payment?action=show&bookingId=" + bookingId + "&error=" + encodedError);
            }

        } catch (Exception e) {
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

    // ... (Rest of payment processing methods stay unchanged)

    // Helper class for payment result
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
