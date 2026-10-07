package com.hotelio.room.infrastructure;

import com.hotelio.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaRoomRepository extends JpaRepository<Room, UUID> {

    List<Room> findByHotelId(UUID hotelId);
}
