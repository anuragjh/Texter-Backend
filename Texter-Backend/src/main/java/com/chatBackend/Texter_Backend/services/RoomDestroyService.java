package com.chatBackend.Texter_Backend.services;

import com.chatBackend.Texter_Backend.entities.Room;
import com.chatBackend.Texter_Backend.repositories.JoinRequestRepository;
import com.chatBackend.Texter_Backend.repositories.MessageRepository;
import com.chatBackend.Texter_Backend.repositories.RoomMemberRepository;
import com.chatBackend.Texter_Backend.repositories.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomDestroyService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository memberRepository;
    private final JoinRequestRepository joinRequestRepository;
    private final MessageRepository messageRepository;
    private final MessageService messageService;

    public RoomDestroyService(
            RoomRepository roomRepository,
            RoomMemberRepository memberRepository,
            JoinRequestRepository joinRequestRepository,
            MessageRepository messageRepository,
            MessageService messageService
    ) {
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
        this.joinRequestRepository = joinRequestRepository;
        this.messageRepository = messageRepository;
        this.messageService = messageService;
    }

    @Transactional
    public void destroyRoom(String roomCode, String adminSessionId) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!adminSessionId.equals(room.getAdminSessionId())) {
            throw new RuntimeException("Not admin");
        }

        messageService.system(roomCode, "Room destroyed by admin");

        memberRepository.deleteByRoomCode(roomCode);
        joinRequestRepository.deleteByRoomCode(roomCode);
        messageRepository.deleteByRoomCode(roomCode);
        roomRepository.delete(room);
    }
}
