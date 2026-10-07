package com.hotelio.hotel.service;


import com.hotelio.common.dto.PageResponse;
import com.hotelio.common.exception.ResourceAlreadyExistsException;
import com.hotelio.common.exception.ResourceNotFoundException;
import com.hotelio.hotel.mapper.HotelMapper;
import com.hotelio.hotel.domain.Hotel;
import com.hotelio.hotel.dto.CreateHotelRequest;
import com.hotelio.hotel.dto.HotelResponse;
import com.hotelio.hotel.dto.HotelSearchCriteria;
import com.hotelio.hotel.dto.HotelSearchRequest;
import com.hotelio.hotel.mapper.HotelSearchMapper;
import com.hotelio.hotel.repository.HotelRepository;
import com.hotelio.hotel.validation.HotelSearchValidator;
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
    private final HotelSearchMapper searchMapper;
    private final HotelSearchValidator searchValidator;

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
    public PageResponse<HotelResponse> search(HotelSearchRequest request, Pageable pageable) {

        searchValidator.validate(request);

        HotelSearchCriteria criteria = searchMapper.toCriteria(request);

        Page<Hotel> hotels = hotelRepository.findAllHotels(criteria, pageable);

        return hotelMapper.toPageResponse(hotels);
    }
}
