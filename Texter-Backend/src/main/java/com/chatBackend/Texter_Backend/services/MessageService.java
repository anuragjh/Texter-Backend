package com.chatBackend.Texter_Backend.services;

import com.chatBackend.Texter_Backend.dto.ChatMessageDTO;
import com.chatBackend.Texter_Backend.entities.Message;
import com.chatBackend.Texter_Backend.entities.MessageType;
import com.chatBackend.Texter_Backend.repositories.MessageRepository;
import com.chatBackend.Texter_Backend.repositories.RoomMemberRepository;
import com.chatBackend.Texter_Backend.repositories.RoomRepository;
import com.chatBackend.Texter_Backend.utils.RateLimiter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final RoomMemberRepository memberRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageService(
            MessageRepository messageRepository,
            RoomRepository roomRepository,
            RoomMemberRepository memberRepository,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
        this.messagingTemplate = messagingTemplate;
    }



    public Message sendChatMessage(
            ChatMessageDTO dto,
            String sessionId
    ) {

        if (!RateLimiter.allow(sessionId)) {
            throw new RuntimeException("Too many messages. Slow down.");
        }


        roomRepository.findByRoomCode(dto.getRoomCode())
                .orElseThrow(() -> new RuntimeException("Room not found"));


        boolean isMember = memberRepository
                .findByRoomCode(dto.getRoomCode())
                .stream()
                .anyMatch(m -> m.getSessionId().equals(sessionId));

        if (!isMember) {
            throw new RuntimeException("Not a room member");
        }

        Message message = Message.builder()
                .roomCode(dto.getRoomCode())
                .senderId(sessionId)
                .senderName(dto.getSender())
                .content(dto.getContent())
                .type(MessageType.CHAT)
                .createdAt(Instant.now())
                .edited(false)
                .deleted(false)
                .build();

        Message saved = messageRepository.save(message);


        messagingTemplate.convertAndSend(
                "/topic/room/" + dto.getRoomCode(),
                saved
        );

        return saved;
    }



    public void broadcastSystemMessage(
            String roomCode,
            String content
    ) {
        Message message = Message.builder()
                .roomCode(roomCode)
                .senderName("SYSTEM")
                .content(content)
                .type(MessageType.SYSTEM)
                .createdAt(Instant.now())
                .edited(false)
                .deleted(false)
                .build();

        Message saved = messageRepository.save(message);

        messagingTemplate.convertAndSend(
                "/topic/room/" + roomCode,
                saved
        );
    }
}
