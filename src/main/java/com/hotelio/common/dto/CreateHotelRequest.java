package com.hotelio.common.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateHotelRequest(

        @NotBlank
        @Size(max = 200)
        String name,

        @NotBlank
        String description,

        @NotNull
        @Valid
        AddressRequest address,

        @NotNull
        @Min(1)
        @Max(5)
        Integer starRating,

        @NotNull
        @DecimalMin("0.00")
        BigDecimal pricePerNight

) {
}
