package com.hotelio.room.controller;

import com.hotelio.room.dto.CreateRoomRequest;
import com.hotelio.room.dto.RoomResponse;
import com.hotelio.room.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/hotels/{hotelId}/rooms")
    public ResponseEntity<RoomResponse> createRoom(@PathVariable UUID hotelId, @Valid @RequestBody CreateRoomRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roomService.createRoom(hotelId, request));
    }

    @GetMapping("/hotels/{hotelId}/rooms")
    public ResponseEntity<List<RoomResponse>> getHotelRooms(@PathVariable UUID hotelId) {

        return ResponseEntity.ok(roomService.getHotelRooms(hotelId));
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<RoomResponse> getRoom(@PathVariable UUID roomId) {

        return ResponseEntity.ok(roomService.getRoom(roomId));
    }
}