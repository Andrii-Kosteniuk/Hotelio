package com.hotelio.booking.service;

import com.hotelio.booking.application.RoomAvailabilityService;
import com.hotelio.booking.dto.BookingResponse;
import com.hotelio.booking.dto.CreateBookingRequest;
import com.hotelio.booking.mapper.BookingMapper;
import com.hotelio.booking.model.Booking;
import com.hotelio.booking.repository.BookingRepository;
import com.hotelio.common.exception.RequestParameterNotValidException;
import com.hotelio.common.exception.ResourceNotFoundException;
import com.hotelio.room.domain.Room;
import com.hotelio.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomAvailabilityService roomAvailabilityService;
    private final RoomRepository roomRepository;
    private final BookingMapper bookingMapper;


    @Override
    @Transactional
    public BookingResponse createBooking(CreateBookingRequest bookingRequest) {

        UUID roomId = bookingRequest.roomId();
        LocalDate checkIn = bookingRequest.checkIn();
        LocalDate checkOut = bookingRequest.checkOut();

        boolean available = roomAvailabilityService.isAvailable(roomId, checkIn, checkOut);

        if (!available) {
            throw new RequestParameterNotValidException(
                    "Room is not available for the selected dates"
            );
        }


        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room with id '%s' was not found"
                                        .formatted(roomId)
                        )
                );

        if (bookingRequest.guests() > room.getCapacity()) {
            throw new RequestParameterNotValidException(
                    "Number of guests exceeds room capacity"
            );
        }

        BigDecimal totalPrice = calculateTotalPrice(room, checkIn, checkOut);

        Booking booking = Booking.createPending(
                room,
                checkIn,
                checkOut,
                bookingRequest.guests(),
                totalPrice);

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingResponse(savedBooking);
    }

    private BigDecimal calculateTotalPrice(Room room, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        BigDecimal pricePerNight = room.getPricePerNight();

        return pricePerNight.multiply(BigDecimal.valueOf(nights)
        );
    }

}
