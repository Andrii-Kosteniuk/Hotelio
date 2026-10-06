package com.dev.hotelio.common.mapper;

import com.dev.hotelio.common.dto.room.RoomResponse;
import com.dev.hotelio.room.domain.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "hotelId", source = "hotel.id")
    RoomResponse toRoomResponse(Room room);

}
