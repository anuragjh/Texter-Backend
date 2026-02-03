package com.chatBackend.Texter_Backend.controllers;

import com.chatBackend.Texter_Backend.dto.CreateRoomRequest;
import com.chatBackend.Texter_Backend.dto.CreateRoomResponse;
import com.chatBackend.Texter_Backend.entities.Room;
import com.chatBackend.Texter_Backend.services.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

  @PostMapping
public ResponseEntity<CreateRoomResponse> createRoom(
        @RequestBody CreateRoomRequest request,
        @RequestHeader("X-Session-Id") String sessionId
) {
    Room room = roomService.createRoom(
            request.getRoomName(),
            request.isPrivate(),
            request.getAdminUsername(),
            sessionId
    );

    return ResponseEntity.ok(
            CreateRoomResponse.builder()
                    .roomCode(room.getRoomCode())
                    .roomName(room.getRoomName())
                    .isPrivate(room.isPrivate())
                    .adminUsername(request.getAdminUsername())
                    .build()
    );
}
}
