package com.hotelio.hotel.validation;

import com.hotelio.common.exception.RequestParameterNotValidException;
import com.hotelio.hotel.dto.HotelSearchRequest;
import org.springframework.stereotype.Component;

@Component
public class HotelSearchValidator {

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

    private void validateDateRange(
            HotelSearchRequest request
    ) {
        boolean checkInPresent =
                request.checkIn() != null;

        boolean checkOutPresent =
                request.checkOut() != null;

        if (checkInPresent != checkOutPresent) {
            throw new RequestParameterNotValidException(
                    "Check-in and check-out must be provided together"
            );
        }

        if (checkInPresent
                && !request.checkOut()
                .isAfter(request.checkIn())) {

            throw new RequestParameterNotValidException(
                    "Check-out must be after check-in"
            );
        }
    }
}