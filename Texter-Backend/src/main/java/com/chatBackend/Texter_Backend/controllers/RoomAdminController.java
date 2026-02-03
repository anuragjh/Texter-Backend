package com.chatBackend.Texter_Backend.controllers;

import com.chatBackend.Texter_Backend.services.RoomAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms/admin")
public class RoomAdminController {

    private final RoomAdminService adminService;

    public RoomAdminController(RoomAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/approve")
    public ResponseEntity<Void> approveJoin(
            @RequestParam String roomCode,
            @RequestParam String targetSessionId,
            @RequestHeader("X-Session-Id") String adminSessionId
    ) {
        adminService.approveJoin(roomCode, adminSessionId, targetSessionId);
        return ResponseEntity.ok().build();
    }
	@PostMapping("/kick")
public ResponseEntity<Void> kickUser(
        @RequestParam String roomCode,
        @RequestParam String username,
        @RequestHeader("X-Session-Id") String adminSessionId
) {
    adminService.kickUser(roomCode, adminSessionId, username);
    return ResponseEntity.ok().build();
}

}
