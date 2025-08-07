package com.yash.cabinbooking.serviceimpl;

import com.yash.cabinbooking.dao.BookingDao;
import com.yash.cabinbooking.daoimpl.BookingDaoImpl;
import com.yash.cabinbooking.model.Booking;
import com.yash.cabinbooking.model.Cabin;
import com.yash.cabinbooking.service.BookingService;
import com.yash.cabinbooking.service.CabinService;
import com.yash.cabinbooking.serviceimpl.CabinServiceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class BookingServiceImpl implements BookingService {
    private BookingDao bookingDAO;
    private CabinService cabinService;
    private Connection conn;

    public BookingServiceImpl(Connection connection) {
        this.conn = connection;
        this.bookingDAO = new BookingDaoImpl(connection);
        this.cabinService = new CabinServiceImpl(); // Agar CabinServiceImpl mein connection constructor chahiye to update kar lena
    }

    @Override
    public boolean createBooking(Booking booking) {
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            return false;
        }

        if (isBookingConflict(booking.getCabinId(), booking.getStartDate(), booking.getEndDate(), 0)) {
            return false;
        }

        // PROFESSIONAL FLOW: Set approval status, payment status and booking status
        booking.setStatus("pending_approval");
        booking.setApprovalStatus("pending_approval");
        booking.setPaymentStatus("unpaid");
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        return bookingDAO.addBooking(booking);
    }
    @Override
    public List<Booking> getBookingsByApprovalStatus(String approvalStatus) {
        return bookingDAO.getBookingsByApprovalStatus(approvalStatus);
    }

    @Override
    public Booking getBookingById(int id) {
        return bookingDAO.getBookingById(id);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingDAO.getAllBookings();
    }

    @Override
    public List<Booking> getUserBookings(int userId) {
        return bookingDAO.getBookingsByUserId(userId);
    }

    @Override
    public List<Booking> getCabinBookings(int cabinId) {
        return bookingDAO.getBookingsByCabinId(cabinId);
    }

    @Override
    public int countBookingsByApprovalStatus(String approvalStatus) {
        return bookingDAO.countBookingsByApprovalStatus(approvalStatus);
    }


    @Override
    public int countBookingsApprovedThisMonth() {
        String sql = "SELECT COUNT(*) FROM bookings WHERE approval_status = 'approved' " +
                "AND MONTH(approved_at) = MONTH(CURRENT_DATE()) " +
                "AND YEAR(approved_at) = YEAR(CURRENT_DATE())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double calculateMonthlyRevenue() {
        String sql = "SELECT SUM(amount) FROM bookings WHERE approval_status = 'approved' " +
                "AND payment_status = 'paid' " +
                "AND MONTH(approved_at) = MONTH(CURRENT_DATE()) " +
                "AND YEAR(approved_at) = YEAR(CURRENT_DATE())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public boolean updateBooking(Booking booking) {
        // Check for conflicts excluding current booking
        if (isBookingConflict(booking.getCabinId(), booking.getStartDate(), booking.getEndDate(), booking.getId())) {
            return false;
        }
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingDAO.updateBooking(booking);
    }

    @Override
    public boolean cancelBooking(int bookingId) {
        return bookingDAO.updateBookingStatus(bookingId, "cancelled");
    }

    @Override
    public boolean confirmBooking(int bookingId) {
        return bookingDAO.updateBookingStatus(bookingId, "confirmed");
    }

    @Override
    public boolean completeBooking(int bookingId) {
        return bookingDAO.updateBookingStatus(bookingId, "completed");
    }

    @Override
    public boolean updatePaymentStatus(int bookingId, String paymentStatus) {
        return bookingDAO.updatePaymentStatus(bookingId, paymentStatus);
    }

    @Override
    public boolean isBookingConflict(int cabinId, LocalDateTime startDate, LocalDateTime endDate, int excludeBookingId) {
        List<Booking> existingBookings = bookingDAO.getBookingsByCabinId(cabinId);

        for (Booking existingBooking : existingBookings) {
            if (existingBooking.getId() == excludeBookingId) {
                continue;
            }
            if ("cancelled".equals(existingBooking.getStatus())) {
                continue;
            }
            if (startDate.isBefore(existingBooking.getEndDate()) && endDate.isAfter(existingBooking.getStartDate())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double calculateBookingAmount(int cabinId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            Cabin cabin = cabinService.getCabinById(cabinId);
            if (cabin != null) {
                Duration duration = Duration.between(startDate, endDate);
                long hours = Math.max(1, duration.toHours());
                return hours * cabin.getHourlyRate();
            }
        } catch (Exception e) {
            System.err.println("Error calculating booking amount: " + e.getMessage());
        }
        // Default fallback
        Duration duration = Duration.between(startDate, endDate);
        long hours = Math.max(1, duration.toHours());
        return hours * 100.0; // koi default rate
    }





    public List<Booking> getBookingsWithDetails() {
        return bookingDAO.getBookingsWithDetails();
    }

    @Override
    public boolean updateApprovalStatus(int bookingId, String approvalStatus, Integer approvedBy, String adminRemarks, String rejectionReason) {
        return bookingDAO.updateApprovalStatus(bookingId, approvalStatus, approvedBy, adminRemarks, rejectionReason);
    }

    public Cabin getCabinForBooking(int cabinId) {
        try {
            return cabinService.getCabinById(cabinId);
        } catch (Exception e) {
            System.err.println("Error fetching cabin details: " + e.getMessage());
            return null;
        }
    }
}
