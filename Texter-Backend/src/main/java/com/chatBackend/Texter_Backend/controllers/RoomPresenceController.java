package com.chatBackend.Texter_Backend.controllers;

import com.chatBackend.Texter_Backend.services.RoomPresenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomPresenceController {

    private final RoomPresenceService presenceService;

    public RoomPresenceController(RoomPresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @PostMapping("/leave")
    public ResponseEntity<Void> leaveRoom(
            @RequestParam String roomCode,
            @RequestHeader("X-Session-Id") String sessionId
    ) {
        presenceService.leaveRoom(roomCode, sessionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roomCode}/online")
    public ResponseEntity<List<String>> getOnlineUsers(@PathVariable String roomCode) {
        return ResponseEntity.ok(presenceService.getOnlineUsers(roomCode));
    }
}

