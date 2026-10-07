package com.hotelio.booking.application;

import com.hotelio.booking.repository.BookingRepository;
import com.hotelio.common.exception.RequestParameterNotValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomAvailabilityService {

    private final BookingRepository bookingRepository;

    public boolean isAvailable(UUID roomId, LocalDate checkIn, LocalDate checkOut) {
        validateDateRange(checkIn, checkOut);

        return !bookingRepository.existsOverlappingBooking(
                roomId,
                checkIn,
                checkOut
        );
    }

    private void validateDateRange(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new RequestParameterNotValidException(
                    "Check-in and check-out dates are required"
            );
        }

        if (!checkOut.isAfter(checkIn)) {
            throw new RequestParameterNotValidException(
                    "Check-out date must be after check-in date"
            );
        }


        LocalDate today = LocalDate.now(ZoneId.systemDefault());

        if (checkIn.isBefore(today)) {
            throw new RequestParameterNotValidException(
                    "Check-in date cannot be in the past"
            );
        }

        if (checkOut.isBefore(today)) {
            throw new RequestParameterNotValidException(
                    "Check-out date cannot be in the past"
            );
        }
    }
}
