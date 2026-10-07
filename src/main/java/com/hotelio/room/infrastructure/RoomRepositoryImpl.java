package com.hotelio.room.infrastructure;

import com.hotelio.room.domain.Room;
import com.hotelio.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {

    private final JpaRoomRepository repository;

    @Override
    public Room save(Room room) {
        return repository.save(room);
    }

    @Override
    public Optional<Room> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<Room> findByHotelId(UUID hotelId) {
        return repository.findByHotelId(hotelId);
    }

}
