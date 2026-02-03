package com.chatBackend.Texter_Backend.controllers;

import com.chatBackend.Texter_Backend.dto.JoinRoomRequest;
import com.chatBackend.Texter_Backend.dto.JoinRoomResponse;
import com.chatBackend.Texter_Backend.services.RoomJoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms/join")
public class RoomJoinController {

    private final RoomJoinService joinService;

    public RoomJoinController(RoomJoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping
    public ResponseEntity<JoinRoomResponse> joinRoom(
            @RequestBody JoinRoomRequest request,
            @RequestHeader("X-Session-Id") String sessionId
    ) {
        return ResponseEntity.ok(
                joinService.joinRoom(
                        request.getRoomCode(),
                        sessionId,
                        request.getUsername()
                )
        );
    }

}

