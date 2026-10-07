package com.hotelio.booking.service;

import com.hotelio.booking.application.RoomAvailabilityService;
import com.hotelio.booking.dto.BookingResponse;
import com.hotelio.booking.dto.CreateBookingRequest;
import com.hotelio.booking.mapper.BookingMapper;
import com.hotelio.booking.model.Booking;
import com.hotelio.booking.repository.BookingRepository;
import com.hotelio.common.exception.RequestParameterNotValidException;
import com.hotelio.room.mapper.RoomMapper;
import com.hotelio.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomAvailabilityService roomAvailabilityService;
    private final RoomService roomService;
    private final BookingMapper bookingMapper;
    private final RoomMapper roomMapper;


    @Override
    @Transactional
    public BookingResponse createBooking(CreateBookingRequest bookingRequest) {

        UUID roomId = bookingRequest.roomId();
        boolean available = roomAvailabilityService.isAvailable(roomId, bookingRequest.checkIn(), bookingRequest.checkOut());

        if (!available) {
            throw new RequestParameterNotValidException(
                    "Room is not available for the selected dates"
            );
        }

        var room = roomMapper.toRoom(roomService.getRoom(roomId));

        Booking booking = Booking.createPending(
                room,
                bookingRequest.checkIn(),
                bookingRequest.checkOut(),
                bookingRequest.guests());

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingResponse(savedBooking);
    }
}
