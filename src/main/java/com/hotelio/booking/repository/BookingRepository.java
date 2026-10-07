package com.hotelio.booking.repository;

import com.hotelio.booking.model.Booking;

import java.time.LocalDate;
import java.util.UUID;

public interface BookingRepository {

    Booking save(Booking booking);

    boolean existsOverlappingBooking(
            UUID roomId,
            LocalDate checkIn,
            LocalDate checkOut
    );
}
