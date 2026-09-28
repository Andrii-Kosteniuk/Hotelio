package com.hotelio.hotel.service;

import com.hotelio.common.dto.*;
import com.hotelio.common.exception.ResourceAlreadyExistsException;
import com.hotelio.common.exception.ResourceNotFoundException;
import com.hotelio.common.mapper.HotelMapper;
import com.hotelio.hotel.domain.Hotel;
import com.hotelio.hotel.repository.HotelRepository;
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
    public PageResponse<HotelResponse> getActiveHotels(Pageable pageable) {
        Page<Hotel> hotels = hotelRepository.findActiveHotels(pageable);
        return hotelMapper.toPageResponse(hotels);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<HotelResponse> searchHotels(HotelSearchRequest request, Pageable pageable) {

        HotelSearchCriteria criteria = new HotelSearchCriteria(
                request.city(),
                request.country(),
                request.name(),
                request.minStarRating(),
                request.maxStarRating(),
                request.minPrice(),
                request.maxPrice()
        );

        Page<Hotel> hotels = hotelRepository.search(criteria, pageable);

        return hotelMapper.toPageResponse(hotels);
    }
}
