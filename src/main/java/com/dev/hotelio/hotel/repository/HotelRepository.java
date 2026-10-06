package com.dev.hotelio.hotel.repository;

import com.dev.hotelio.common.dto.hotel.HotelSearchCriteria;
import com.dev.hotelio.hotel.domain.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface HotelRepository {

    Optional<Hotel> findById(UUID id);

    Hotel save(Hotel hotel);

    boolean existsByName(String name);

    Page<Hotel> findAllHotels(HotelSearchCriteria criteria, Pageable pageable);


}
