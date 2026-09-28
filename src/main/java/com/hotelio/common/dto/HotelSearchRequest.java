package com.hotelio.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record HotelSearchRequest(
        String city,
        String country,
        String name,
        @Min(1) @Max(5) Integer minStarRating,
        @Min(1) @Max(5) Integer maxStarRating,
        BigDecimal minPrice,
        BigDecimal maxPrice

) {
}
