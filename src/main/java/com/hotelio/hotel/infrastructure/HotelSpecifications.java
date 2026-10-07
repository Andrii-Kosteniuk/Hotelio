package com.hotelio.hotel.infrastructure;

import com.hotelio.booking.model.Booking;
import com.hotelio.booking.model.BookingStatus;
import com.hotelio.hotel.domain.Hotel;
import com.hotelio.hotel.domain.HotelStatus;
import com.hotelio.room.domain.Room;
import com.hotelio.room.domain.RoomStatus;
import com.hotelio.room.dto.RoomSearchCriteria;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class HotelSpecifications {
    private static final String HOTEL = "hotel";
    private static final String STATUS = "status";

    private HotelSpecifications() {
    }

    public static Specification<Hotel> hasStatus(HotelStatus status) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(STATUS), status);
    }

    public static Specification<Hotel> cityContains(String city) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("address").get("city")),
                        "%" + city.trim().toLowerCase(Locale.ROOT) + "%"
                );
    }

    public static Specification<Hotel> countryEquals(String country) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("address").get("country")),
                        country.trim().toLowerCase(Locale.ROOT)
                );
    }

    public static Specification<Hotel> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.trim().toLowerCase(Locale.ROOT) + "%"
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

    public static Specification<Hotel> hasMatchingRoom(RoomSearchCriteria criteria) {

        return (root, query, criteriaBuilder) -> {
            Subquery<UUID> subquery = query.subquery(UUID.class);

            Root<Room> room = subquery.from(Room.class);

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(
                    room.get(HOTEL).get("id"), root.get("id")));

            predicates.add(criteriaBuilder.equal(
                    room.get(STATUS), RoomStatus.ACTIVE));

            if (criteria.type() != null) {
                predicates.add(criteriaBuilder.equal(
                        room.get("type"), criteria.type()));
            }

            if (criteria.guests() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        room.get("capacity"), criteria.guests()));
            }

            if (criteria.bedCount() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        room.get("bedCount"), criteria.bedCount()));
            }

            if (criteria.minPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                                room.get("pricePerNight"),
                                criteria.minPrice()
                        )
                );
            }

            if (criteria.maxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                                room.get("pricePerNight"),
                                criteria.maxPrice()
                        )
                );
            }

            if (criteria.checkIn() != null
                    && criteria.checkOut() != null) {

                Subquery<UUID> bookingSubquery =
                        subquery.subquery(UUID.class);

                Root<Booking> booking =
                        bookingSubquery.from(Booking.class);

                bookingSubquery
                        .select(booking.get("id"))
                        .where(
                                criteriaBuilder.equal(
                                        booking.get("room").get("id"),
                                        room.get("id")
                                ),
                                booking.get(STATUS).in(
                                        BookingStatus.PENDING,
                                        BookingStatus.CONFIRMED
                                ),
                                criteriaBuilder.lessThan(
                                        booking.get("checkIn"),
                                        criteria.checkOut()
                                ),
                                criteriaBuilder.greaterThan(
                                        booking.get("checkOut"),
                                        criteria.checkIn()
                                )
                        );

                predicates.add(
                        criteriaBuilder.not(criteriaBuilder.exists(bookingSubquery))
                );
            }


            subquery.select(room.get("id"))
                    .where(predicates.toArray(Predicate[]::new));

            return criteriaBuilder.exists(subquery);
        };
    }

}
