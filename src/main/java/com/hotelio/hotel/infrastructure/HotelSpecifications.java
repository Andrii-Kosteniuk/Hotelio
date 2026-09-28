package com.hotelio.hotel.infrastructure;

import com.hotelio.hotel.domain.Hotel;
import com.hotelio.hotel.domain.HotelStatus;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class HotelSpecifications {
    private HotelSpecifications() {
        /* This utility class should not be instantiated */
    }

    public static Specification<Hotel> hasStatus(HotelStatus status) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Hotel> cityContains(String city) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("address").get("city")),
                        "%" + city.toLowerCase() + "%"
                );
    }

    public static Specification<Hotel> countryEquals(String country) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("address").get("country")),
                        country.toLowerCase()
                );
    }

    public static Specification<Hotel> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
    }

    public static Specification<Hotel> minStarRating(Integer rating) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("starRating"),
                        rating
                );
    }

    public static Specification<Hotel> maxStarRating(Integer rating) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("starRating"),
                        rating
                );
    }

    public static Specification<Hotel> minPrice(BigDecimal price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("pricePerNight"),
                        price
                );
    }

    public static Specification<Hotel> maxPrice(BigDecimal price) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("pricePerNight"),
                        price
                );
    }
}
