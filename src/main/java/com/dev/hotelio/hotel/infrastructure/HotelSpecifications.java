package com.dev.hotelio.hotel.infrastructure;

import com.dev.hotelio.hotel.domain.Hotel;

import com.dev.hotelio.hotel.domain.HotelStatus;
import com.dev.hotelio.room.domain.Room;
import com.dev.hotelio.room.domain.RoomStatus;
import com.dev.hotelio.room.domain.RoomType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class HotelSpecifications {
    private static final String HOTEL = "hotel";
    private static final String STATUS = "status";

    private HotelSpecifications() {
        /* This utility class should not be instantiated */
    }

    public static Specification<Hotel> hasStatus(HotelStatus status) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(STATUS), status);
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

    public static Specification<Hotel> hasRoomType(RoomType type) {
        return (root, query, cb) -> {
            Subquery<UUID> subquery = query.subquery(UUID.class);

            Root<Room> room = subquery.from(Room.class);

            subquery.select(room.get("id"))
                    .where(
                            cb.equal(
                                    room.get(HOTEL).get("id"),
                                    root.get("id")
                            ),
                            cb.equal(room.get("type"), type),
                            cb.equal(room.get(STATUS), RoomStatus.ACTIVE)
                    );

            return cb.exists(subquery);
        };
    }

    public static Specification<Hotel> hasRoomCapacity(Integer capacity) {
        return (root, query, cb) -> {
            Subquery<UUID> subquery = query.subquery(UUID.class);

            Root<Room> room = subquery.from(Room.class);

            subquery.select(room.get("id"))
                    .where(
                            cb.equal(
                                    room.get(HOTEL).get("id"),
                                    root.get("id")
                            ),
                            cb.greaterThanOrEqualTo(
                                    room.get("capacity"),
                                    capacity
                            ),
                            cb.equal(room.get(STATUS), RoomStatus.ACTIVE)
                    );

            return cb.exists(subquery);
        };
    }

    public static Specification<Hotel> hasBedCount(Integer bedCount) {
        return (root, query, cb) -> {
            Subquery<UUID> subquery = query.subquery(UUID.class);

            Root<Room> room = subquery.from(Room.class);

            subquery.select(room.get("id"))
                    .where(
                            cb.equal(
                                    room.get(HOTEL).get("id"),
                                    root.get("id")
                            ),
                            cb.greaterThanOrEqualTo(
                                    room.get("bedCount"),
                                    bedCount
                            ),
                            cb.equal(room.get(STATUS), RoomStatus.ACTIVE)
                    );

            return cb.exists(subquery);
        };
    }


}
