package com.chatBackend.Texter_Backend.services;

import com.chatBackend.Texter_Backend.entities.RoomMember;
import com.chatBackend.Texter_Backend.repositories.RoomMemberRepository;
import com.chatBackend.Texter_Backend.repositories.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomPresenceService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository memberRepository;
    private final MessageService messageService;

    public RoomPresenceService(RoomRepository roomRepository, RoomMemberRepository memberRepository, MessageService messageService) {
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
        this.messageService = messageService;
    }

    public void leaveRoom(String roomCode, String sessionId) {

        RoomMember member = memberRepository.findByRoomCode(roomCode).stream()
                .filter(m -> m.getSessionId().equals(sessionId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not in room"));

        memberRepository.delete(member);

        messageService.system(roomCode, member.getNickname() + " left the room");

        long remaining = memberRepository.countByRoomCode(roomCode);
        if (remaining == 0) {
            roomRepository.findByRoomCode(roomCode)
                    .ifPresent(room -> {
                        messageService.system(roomCode, "Room closed");
                        roomRepository.delete(room);
                    });
        }
    }

    public List<String> getOnlineUsers(String roomCode) {
        return memberRepository.findByRoomCode(roomCode)
                .stream()
                .map(RoomMember::getNickname)
                .toList();
    }
}

