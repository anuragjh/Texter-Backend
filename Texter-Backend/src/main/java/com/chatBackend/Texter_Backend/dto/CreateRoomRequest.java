package com.chatBackend.Texter_Backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomRequest {

    private String roomName;

    private boolean isPrivate;

    private String adminUsername;
}
