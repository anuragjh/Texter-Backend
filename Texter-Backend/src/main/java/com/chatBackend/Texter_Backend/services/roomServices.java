package com.chatBackend.Texter_Backend.services;

import com.chatBackend.Texter_Backend.entities.Room;
import com.chatBackend.Texter_Backend.repositories.roomRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class roomServices {

	private final roomRepo roomRepo;

	public roomServices(roomRepo roomRepo) {
		this.roomRepo = roomRepo;
	}

	public ResponseEntity<?> createRoom(String roomName) {
		if (roomRepo.findByRoomName(roomName) != null) {
			return ResponseEntity.badRequest().body("Room already exists");
		}
		Room room = new Room(); // Changed to PascalCase
		room.setRoomName(roomName.trim().replaceAll("\"", ""));
		roomRepo.save(room);
		return ResponseEntity.ok(room);
	}

	public ResponseEntity<?> getRoom(String roomName) {
		Room room = roomRepo.findByRoomName(roomName);
		if (room == null) {
			return ResponseEntity.badRequest().body("Room does not exist");
		}
		return ResponseEntity.ok(room);
	}

	public ResponseEntity<?> getMessages(String roomName) {
		Room room = roomRepo.findByRoomName(roomName);
		if (room == null) {
			return ResponseEntity.badRequest().body("Room does not exist");
		}
		return ResponseEntity.ok(room.getMessages());
	}
}
