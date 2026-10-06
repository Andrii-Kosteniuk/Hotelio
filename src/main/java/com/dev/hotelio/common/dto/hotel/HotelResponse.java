package com.dev.hotelio.common.dto.hotel;

import com.dev.hotelio.hotel.domain.Address;
import java.util.UUID;

public record HotelResponse(

        UUID id,
        String name,
        String description,
        Address address,
        Integer starRating
) {
}
