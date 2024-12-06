package com.chatBackend.Texter_Backend.repositories;

import com.chatBackend.Texter_Backend.entities.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface roomRepo extends MongoRepository<Room,String> {
	public Room findByRoomName(String roomName);
}
