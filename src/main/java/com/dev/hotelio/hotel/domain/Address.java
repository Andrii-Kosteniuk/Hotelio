package com.dev.hotelio.hotel.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String district;

    @Column(nullable = false, length = 150)
    private String street;

    @Column(name = "building_number", nullable = false)
    private Integer buildingNumber;

    @Column(name = "zip_code", nullable = false, length = 20)
    private String zipCode;

}