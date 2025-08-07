package com.yash.cabinbooking.service;

import com.yash.cabinbooking.model.Booking;
import com.yash.cabinbooking.model.Cabin;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    boolean createBooking(Booking booking);
    Booking getBookingById(int id);
    List<Booking> getAllBookings();
    List<Booking> getUserBookings(int userId);
    List<Booking> getCabinBookings(int cabinId);
    boolean updateBooking(Booking booking);
    boolean cancelBooking(int bookingId);
    boolean confirmBooking(int bookingId);
    boolean completeBooking(int bookingId);
    boolean updatePaymentStatus(int bookingId, String paymentStatus);
    boolean isBookingConflict(int cabinId, LocalDateTime startDate, LocalDateTime endDate, int excludeBookingId);
    double calculateBookingAmount(int cabinId, LocalDateTime startDate, LocalDateTime endDate);
    List<Booking> getBookingsByApprovalStatus(String approvalStatus);
    int countBookingsByApprovalStatus(String approvalStatus);
    int countBookingsApprovedThisMonth();
    double calculateMonthlyRevenue();
    // Add this new method to match the implementation
    Cabin getCabinForBooking(int cabinId);
    boolean updateApprovalStatus(int bookingId, String approvalStatus, Integer approvedBy, String adminRemarks, String rejectionReason);

}