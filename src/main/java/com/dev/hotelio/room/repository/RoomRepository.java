package com.dev.hotelio.room.repository;

import com.dev.hotelio.room.domain.Room;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository {
    Room save(Room room);

    Optional<Room> findById(UUID id);

    List<Room> findByHotelId(UUID hotelId);
}
