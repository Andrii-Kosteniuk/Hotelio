package com.hotelio.booking.service;

import com.hotelio.booking.dto.CreateBookingRequest;
import com.hotelio.booking.dto.BookingResponse;

public interface BookingService {

    BookingResponse createBooking(CreateBookingRequest request);
}
