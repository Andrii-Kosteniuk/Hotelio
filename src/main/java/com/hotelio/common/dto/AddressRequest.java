package com.hotelio.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AddressRequest(
        @NotBlank
        @Size(max = 100)
        String country,

        @NotBlank
        @Size(max = 100)
        String city,

        @NotBlank
        @Size(max = 100)
        String district,

        @NotBlank
        @Size(max = 200)
        String street,

        @NotNull
        @Positive
        Integer buildingNumber,

        @NotBlank
        @Size(max = 20)
        String zipCode
) {
}
