package com.chatBackend.Texter_Backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRoomRequest {

    private String roomCode;

    private String username;
}
