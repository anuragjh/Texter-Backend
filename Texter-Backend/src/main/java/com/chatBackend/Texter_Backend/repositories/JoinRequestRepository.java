package com.chatBackend.Texter_Backend.repositories;

import com.chatBackend.Texter_Backend.entities.JoinRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JoinRequestRepository
        extends MongoRepository<JoinRequest, String> {

    List<JoinRequest> findByRoomCode(String roomCode);

    void deleteByRoomCode(String roomCode);
}
