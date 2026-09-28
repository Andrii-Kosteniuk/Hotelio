package com.hotelio.hotel.controller;


import com.hotelio.common.dto.CreateHotelRequest;
import com.hotelio.common.dto.HotelResponse;
import com.hotelio.common.dto.HotelSearchRequest;
import com.hotelio.common.dto.PageResponse;
import com.hotelio.hotel.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelResponse> createHotel(@Valid @RequestBody CreateHotelRequest createHotelRequest) {
        HotelResponse hotelResponse = hotelService.createHotel(createHotelRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelResponse);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelResponse> getHotel(@PathVariable UUID hotelId) {
        return ResponseEntity.ok(hotelService.getHotel(hotelId));
    }

    @GetMapping
    public PageResponse<HotelResponse> getHotels(Pageable pageable) {
        return hotelService.getActiveHotels(pageable);
    }

    @GetMapping("/search")
    public PageResponse<HotelResponse> searchHotels(
            @Valid @ModelAttribute HotelSearchRequest request,
            @PageableDefault(
                    size = 20,
                    sort = "pricePerNight",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        return hotelService.searchHotels(request, pageable);

    }
}