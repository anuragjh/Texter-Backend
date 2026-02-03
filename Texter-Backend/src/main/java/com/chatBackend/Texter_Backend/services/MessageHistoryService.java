package com.chatBackend.Texter_Backend.services;

import com.chatBackend.Texter_Backend.entities.Message;
import com.chatBackend.Texter_Backend.repositories.MessageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageHistoryService {

    private final MessageRepository messageRepository;

    public MessageHistoryService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getLastMessages(String roomCode, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return messageRepository.findByRoomCodeOrderByCreatedAtDesc(roomCode, pageable);
    }
}
