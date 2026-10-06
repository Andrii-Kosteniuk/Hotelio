package com.dev.hotelio.hotel.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Entity
@Table(name = "hotels",
        indexes = {
                @Index(name = "idx_hotel_name", columnList = "hotel_name")
        })
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Hotel {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Embedded
    private Address address;

    @Column(name = "star_rating", nullable = false)
    private Integer starRating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private HotelStatus status;

    @Column(name = "approved_by")
    private UUID approvedBy;

    @Column(name = "approved_at")
    private OffsetDateTime approvedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Version
    private Long version;


    public static Hotel createNew(String name, String description, Address address, Integer starRating) {
        return Hotel.builder()
                .name(name)
                .description(description)
                .address(address)
                .starRating(starRating)
                .status(HotelStatus.PENDING_REVIEW)
                .createdAt(OffsetDateTime.now(ZoneId.systemDefault()))
                .build();

    }


}
