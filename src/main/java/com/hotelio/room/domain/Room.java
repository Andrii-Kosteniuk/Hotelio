package com.hotelio.room.domain;

import com.hotelio.hotel.domain.Hotel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@Entity
@Table(
        name = "rooms",
        indexes = {
                @Index(name = "idx_rooms_hotel_id", columnList = "hotel_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue
    @Column(name = "room_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RoomType type;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "bed_count", nullable = false)
    private Integer bedCount;

    @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RoomStatus status;

    public static Room createNew(
            Hotel hotel,
            RoomType type,
            Integer capacity,
            Integer bedCount,
            BigDecimal pricePerNight
    ) {
        return Room.builder()
                .hotel(hotel)
                .type(type)
                .capacity(capacity)
                .bedCount(bedCount)
                .pricePerNight(pricePerNight)
                .status(RoomStatus.ACTIVE)
                .build();
    }
}