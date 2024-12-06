package com.chatBackend.Texter_Backend.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "rooms")
public class Room {
	@Id
	private String id;
	private String roomName;
	private List<Messages> messages = new ArrayList<>();


	public Room() {
	}


	public Room(String id, String roomName, List<Messages> messages) {
		this.id = id;
		this.roomName = roomName;
		this.messages = messages;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getRoomName() {
		return roomName;
	}


	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}


	public List<Messages> getMessages() {
		return messages;
	}


	public void setMessages(List<Messages> messages) {
		this.messages = messages;
	}
}
