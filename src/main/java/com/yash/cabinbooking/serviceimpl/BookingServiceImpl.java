package com.yash.cabinbooking.serviceimpl;

import com.yash.cabinbooking.dao.BookingDao;
import com.yash.cabinbooking.daoimpl.BookingDaoImpl;
import com.yash.cabinbooking.model.Booking;
import com.yash.cabinbooking.service.BookingService;

import java.sql.Connection;
import java.util.List;

public class BookingServiceImpl implements BookingService {

    private BookingDao bookingDao;

    public BookingServiceImpl(Connection conn) {
        this.bookingDao = new BookingDaoImpl(conn);
    }

    @Override
    public List<Booking> getBookingsForUser(int userId) {
        return bookingDao.getBookingsByUserId(userId);
    }
    @Override
    public boolean addBooking(Booking booking) {
        return bookingDao.addBooking(booking);
    }

}

