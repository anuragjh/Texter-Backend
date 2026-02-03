package com.chatBackend.Texter_Backend.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "join_requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {

    @Id
    private String id;

    private String roomCode;

    private String sessionId;

    private String nickname;

    private Instant requestedAt;
}
