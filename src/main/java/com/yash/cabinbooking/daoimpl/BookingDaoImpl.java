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
    public int countBookingsByApprovalStatus(String approvalStatus) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE approval_status = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, approvalStatus);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    @Override
    public boolean addBooking(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, cabin_id, start_date, end_date, guests, amount, status, payment_status, approval_status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            stmt.setString(9, booking.getApprovalStatus() != null ? booking.getApprovalStatus() : "pending_approval");
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            stmt.setTimestamp(10, now);
            stmt.setTimestamp(11, now);

            int rowsAffected = stmt.executeUpdate();

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
        String sql = "SELECT b.*, u.name AS user_name, u.email AS user_email, c.name AS cabin_name, c.location AS cabin_location, " +
                "a.name AS approved_by_name " +
                "FROM bookings b " +
                "JOIN users u ON b.user_id = u.id " +
                "JOIN cabins c ON b.cabin_id = c.id " +
                "LEFT JOIN users a ON b.approved_by = a.id " +
                "WHERE b.id = ?";

        System.out.println("DEBUG: Getting booking by ID: " + id);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Booking booking = extractBookingFromResultSetWithDetails(rs);
                System.out.println("DEBUG: Booking found - ID: " + booking.getId() +
                        ", Status: " + booking.getStatus() +
                        ", Payment Status: " + booking.getPaymentStatus() +
                        ", Amount: " + booking.getAmount() +
                        ", Approval Status: " + booking.getApprovalStatus());
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
        String sql = "UPDATE bookings SET user_id=?, cabin_id=?, start_date=?, end_date=?, guests=?, amount=?, status=?, payment_status=?, updated_at=?, " +
                "approval_status=?, approved_by=?, approved_at=?, admin_remarks=?, rejection_reason=? WHERE id=?";
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

            // Approval fields
            stmt.setString(10, booking.getApprovalStatus());
            if (booking.getApprovedBy() != null) {
                stmt.setInt(11, booking.getApprovedBy());
            } else {
                stmt.setNull(11, Types.INTEGER);
            }
            if (booking.getApprovedAt() != null) {
                stmt.setTimestamp(12, Timestamp.valueOf(booking.getApprovedAt()));
            } else {
                stmt.setNull(12, Types.TIMESTAMP);
            }
            stmt.setString(13, booking.getAdminRemarks());
            stmt.setString(14, booking.getRejectionReason());

            stmt.setInt(15, booking.getId());

            int rowsAffected = stmt.executeUpdate();

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

            System.out.println("DEBUG: Status update result - Rows affected: " + rowsAffected);
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

            System.out.println("DEBUG: Payment status update result - Rows affected: " + rowsAffected);
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

    // === New method: Update approval status and related info ===
    @Override
    public boolean updateApprovalStatus(int bookingId, String approvalStatus, Integer approvedBy, String adminRemarks, String rejectionReason) {
        String sql = "UPDATE bookings SET approval_status = ?, approved_by = ?, approved_at = ?, admin_remarks = ?, rejection_reason = ?, updated_at = ? WHERE id = ?";
        System.out.println("DEBUG: Updating approval status - Booking ID: " + bookingId + ", ApprovalStatus: " + approvalStatus);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, approvalStatus);

            if (approvedBy != null) {
                stmt.setInt(2, approvedBy);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            stmt.setString(4, adminRemarks);
            stmt.setString(5, rejectionReason);
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(7, bookingId);

            int rowsAffected = stmt.executeUpdate();

            System.out.println("DEBUG: Approval status update result - Rows affected: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("ERROR: Failed to update approval status - " + e.getMessage());
            e.printStackTrace();
            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                    System.out.println("DEBUG: Rollback on approval status update");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("ERROR: Rollback on approval status update failed - " + rollbackEx.getMessage());
            }
            return false;
        }
    }

    // === New method: Get bookings by approval status ===
    @Override
    public List<Booking> getBookingsByApprovalStatus(String approvalStatus) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, u.name AS user_name, u.email AS user_email, c.name AS cabin_name, c.location AS cabin_location, " +
                "a.name AS approved_by_name " +
                "FROM bookings b " +
                "JOIN users u ON b.user_id = u.id " +
                "JOIN cabins c ON b.cabin_id = c.id " +
                "LEFT JOIN users a ON b.approved_by = a.id " +
                "WHERE b.approval_status = ? " +
                "ORDER BY b.created_at DESC";

        System.out.println("DEBUG: Getting bookings with approval status: " + approvalStatus);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, approvalStatus);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(extractBookingFromResultSetWithDetails(rs));
            }

            System.out.println("DEBUG: Retrieved " + bookings.size() + " bookings with approval status '" + approvalStatus + "'");
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to get bookings by approval status - " + e.getMessage());
            e.printStackTrace();
        }

        return bookings;
    }

    // === New method: Get all bookings with joined user/cabin/admin info (optional for dashboard) ===
    @Override
    public List<Booking> getBookingsWithDetails() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, u.name AS user_name, u.email AS user_email, c.name AS cabin_name, c.location AS cabin_location, " +
                "a.name AS approved_by_name " +
                "FROM bookings b " +
                "JOIN users u ON b.user_id = u.id " +
                "JOIN cabins c ON b.cabin_id = c.id " +
                "LEFT JOIN users a ON b.approved_by = a.id " +
                "ORDER BY b.created_at DESC";

        System.out.println("DEBUG: Getting all bookings with details");

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bookings.add(extractBookingFromResultSetWithDetails(rs));
            }

            System.out.println("DEBUG: Retrieved " + bookings.size() + " bookings with details");
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to get bookings with details - " + e.getMessage());
            e.printStackTrace();
        }

        return bookings;
    }

    // Helper method to extract Booking with joined user/cabin/admin info
    private Booking extractBookingFromResultSetWithDetails(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setCabinId(rs.getInt("cabin_id"));

        Timestamp startTime = rs.getTimestamp("start_date");
        Timestamp endTime = rs.getTimestamp("end_date");
        Timestamp createdTime = rs.getTimestamp("created_at");
        Timestamp updatedTime = rs.getTimestamp("updated_at");
        Timestamp approvedAtTime = rs.getTimestamp("approved_at");

        if (startTime != null) booking.setStartDate(startTime.toLocalDateTime());
        if (endTime != null) booking.setEndDate(endTime.toLocalDateTime());
        if (createdTime != null) booking.setCreatedAt(createdTime.toLocalDateTime());
        if (updatedTime != null) booking.setUpdatedAt(updatedTime.toLocalDateTime());
        if (approvedAtTime != null) booking.setApprovedAt(approvedAtTime.toLocalDateTime());

        booking.setGuests(rs.getInt("guests"));
        booking.setAmount(rs.getDouble("amount"));
        booking.setStatus(rs.getString("status"));
        booking.setPaymentStatus(rs.getString("payment_status"));
        booking.setApprovalStatus(rs.getString("approval_status"));
        int approvedBy = rs.getInt("approved_by");
        booking.setApprovedBy(rs.wasNull() ? null : approvedBy);
        booking.setAdminRemarks(rs.getString("admin_remarks"));
        booking.setRejectionReason(rs.getString("rejection_reason"));

        booking.setUserName(rs.getString("user_name"));
        booking.setUserEmail(rs.getString("user_email"));
        booking.setCabinName(rs.getString("cabin_name"));
        booking.setCabinLocation(rs.getString("cabin_location"));
        booking.setApprovedByName(rs.getString("approved_by_name"));

        return booking;
    }

    // Extract basic Booking without joined info
    private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setCabinId(rs.getInt("cabin_id"));

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

        // Set approval fields only if present - for pure bookings table select this may be NULL
        try {
            booking.setApprovalStatus(rs.getString("approval_status"));
            int approvedBy = rs.getInt("approved_by");
            booking.setApprovedBy(rs.wasNull() ? null : approvedBy);
            booking.setApprovedAt(rs.getTimestamp("approved_at") != null
                    ? rs.getTimestamp("approved_at").toLocalDateTime() : null);
            booking.setAdminRemarks(rs.getString("admin_remarks"));
            booking.setRejectionReason(rs.getString("rejection_reason"));
        } catch (SQLException ignore) {
            // ignore if columns not selected
        }

        return booking;
    }
}
