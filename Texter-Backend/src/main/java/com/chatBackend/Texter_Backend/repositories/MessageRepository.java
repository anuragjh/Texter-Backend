package com.chatBackend.Texter_Backend.repositories;

import com.chatBackend.Texter_Backend.entities.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findByRoomCodeOrderByCreatedAtAsc(String roomCode);

    List<Message> findByRoomCodeOrderByCreatedAtDesc(
            String roomCode,
            Pageable pageable
    );
}
