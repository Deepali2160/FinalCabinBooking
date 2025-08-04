package com.yash.cabinbooking.dao;

import com.yash.cabinbooking.model.Booking;

import java.util.List;

public interface BookingDao {
    List<Booking> getBookingsByUserId(int userId);
    boolean addBooking(Booking booking);

}
