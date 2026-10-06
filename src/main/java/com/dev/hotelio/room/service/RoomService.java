package com.dev.hotelio.room.service;

import com.dev.hotelio.common.dto.room.CreateRoomRequest;
import com.dev.hotelio.common.dto.room.RoomResponse;

import java.util.List;
import java.util.UUID;

public interface RoomService {

    RoomResponse createRoom(UUID hotelId, CreateRoomRequest request);

    List<RoomResponse> getHotelRooms(UUID hotelId);

    RoomResponse getRoom(UUID roomId);
}
