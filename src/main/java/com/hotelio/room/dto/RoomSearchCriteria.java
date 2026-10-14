package com.hotelio.room.dto;

import com.hotelio.room.domain.RoomType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RoomSearchCriteria(

        RoomType type,
        Integer guests,
        Integer bedCount,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        LocalDate checkIn,
        LocalDate checkOut
) {

    public boolean hasFilters() {
        return type != null
                || guests != null
                || bedCount != null
                || minPrice != null
                || maxPrice != null
                || checkIn != null
                || checkOut != null;
    }
}
