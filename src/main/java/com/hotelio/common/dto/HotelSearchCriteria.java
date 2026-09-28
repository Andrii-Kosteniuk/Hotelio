package com.hotelio.common.dto;

import java.math.BigDecimal;

public record HotelSearchCriteria(

        String city,
        String country,
        String name,
        Integer minStarRating,
        Integer maxStarRating,
        BigDecimal minPrice,
        BigDecimal maxPrice
) {
}


