package com.chatBackend.Texter_Backend.services;

import com.chatBackend.Texter_Backend.entities.Room;
import com.chatBackend.Texter_Backend.entities.RoomMember;
import com.chatBackend.Texter_Backend.repositories.RoomMemberRepository;
import com.chatBackend.Texter_Backend.repositories.RoomRepository;
import com.chatBackend.Texter_Backend.utils.RoomCodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository memberRepository;
    private final MessageService messageService;

    public RoomService(RoomRepository roomRepository, RoomMemberRepository memberRepository, MessageService messageService) {
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
        this.messageService = messageService;
    }

    @Transactional
    public Room createRoom(String roomName, boolean isPrivate, String adminUsername, String sessionId) {

        if (adminUsername == null || adminUsername.isBlank()) {
            throw new IllegalArgumentException("Admin username required");
        }

        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("Session ID required");
        }

        String roomCode;
        do {
            roomCode = RoomCodeGenerator.generate(5);
        } while (roomRepository.existsByRoomCode(roomCode));

        Room room = Room.builder()
                .roomCode(roomCode)
                .roomName(roomName == null || roomName.isBlank() ? "Chat Room" : roomName.trim())
                .isPrivate(isPrivate)
                .adminSessionId(sessionId)
                .active(true)
                .maxParticipants(50)
                .createdAt(Instant.now())
                .expiresAt(null)
                .build();

        Room savedRoom = roomRepository.save(room);

        RoomMember adminMember = RoomMember.builder()
                .roomCode(roomCode)
                .sessionId(sessionId)
                .nickname(adminUsername.trim())
                .admin(true)
                .joinedAt(Instant.now())
                .build();

        memberRepository.save(adminMember);

        return savedRoom;
    }

    public void destroyRoom(String roomCode) {
        roomRepository.findByRoomCode(roomCode)
                .ifPresent(room -> {
                    messageService.system(roomCode, "Room was destroyed by admin");
                    roomRepository.delete(room);
                });
    }
}
