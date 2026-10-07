package com.hotelio.room.dto;

import com.hotelio.room.domain.RoomType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateRoomRequest(

        @NotNull
        RoomType type,

        @NotNull
        @Positive
        Integer capacity,

        @NotNull
        @Positive
        Integer bedCount,

        @NotNull
        @DecimalMin("0.00")
        @Digits(integer = 8, fraction = 2)
        BigDecimal pricePerNight
) {
}
