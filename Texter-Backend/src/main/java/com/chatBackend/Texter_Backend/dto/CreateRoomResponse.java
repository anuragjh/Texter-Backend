package com.chatBackend.Texter_Backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateRoomResponse {

    private String roomCode;

    private String roomName;

    private boolean isPrivate;

    private String adminUsername;
}
