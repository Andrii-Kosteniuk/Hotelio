package com.hotelio.hotel.repository;

import com.hotelio.common.dto.HotelSearchCriteria;
import com.hotelio.hotel.domain.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface HotelRepository {

    Optional<Hotel> findById(UUID id);

    Page<Hotel> findActiveHotels(Pageable pageable);

    Hotel save(Hotel hotel);

    boolean existsByName(String name);

    Page<Hotel> search(HotelSearchCriteria criteria, Pageable pageable);


}
