package com.chatBackend.Texter_Backend.services;

import com.chatBackend.Texter_Backend.dto.JoinRoomResponse;
import com.chatBackend.Texter_Backend.entities.JoinRequest;
import com.chatBackend.Texter_Backend.entities.Room;
import com.chatBackend.Texter_Backend.entities.RoomMember;
import com.chatBackend.Texter_Backend.repositories.JoinRequestRepository;
import com.chatBackend.Texter_Backend.repositories.RoomMemberRepository;
import com.chatBackend.Texter_Backend.repositories.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RoomJoinService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository memberRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final MessageService messageService;

    public RoomJoinService(
            RoomRepository roomRepository,
            RoomMemberRepository memberRepository,
            JoinRequestRepository joinRequestRepository,
            MessageService messageService
    ) {
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
        this.joinRequestRepository = joinRequestRepository;
        this.messageService = messageService;
    }

    public JoinRoomResponse joinRoom(
            String roomCode,
            String sessionId,
            String username
    ) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!room.isActive()) {
            throw new RuntimeException("Room closed");
        }

        boolean usernameTaken = memberRepository
                .findByRoomCode(roomCode)
                .stream()
                .anyMatch(m -> m.getNickname().equalsIgnoreCase(username));

        if (usernameTaken) {
            throw new RuntimeException("Username already taken");
        }

        if (room.isPrivate()) {
            joinRequestRepository.save(
                    JoinRequest.builder()
                            .roomCode(roomCode)
                            .sessionId(sessionId)
                            .nickname(username)
                            .requestedAt(Instant.now())
                            .build()
            );
            return JoinRoomResponse.builder().status("PENDING").build();
        }

        boolean isFirstUser = memberRepository.countByRoomCode(roomCode) == 0;

        memberRepository.save(
                RoomMember.builder()
                        .roomCode(roomCode)
                        .sessionId(sessionId)
                        .nickname(username)
                        .admin(isFirstUser)
                        .joinedAt(Instant.now())
                        .build()
        );

        if (isFirstUser) {
            room.setAdminSessionId(sessionId);
            roomRepository.save(room);
        }

        messageService.system(roomCode, username + " joined the room");

        return JoinRoomResponse.builder().status("JOINED").build();
    }
}
