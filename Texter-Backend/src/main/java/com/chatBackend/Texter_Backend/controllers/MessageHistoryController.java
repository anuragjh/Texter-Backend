package com.chatBackend.Texter_Backend.controllers;

import com.chatBackend.Texter_Backend.entities.Message;
import com.chatBackend.Texter_Backend.services.MessageHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms/messages")
public class MessageHistoryController {

    private final MessageHistoryService historyService;

    public MessageHistoryController(MessageHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/{roomCode}")
    public List<Message> getLastMessages(
            @PathVariable String roomCode,
            @RequestParam(defaultValue = "50") int limit
    ) {
        return historyService.getLastMessages(roomCode, Math.min(limit, 50));
    }
}
