package com.dev.hotelio.hotel.infrastructure;

import com.dev.hotelio.common.dto.hotel.HotelSearchCriteria;
import com.dev.hotelio.hotel.domain.Hotel;
import com.dev.hotelio.hotel.domain.HotelStatus;
import com.dev.hotelio.hotel.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class HotelRepositoryImpl implements HotelRepository {

    private final JpaHotelRepository repository;

    @Override
    public Optional<Hotel> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Hotel save(Hotel hotel) {
        return repository.save(hotel);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.exists(Example.of(Hotel.builder().name(name).build()));
    }

    @Override
    public Page<Hotel> findAllHotels(HotelSearchCriteria criteria, Pageable pageable) {

        Specification<Hotel> specification = HotelSpecifications.hasStatus(HotelStatus.ACTIVE);

        if (criteria.city() != null && !criteria.city().isBlank()) {
            specification = specification.and(HotelSpecifications.cityContains(criteria.city()));
        }

        if (criteria.country() != null && !criteria.country().isBlank()) {
            specification = specification.and(
                    HotelSpecifications.countryEquals(criteria.country())
            );
        }

        if (criteria.name() != null && !criteria.name().isBlank()) {
            specification = specification.and(
                    HotelSpecifications.nameContains(criteria.name())
            );
        }

        if (criteria.minStarRating() != null) {
            specification = specification.and(
                    HotelSpecifications.minStarRating(
                            criteria.minStarRating()
                    )
            );
        }

        if (criteria.maxStarRating() != null) {
            specification = specification.and(
                    HotelSpecifications.maxStarRating(
                            criteria.maxStarRating()
                    )
            );
        }
        if (criteria.roomType() != null) {
            specification = specification.and(
                    HotelSpecifications.hasRoomType(
                            criteria.roomType()
                    )
            );
        }

        if (criteria.roomCapacity() != null) {
            specification = specification.and(
                    HotelSpecifications.hasRoomCapacity(
                            criteria.roomCapacity()
                    )
            );
        }

        if (criteria.bedCount() != null) {
            specification = specification.and(
                    HotelSpecifications.hasBedCount(
                            criteria.bedCount()
                    )
            );
        }

        return repository.findAll(specification, pageable);
    }
}
