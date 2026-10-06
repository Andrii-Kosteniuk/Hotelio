package com.dev.hotelio.hotel.service;


import com.dev.hotelio.common.dto.PageResponse;
import com.dev.hotelio.common.dto.hotel.*;
import com.dev.hotelio.common.exception.ResourceAlreadyExistsException;
import com.dev.hotelio.common.exception.ResourceNotFoundException;
import com.dev.hotelio.common.mapper.HotelMapper;
import com.dev.hotelio.hotel.domain.Hotel;
import com.dev.hotelio.hotel.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    @Transactional
    public HotelResponse createHotel(CreateHotelRequest request) {

        if (hotelRepository.existsByName(request.name())) {
            throw new ResourceAlreadyExistsException(
                    "Hotel with name '%s' already exists".formatted(request.name())
            );
        }

        Hotel hotel = hotelMapper.toHotel(request);
        Hotel saved = hotelRepository.save(hotel);

        return hotelMapper.toHotelResponse(saved);
    }

    @Override
    public HotelResponse getHotel(UUID hotelId) {
        return hotelRepository.findById(hotelId)
                .map(hotelMapper::toHotelResponse)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Hotel not found with id %s:".formatted(hotelId)));
    }
    @Override
    @Transactional(readOnly = true)
    public PageResponse<HotelResponse> getHotels(HotelSearchRequest request, Pageable pageable) {

        HotelSearchCriteria criteria = new HotelSearchCriteria(
                request.city(),
                request.country(),
                request.name(),
                request.minStarRating(),
                request.maxStarRating(),
                request.roomType(),
                request.roomCapacity(),
                request.bedCount()
        );

        Page<Hotel> hotels = hotelRepository.findAllHotels(criteria, pageable);

        return hotelMapper.toPageResponse(hotels);
    }
}
