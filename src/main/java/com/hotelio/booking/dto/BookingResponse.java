package com.hotelio.booking.dto;

import com.hotelio.booking.model.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record BookingResponse(

        UUID id,
        UUID roomId,
        LocalDate checkIn,
        LocalDate checkOut,
        int guests,
        BookingStatus status,
        BigDecimal totalPrice
) {
}
