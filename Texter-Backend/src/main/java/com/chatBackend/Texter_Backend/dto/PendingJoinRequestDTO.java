package com.chatBackend.Texter_Backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class PendingJoinRequestDTO {

    private String sessionId;
    private String nickname;
    private Instant requestedAt;
}
