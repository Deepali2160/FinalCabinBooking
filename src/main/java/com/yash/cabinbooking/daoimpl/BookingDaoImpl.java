package com.yash.cabinbooking.daoimpl;

import com.yash.cabinbooking.dao.BookingDao;
import com.yash.cabinbooking.model.Booking;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDaoImpl implements BookingDao {
    private Connection connection;

    public BookingDaoImpl(Connection connection) {
        this.connection = connection;
        System.out.println("DEBUG: BookingDaoImpl initialized with connection: " + (connection != null));
    }

    @Override
    public boolean addBooking(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, cabin_id, start_date, end_date, guests, amount, status, payment_status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        System.out.println("DEBUG: Adding booking - User: " + booking.getUserId() +
                ", Cabin: " + booking.getCabinId() +
                ", Guests: " + booking.getGuests() +
                ", Amount: " + booking.getAmount());

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getCabinId());
            stmt.setTimestamp(3, Timestamp.valueOf(booking.getStartDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(booking.getEndDate()));
            stmt.setInt(5, booking.getGuests());
            stmt.setDouble(6, booking.getAmount());
            stmt.setString(7, booking.getStatus() != null ? booking.getStatus() : "pending");
            stmt.setString(8, booking.getPaymentStatus() != null ? booking.getPaymentStatus() : "unpaid");
            stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));

            int rowsAffected = stmt.executeUpdate();

            // ✅ FIXED: Only commit for addBooking - this is typically called outside payment transactions
            if (!connection.getAutoCommit()) {
                connection.commit();
                System.out.println("DEBUG: Transaction committed explicitly");
            }

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        booking.setId(generatedId);
                        System.out.println("DEBUG: Booking created successfully with ID: " + generatedId);
                    }
                }
            }

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to add booking - " + e.getMessage());
            e.printStackTrace();
            // Rollback on error
            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                    System.out.println("DEBUG: Transaction rolled back");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("ERROR: Rollback failed - " + rollbackEx.getMessage());
            }
            return false;
        }
    }

    @Override
    public Booking getBookingById(int id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        System.out.println("DEBUG: Getting booking by ID: " + id);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Booking booking = extractBookingFromResultSet(rs);
                System.out.println("DEBUG: Booking found - ID: " + booking.getId() +
                        ", Status: " + booking.getStatus() +
                        ", Payment Status: " + booking.getPaymentStatus() +
                        ", Amount: " + booking.getAmount());
                return booking;
            } else {
                System.out.println("DEBUG: No booking found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to get booking by ID - " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings ORDER BY created_at DESC";
        System.out.println("DEBUG: Getting all bookings");

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }

            System.out.println("DEBUG: Retrieved " + bookings.size() + " total bookings");
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to get all bookings - " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ? ORDER BY created_at DESC";
        System.out.println("DEBUG: Getting bookings for user ID: " + userId);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }

            System.out.println("DEBUG: Retrieved " + bookings.size() + " bookings for user " + userId);
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to get bookings by user ID - " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByCabinId(int cabinId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE cabin_id = ? ORDER BY start_date ASC";
        System.out.println("DEBUG: Getting bookings for cabin ID: " + cabinId);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cabinId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }

            System.out.println("DEBUG: Retrieved " + bookings.size() + " bookings for cabin " + cabinId);
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to get bookings by cabin ID - " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public boolean updateBooking(Booking booking) {
        String sql = "UPDATE bookings SET user_id=?, cabin_id=?, start_date=?, end_date=?, guests=?, amount=?, status=?, payment_status=?, updated_at=? WHERE id=?";
        System.out.println("DEBUG: Updating booking ID: " + booking.getId());

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getCabinId());
            stmt.setTimestamp(3, Timestamp.valueOf(booking.getStartDate()));
            stmt.setTimestamp(4, Timestamp.valueOf(booking.getEndDate()));
            stmt.setInt(5, booking.getGuests());
            stmt.setDouble(6, booking.getAmount());
            stmt.setString(7, booking.getStatus());
            stmt.setString(8, booking.getPaymentStatus());
            stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(10, booking.getId());

            int rowsAffected = stmt.executeUpdate();

            // ✅ FIXED: Let calling service/servlet handle transaction commits
            // Removed: if (!connection.getAutoCommit()) { connection.commit(); }

            System.out.println("DEBUG: Booking update result - Rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to update booking - " + e.getMessage());
            e.printStackTrace();
            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("ERROR: Update rollback failed - " + rollbackEx.getMessage());
            }
            return false;
        }
    }

    @Override
    public boolean deleteBooking(int id) {
        String sql = "DELETE FROM bookings WHERE id = ?";
        System.out.println("DEBUG: Deleting booking ID: " + id);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            // ✅ FIXED: Let calling service/servlet handle transaction commits
            // Removed: if (!connection.getAutoCommit()) { connection.commit(); }

            System.out.println("DEBUG: Booking deletion result - Rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to delete booking - " + e.getMessage());
            e.printStackTrace();
            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("ERROR: Delete rollback failed - " + rollbackEx.getMessage());
            }
            return false;
        }
    }

    @Override
    public boolean updateBookingStatus(int id, String status) {
        String sql = "UPDATE bookings SET status = ?, updated_at = ? WHERE id = ?";
        System.out.println("DEBUG: Updating booking status - ID: " + id + ", Status: " + status);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3, id);

            int rowsAffected = stmt.executeUpdate();

            // ✅ FIXED: Removed individual commit - let PaymentServlet handle transaction
            // This allows atomic payment processing (both payment_status AND booking status updates together)

            System.out.println("DEBUG: Status update result - Rows affected: " + rowsAffected + " (not committed yet)");
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to update booking status - " + e.getMessage());
            e.printStackTrace();
            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("ERROR: Status update rollback failed - " + rollbackEx.getMessage());
            }
            return false;
        }
    }

    @Override
    public boolean updatePaymentStatus(int id, String paymentStatus) {
        String sql = "UPDATE bookings SET payment_status = ?, updated_at = ? WHERE id = ?";
        System.out.println("DEBUG: Updating payment status - ID: " + id + ", Status: " + paymentStatus);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, paymentStatus);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3, id);

            int rowsAffected = stmt.executeUpdate();

            // ✅ FIXED: Removed individual commit - let PaymentServlet handle transaction
            // This allows atomic payment processing (both payment_status AND booking status updates together)

            System.out.println("DEBUG: Payment status update result - Rows affected: " + rowsAffected + " (not committed yet)");
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to update payment status - " + e.getMessage());
            e.printStackTrace();
            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("ERROR: Payment status update rollback failed - " + rollbackEx.getMessage());
            }
            return false;
        }
    }

    private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setCabinId(rs.getInt("cabin_id"));

        // Handle potential null timestamps
        Timestamp startTime = rs.getTimestamp("start_date");
        Timestamp endTime = rs.getTimestamp("end_date");
        Timestamp createdTime = rs.getTimestamp("created_at");
        Timestamp updatedTime = rs.getTimestamp("updated_at");

        if (startTime != null) booking.setStartDate(startTime.toLocalDateTime());
        if (endTime != null) booking.setEndDate(endTime.toLocalDateTime());
        if (createdTime != null) booking.setCreatedAt(createdTime.toLocalDateTime());
        if (updatedTime != null) booking.setUpdatedAt(updatedTime.toLocalDateTime());

        booking.setGuests(rs.getInt("guests"));
        booking.setAmount(rs.getDouble("amount"));
        booking.setStatus(rs.getString("status"));
        booking.setPaymentStatus(rs.getString("payment_status"));

        return booking;
    }
}
