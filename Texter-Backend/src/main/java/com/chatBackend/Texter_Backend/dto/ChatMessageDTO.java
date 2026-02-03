package com.chatBackend.Texter_Backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDTO {

    private String roomCode;
    private String sender;
    private String content;
}
