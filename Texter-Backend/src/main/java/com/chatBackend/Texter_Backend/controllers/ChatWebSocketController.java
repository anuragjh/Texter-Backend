package com.chatBackend.Texter_Backend.controllers;

import com.chatBackend.Texter_Backend.dto.ChatMessageDTO;
import com.chatBackend.Texter_Backend.services.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class ChatWebSocketController {

    private final MessageService messageService;

    public ChatWebSocketController(MessageService messageService) {
        this.messageService = messageService;
    }

  @MessageMapping("/chat.send")
public void sendMessage(
        @Payload ChatMessageDTO messageDTO,
        SimpMessageHeaderAccessor headerAccessor
) {
    String sessionId = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("sessionId");

    if (sessionId == null) throw new RuntimeException("Unauthorized session");

    messageService.sendChatMessage(messageDTO, sessionId);
}
}
