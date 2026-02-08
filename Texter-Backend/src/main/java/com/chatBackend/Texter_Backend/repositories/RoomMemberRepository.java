package com.chatBackend.Texter_Backend.repositories;

import com.chatBackend.Texter_Backend.entities.RoomMember;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RoomMemberRepository
        extends MongoRepository<RoomMember, String> {

    List<RoomMember> findByRoomCode(String roomCode);

    long countByRoomCode(String roomCode);

    void deleteByRoomCodeAndSessionId(String roomCode, String sessionId);

    void deleteByRoomCode(String roomCode);
}
