package com.chatBackend.Texter_Backend.controllers;

import com.chatBackend.Texter_Backend.services.RoomDestroyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms/admin")
public class RoomDestroyController {

    private final RoomDestroyService destroyService;

    public RoomDestroyController(RoomDestroyService destroyService) {
        this.destroyService = destroyService;
    }

    @PostMapping("/destroy")
    public ResponseEntity<Void> destroyRoom(
            @RequestParam String roomCode,
            @RequestHeader("X-Session-Id") String adminSessionId
    ) {
        destroyService.destroyRoom(roomCode, adminSessionId);
        return ResponseEntity.ok().build();
    }
}
