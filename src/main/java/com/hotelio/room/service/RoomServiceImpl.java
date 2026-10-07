package com.hotelio.room.service;

import com.hotelio.room.dto.CreateRoomRequest;
import com.hotelio.room.dto.RoomResponse;
import com.hotelio.common.exception.ResourceNotFoundException;
import com.hotelio.room.mapper.RoomMapper;
import com.hotelio.hotel.domain.Hotel;
import com.hotelio.hotel.repository.HotelRepository;
import com.hotelio.room.domain.Room;
import com.hotelio.room.repository.RoomRepository;
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
