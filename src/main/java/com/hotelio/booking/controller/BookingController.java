package com.hotelio.booking.controller;

import com.hotelio.booking.dto.BookingResponse;
import com.hotelio.booking.dto.CreateBookingRequest;
import com.hotelio.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody CreateBookingRequest bookingRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.createBooking(bookingRequest));
    }
}
