package com.dev.hotelio.hotel.infrastructure;

import com.dev.hotelio.hotel.domain.Hotel;
import com.dev.hotelio.hotel.domain.HotelStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface JpaHotelRepository extends JpaRepository<Hotel, UUID>, JpaSpecificationExecutor<Hotel> {

    Page<Hotel> findByStatus(HotelStatus status, Pageable pageable);
}
