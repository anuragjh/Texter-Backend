package com.chatBackend.Texter_Backend.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "room_members")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomMember {

    @Id
    private String id;

    @Indexed
    private String roomCode;

    @Indexed
    private String sessionId;

    private String nickname;

    private boolean admin;

    private Instant joinedAt;

}
