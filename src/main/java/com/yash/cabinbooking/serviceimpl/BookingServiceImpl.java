package com.yash.cabinbooking.serviceimpl;

import com.yash.cabinbooking.dao.BookingDao;
import com.yash.cabinbooking.daoimpl.BookingDaoImpl;
import com.yash.cabinbooking.model.Booking;
import com.yash.cabinbooking.model.Cabin;
import com.yash.cabinbooking.service.BookingService;
import com.yash.cabinbooking.service.CabinService;
import com.yash.cabinbooking.serviceimpl.CabinServiceImpl;
import java.sql.Connection;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class BookingServiceImpl implements BookingService {
    private BookingDao bookingDAO;
    private CabinService cabinService; // Add this

    public BookingServiceImpl(Connection connection) {
        this.bookingDAO = new BookingDaoImpl(connection);
        this.cabinService = new CabinServiceImpl(); // Initialize cabin service
    }

    @Override
    public boolean createBooking(Booking booking) {
        // Validate booking dates
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            return false;
        }

        // Check for conflicts
        if (isBookingConflict(booking.getCabinId(), booking.getStartDate(), booking.getEndDate(), 0)) {
            return false;
        }

        // Set default status and timestamps
        booking.setStatus("pending");
        booking.setPaymentStatus("unpaid");
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        return bookingDAO.addBooking(booking);
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
            // Skip if this is the same booking we're updating
            if (existingBooking.getId() == excludeBookingId) {
                continue;
            }

            // Skip cancelled bookings
            if ("cancelled".equals(existingBooking.getStatus())) {
                continue;
            }

            // Check for overlap
            if (startDate.isBefore(existingBooking.getEndDate()) &&
                    endDate.isAfter(existingBooking.getStartDate())) {
                return true; // Conflict found
            }
        }
        return false; // No conflict
    }

    @Override
    public double calculateBookingAmount(int cabinId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            // Get cabin details to fetch actual hourly rate
            Cabin cabin = cabinService.getCabinById(cabinId);

            if (cabin != null) {
                Duration duration = Duration.between(startDate, endDate);
                long hours = Math.max(1, duration.toHours()); // Minimum 1 hour billing
                return hours * cabin.getHourlyRate();
            } else {
                // Fallback to default rate if cabin not found
                Duration duration = Duration.between(startDate, endDate);
                long hours = Math.max(1, duration.toHours());
                double defaultHourlyRate = 100.0; // Default rate
                return hours * defaultHourlyRate;
            }
        } catch (Exception e) {
            // Log the error and return default calculation
            System.err.println("Error calculating booking amount: " + e.getMessage());
            Duration duration = Duration.between(startDate, endDate);
            long hours = Math.max(1, duration.toHours());
            return hours * 100.0; // Default rate
        }
    }

    // Add this new method to get cabin details for bookings
    public Cabin getCabinForBooking(int cabinId) {
        try {
            return cabinService.getCabinById(cabinId);
        } catch (Exception e) {
            System.err.println("Error fetching cabin details: " + e.getMessage());
            return null;
        }
    }
}
