package com.chatBackend.Texter_Backend.services;

import com.chatBackend.Texter_Backend.entities.JoinRequest;
import com.chatBackend.Texter_Backend.entities.Room;
import com.chatBackend.Texter_Backend.entities.RoomMember;
import com.chatBackend.Texter_Backend.repositories.JoinRequestRepository;
import com.chatBackend.Texter_Backend.repositories.RoomMemberRepository;
import com.chatBackend.Texter_Backend.repositories.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RoomAdminService {

    private final RoomRepository roomRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final RoomMemberRepository memberRepository;
    private final MessageService messageService;

    public RoomAdminService(
            RoomRepository roomRepository,
            JoinRequestRepository joinRequestRepository,
            RoomMemberRepository memberRepository,
            MessageService messageService
    ) {
        this.roomRepository = roomRepository;
        this.joinRequestRepository = joinRequestRepository;
        this.memberRepository = memberRepository;
        this.messageService = messageService;
    }

    public void approveJoin(
            String roomCode,
            String adminSessionId,
            String targetSessionId
    ) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!adminSessionId.equals(room.getAdminSessionId())) {
            throw new RuntimeException("Not admin");
        }

        JoinRequest req = joinRequestRepository.findByRoomCode(roomCode)
                .stream()
                .filter(r -> r.getSessionId().equals(targetSessionId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Join request not found"));

        memberRepository.save(
                RoomMember.builder()
                        .roomCode(roomCode)
                        .sessionId(req.getSessionId())
                        .nickname(req.getNickname())
                        .admin(false)
                        .joinedAt(Instant.now())
                        .build()
        );

        joinRequestRepository.delete(req);

        messageService.system(roomCode, req.getNickname() + " joined the room");
    }

    public void kickUser(
            String roomCode,
            String adminSessionId,
            String username
    ) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!room.getAdminSessionId().equals(adminSessionId)) {
            throw new RuntimeException("Not admin");
        }

        memberRepository.findByRoomCode(roomCode).stream()
                .filter(m -> m.getNickname().equalsIgnoreCase(username))
                .findFirst()
                .ifPresentOrElse(
                        member -> {
                            memberRepository.delete(member);
                            messageService.system(roomCode, member.getNickname() + " was kicked by admin");
                        },
                        () -> { throw new RuntimeException("User not found"); }
                );
    }
}
