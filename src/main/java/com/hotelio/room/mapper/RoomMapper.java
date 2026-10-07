package com.hotelio.room.mapper;

import com.hotelio.room.dto.RoomResponse;
import com.hotelio.room.domain.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "hotelId", source = "hotel.id")
    RoomResponse toRoomResponse(Room room);

    @Mapping(target = "hotel.id", source = "hotelId")
    Room toRoom(RoomResponse roomResponse);
}
