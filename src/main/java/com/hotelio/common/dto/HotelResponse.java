package com.hotelio.common.dto;

import com.hotelio.hotel.domain.Address;
import com.hotelio.hotel.domain.HotelStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record HotelResponse(

        UUID id,
        String name,
        String description,
        Address address,
        Integer starRating,
        BigDecimal pricePerNight,
        HotelStatus status,
        OffsetDateTime createdAt

) {
}
