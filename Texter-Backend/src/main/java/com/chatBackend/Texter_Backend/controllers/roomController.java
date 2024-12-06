package com.chatBackend.Texter_Backend.controllers;

import com.chatBackend.Texter_Backend.entities.Room;
import com.chatBackend.Texter_Backend.services.roomServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/texter/rooms")
public class roomController {

	private final roomServices roomServices;

	public roomController(roomServices roomServices) {
		this.roomServices = roomServices;
	}

	@PostMapping("/createRoom")
	public ResponseEntity<?> createRoom(@RequestBody String roomName) {
		return roomServices.createRoom(roomName);
	}

	@GetMapping("/{roomId}")
	public ResponseEntity<?> getRoom(@PathVariable String roomId) {
		return roomServices.getRoom(roomId);
	}

	@GetMapping("/{roomId}/messages")
	public ResponseEntity<?> getMessages(@PathVariable String roomId) {
		return roomServices.getMessages(roomId);
	}
}
