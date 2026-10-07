package com.hotelio.hotel.validation;

import com.hotelio.common.exception.RequestParameterNotValidException;
import com.hotelio.hotel.dto.HotelSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class HotelSearchValidator {

    private final Clock clock;

    public void validate(HotelSearchRequest request) {

        validateRatingRange(request);
        validatePriceRange(request);
        validateDateRange(request);
    }

    private void validateRatingRange(
            HotelSearchRequest request
    ) {
        if (request.minStarRating() != null
                && request.maxStarRating() != null
                && request.minStarRating()
                > request.maxStarRating()) {

            throw new RequestParameterNotValidException(
                    "Minimum star rating cannot be greater than maximum star rating"
            );
        }
    }

    private void validatePriceRange(
            HotelSearchRequest request
    ) {
        if (request.minRoomPrice() != null
                && request.maxRoomPrice() != null
                && request.minRoomPrice()
                .compareTo(request.maxRoomPrice()) > 0) {

            throw new RequestParameterNotValidException(
                    "Minimum room price cannot be greater than maximum room price"
            );
        }
    }

    private void validateDateRange(HotelSearchRequest request) {
        LocalDate checkIn = request.checkIn();
        LocalDate checkOut = request.checkOut();

        if ((checkIn == null) != (checkOut == null)) {
            throw new RequestParameterNotValidException(
                    "Check-in and check-out must be provided together"
            );
        }

        if (checkIn == null) {
            return;
        }

        if (checkIn.isBefore(LocalDate.now(clock))) {
            throw new RequestParameterNotValidException(
                    "Check-in date cannot be in the past"
            );
        }

        if (!checkOut.isAfter(checkIn)) {
            throw new RequestParameterNotValidException(
                    "Check-out date must be after check-in date"
            );
        }
    }
}