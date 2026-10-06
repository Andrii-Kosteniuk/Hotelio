package com.dev.hotelio.common.dto.hotel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record CreateHotelRequest(

        @NotBlank
        @Size(max = 200)
        String name,

        @NotBlank
        String description,

        @NotNull
        @Valid
        HotelAddressRequest address,

        @NotNull
        @Min(1)
        @Max(5)
        Integer starRating

) {
}
