package com.hotelio.hotel.infrastructure;

import com.hotelio.hotel.domain.Hotel;
import com.hotelio.hotel.domain.HotelStatus;
import com.hotelio.hotel.dto.HotelSearchCriteria;
import com.hotelio.hotel.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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
        return repository.existsByName(name);
    }

    @Override
    public Page<Hotel> findAllHotels(HotelSearchCriteria criteria, Pageable pageable) {

        Specification<Hotel> specification = HotelSpecifications.hasStatus(HotelStatus.ACTIVE);

        if (StringUtils.hasText(criteria.city())) {
            specification = specification
                    .and(HotelSpecifications.cityContains(criteria.city()));
        }

        if (StringUtils.hasText(criteria.country())) {
            specification = specification
                    .and(HotelSpecifications.countryEquals(criteria.country()));
        }

        if (StringUtils.hasText(criteria.name())) {
            specification = specification
                    .and(HotelSpecifications.nameContains(criteria.name()));
        }

        if (criteria.minStarRating() != null) {
            specification = specification
                    .and(HotelSpecifications.minStarRating(criteria.minStarRating()));
        }

        if (criteria.maxStarRating() != null) {
            specification = specification
                    .and(HotelSpecifications.maxStarRating(criteria.maxStarRating()));
        }

        if (criteria.roomSearchCriteria() != null) {
            specification = specification
                    .and(HotelSpecifications.hasMatchingRoom(criteria.roomSearchCriteria()));
        }

        return repository.findAll(specification, pageable);
    }
}
