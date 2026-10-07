package com.hotelio.hotel.dto;

public record HotelAddressResponse(
        String country,
        String city,
        String district,
        String street,
        Integer buildingNumber,
        String zipCode

) {
}

