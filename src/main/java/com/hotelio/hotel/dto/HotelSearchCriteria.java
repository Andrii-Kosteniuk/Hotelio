package com.hotelio.hotel.dto;


import com.hotelio.room.dto.RoomSearchCriteria;

public record HotelSearchCriteria(

        String city,
        String country,
        String name,
        Integer minStarRating,
        Integer maxStarRating,
        RoomSearchCriteria roomSearchCriteria
) {
}


