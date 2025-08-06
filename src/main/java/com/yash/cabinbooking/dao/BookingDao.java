package com.yash.cabinbooking.dao;

import com.yash.cabinbooking.model.Booking;
import java.util.List;

public interface BookingDao {
    boolean addBooking(Booking booking);
    Booking getBookingById(int id);
    List<Booking> getAllBookings();
    List<Booking> getBookingsByUserId(int userId);
    List<Booking> getBookingsByCabinId(int cabinId);
    boolean updateBooking(Booking booking);
    boolean deleteBooking(int id);
    boolean updateBookingStatus(int id, String status);
    boolean updatePaymentStatus(int id, String paymentStatus);
}
