package com.hotelio.room.service;

import com.hotelio.room.dto.CreateRoomRequest;
import com.hotelio.room.dto.RoomResponse;

import java.util.List;
import java.util.UUID;

public interface RoomService {

    RoomResponse createRoom(UUID hotelId, CreateRoomRequest request);

    List<RoomResponse> getHotelRooms(UUID hotelId);

    RoomResponse getRoom(UUID roomId);
}
