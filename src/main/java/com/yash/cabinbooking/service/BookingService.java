package com.yash.cabinbooking.service;

import com.yash.cabinbooking.model.Booking;

import java.util.List;

public interface BookingService {
    List<Booking> getBookingsForUser(int userId);
    boolean addBooking(Booking booking);

}