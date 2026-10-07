package com.hotelio.room.dto;

import com.hotelio.room.domain.RoomStatus;
import com.hotelio.room.domain.RoomType;

import java.math.BigDecimal;
import java.util.UUID;

public record RoomResponse(
        UUID id,
        UUID hotelId,
        RoomType type,
        Integer capacity,
        Integer bedCount,
        BigDecimal pricePerNight,
        RoomStatus status

) {

}
