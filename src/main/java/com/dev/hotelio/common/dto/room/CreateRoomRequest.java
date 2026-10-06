package com.dev.hotelio.common.dto.room;

import com.dev.hotelio.room.domain.RoomType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateRoomRequest(

        @NotNull
        RoomType type,

        @NotNull
        @Min(1)
        Integer capacity,

        @NotNull
        @Min(1)
        Integer bedCount,

        @NotNull
        @DecimalMin(value = "0.00")
        BigDecimal pricePerNight
) {
}
