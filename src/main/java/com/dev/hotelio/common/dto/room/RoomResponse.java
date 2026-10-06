package com.dev.hotelio.common.dto.room;

import com.dev.hotelio.room.domain.RoomStatus;
import com.dev.hotelio.room.domain.RoomType;

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
