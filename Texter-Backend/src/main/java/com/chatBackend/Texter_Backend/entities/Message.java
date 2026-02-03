package com.chatBackend.Texter_Backend.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "messages")
public class Message {

    @Id
    private String id;

    @Indexed
    private String roomCode;

    private String content;

    private String senderId;

    private String senderName;

    private MessageType type;

    @Indexed
    private Instant createdAt;

    private boolean edited;

    private boolean deleted;
}
