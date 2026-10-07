package com.hotelio.hotel.dto;

import java.util.UUID;

public record HotelResponse(

        UUID id,
        String name,
        String description,
        HotelAddressResponse address,
        Integer starRating
) {
}
