package com.hotelio.hotel.service;

import com.hotelio.hotel.dto.CreateHotelRequest;
import com.hotelio.hotel.dto.HotelResponse;
import com.hotelio.hotel.dto.HotelSearchRequest;
import com.hotelio.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface HotelService {

    HotelResponse createHotel(CreateHotelRequest request);

    HotelResponse getHotel(UUID hotelId);

    PageResponse<HotelResponse> search(HotelSearchRequest request, Pageable pageable);
}
