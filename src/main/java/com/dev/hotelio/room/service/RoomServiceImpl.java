package com.dev.hotelio.room.service;

import com.dev.hotelio.common.dto.room.CreateRoomRequest;
import com.dev.hotelio.common.dto.room.RoomResponse;
import com.dev.hotelio.common.exception.ResourceNotFoundException;
import com.dev.hotelio.common.mapper.RoomMapper;
import com.dev.hotelio.hotel.domain.Hotel;
import com.dev.hotelio.hotel.repository.HotelRepository;
import com.dev.hotelio.room.domain.Room;
import com.dev.hotelio.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional
    public RoomResponse createRoom(UUID hotelId, CreateRoomRequest request) {

        Hotel hotel = findHotelById(hotelId);

        Room room = Room.createNew(
                hotel,
                request.type(),
                request.capacity(),
                request.bedCount(),
                request.pricePerNight());

        Room savedRoom = roomRepository.save(room);

        return roomMapper.toRoomResponse(savedRoom);
    }

    @Override
    public List<RoomResponse> getHotelRooms(UUID hotelId) {

        findHotelById(hotelId);

        return roomRepository.findByHotelId(hotelId)
                .stream()
                .map(roomMapper::toRoomResponse)
                .toList();
    }

    @Override
    public RoomResponse getRoom(UUID roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room with id '%s' was not found".formatted(roomId)));
        return roomMapper.toRoomResponse(room);
    }

    private Hotel findHotelById(UUID hotelId) {
        return hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found: " + hotelId));
    }


}
