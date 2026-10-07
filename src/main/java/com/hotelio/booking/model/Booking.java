package com.hotelio.booking.model;

import com.hotelio.room.domain.Room;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "bookings",
        indexes = {
                @Index(
                        name = "idx_bookings_room_id",
                        columnList = "room_id"),
                @Index(
                        name = "idx_bookings_room_dates",
                        columnList = "room_id, check_in, check_out"),
                @Index(
                        name = "idx_bookings_room_status",
                        columnList = "room_id, status")
        }
)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private int guests;

    @Column(
            name = "total_price",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BookingStatus status;

    public static Booking createPending(
            Room room,
            LocalDate checkIn,
            LocalDate checkOut,
            int guests,
            BigDecimal totalPrice
    ) {
        return Booking.builder()
                .room(room)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .guests(guests)
                .totalPrice(totalPrice)
                .status(BookingStatus.PENDING)
                .build();
    }
}

