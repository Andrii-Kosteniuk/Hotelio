package com.hotelio.hotel.mapper;

import com.hotelio.hotel.dto.HotelSearchCriteria;
import com.hotelio.hotel.dto.HotelSearchRequest;
import com.hotelio.room.dto.RoomSearchCriteria;
import org.springframework.stereotype.Component;

@Component
public class HotelSearchMapper {

    public HotelSearchCriteria toCriteria(
            HotelSearchRequest request
    ) {
        RoomSearchCriteria roomCriteria =
                new RoomSearchCriteria(
                        request.roomType(),
                        request.guests(),
                        request.bedCount(),
                        request.minRoomPrice(),
                        request.maxRoomPrice(),
                        request.checkIn(),
                        request.checkOut()
                );

        return new HotelSearchCriteria(
                request.city(),
                request.country(),
                request.name(),
                request.minStarRating(),
                request.maxStarRating(),
                roomCriteria
        );
    }

}


