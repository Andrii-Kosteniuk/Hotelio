package com.hotelio.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

public record BookingResponse(

        @NotNull
        UUID id,

        @NotNull
        UUID roomId,

        @NotNull
        LocalDate checkIn,

        @NotNull
        LocalDate checkOut,

        @Positive
        int guests
) {
}
