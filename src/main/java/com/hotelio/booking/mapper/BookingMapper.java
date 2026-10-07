package com.hotelio.booking.mapper;

import com.hotelio.booking.dto.BookingResponse;
import com.hotelio.booking.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "roomId", source = "room.id")
    BookingResponse toBookingResponse(Booking booking);

}



