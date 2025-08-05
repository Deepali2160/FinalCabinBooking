package com.yash.cabinbooking.daoimpl;
import com.yash.cabinbooking.dao.BookingDao;
import com.yash.cabinbooking.model.Booking;

import java.sql.*;
import java.util.*;

public class BookingDaoImpl implements BookingDao {

    private Connection conn;

    public BookingDaoImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Booking b = new Booking();
                b.setId(rs.getInt("id"));
                b.setUserId(rs.getInt("user_id"));
                b.setCabinName(rs.getString("cabin_name"));
                b.setStartDate(rs.getDate("start_date"));
                b.setEndDate(rs.getDate("end_date"));
                b.setGuests(rs.getInt("guests"));
                b.setAmount(rs.getDouble("amount"));
                b.setStatus(rs.getString("status"));

                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    public boolean addBooking (Booking booking){
        String sql = "INSERT INTO bookings (user_id, cabin_name, start_date, end_date, guests, amount, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            conn.setAutoCommit(false);  // Begin transaction

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, booking.getUserId());
                ps.setString(2, booking.getCabinName());
                ps.setDate(3, new java.sql.Date(booking.getStartDate().getTime()));
                ps.setDate(4, new java.sql.Date(booking.getEndDate().getTime()));
                ps.setInt(5, booking.getGuests());
                ps.setDouble(6, booking.getAmount());
                ps.setString(7, booking.getStatus());

                int rowsInserted = ps.executeUpdate();

                if (rowsInserted == 1) {
                    conn.commit(); // ✅ Commit transaction
                    return true;
                } else {
                    conn.rollback(); // ❌ Something went wrong, rollback
                }
            }

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback(); // rollback in case of exception
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true); // reset auto-commit to true
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}