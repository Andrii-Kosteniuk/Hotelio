package com.dev.hotelio.common.dto.hotel;


import com.dev.hotelio.room.domain.RoomType;

public record HotelSearchCriteria(

        String city,
        String country,
        String name,
        Integer minStarRating,
        Integer maxStarRating,
        RoomType roomType,
        Integer roomCapacity,
        Integer bedCount
) {
}


