package com.hotelio.hotel.service;

import com.hotelio.common.dto.CreateHotelRequest;
import com.hotelio.common.dto.HotelResponse;
import com.hotelio.common.dto.HotelSearchRequest;
import com.hotelio.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface HotelService {

    HotelResponse createHotel(CreateHotelRequest request);

    HotelResponse getHotel(UUID hotelId);

    PageResponse<HotelResponse> getActiveHotels(Pageable pageable);

    PageResponse<HotelResponse> searchHotels(HotelSearchRequest request, Pageable pageable);
}
