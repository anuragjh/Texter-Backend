package com.chatBackend.Texter_Backend.entities;

import java.time.LocalDateTime;

public class Messages {
	private String message;
	private String sender;
	private LocalDateTime timeStamp;


	public Messages() {
	}


	public Messages(String message, String sender, LocalDateTime timeStamp) {
		this.message = message;
		this.sender = sender;
		this.timeStamp = timeStamp;
	}


	public Messages(String message, String sender) {
		this.message = message;
		this.sender = sender;
		this.timeStamp = LocalDateTime.now();
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getSender() {
		return sender;
	}


	public void setSender(String sender) {
		this.sender = sender;
	}


	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}


	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
}
