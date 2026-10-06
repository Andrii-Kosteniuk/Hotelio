package com.dev.hotelio.hotel.service;

import com.dev.hotelio.common.dto.hotel.CreateHotelRequest;
import com.dev.hotelio.common.dto.hotel.HotelResponse;
import com.dev.hotelio.common.dto.hotel.HotelSearchRequest;
import com.dev.hotelio.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface HotelService {

    HotelResponse createHotel(CreateHotelRequest request);

    HotelResponse getHotel(UUID hotelId);

    PageResponse<HotelResponse> getHotels(HotelSearchRequest request, Pageable pageable);
}
