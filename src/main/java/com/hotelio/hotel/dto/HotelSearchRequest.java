package com.hotelio.hotel.dto;

import com.hotelio.room.domain.RoomType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HotelSearchRequest(

        String city,
        String country,
        String name,

        @Min(1)
        @Max(5)
        Integer minStarRating,

        @Min(1)
        @Max(5)
        Integer maxStarRating,

        RoomType roomType,

        @Positive
        Integer guests,

        @Positive
        Integer bedCount,

        @DecimalMin("0.00")
        BigDecimal minRoomPrice,

        @DecimalMin("0.00")
        BigDecimal maxRoomPrice,

        LocalDate checkIn,

        LocalDate checkOut
) {
}
