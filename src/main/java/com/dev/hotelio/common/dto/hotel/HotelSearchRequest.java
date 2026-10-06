package com.dev.hotelio.common.dto.hotel;

import com.dev.hotelio.room.domain.RoomType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record HotelSearchRequest(
        String city,
        String country,
        String name,
        @Min(1) @Max(5) Integer minStarRating,
        @Min(1) @Max(5) Integer maxStarRating,
        RoomType roomType,
        Integer roomCapacity,
        Integer bedCount
) {
}
