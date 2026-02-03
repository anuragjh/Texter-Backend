package com.chatBackend.Texter_Backend.services;

import com.chatBackend.Texter_Backend.repositories.RoomMemberRepository;
import com.chatBackend.Texter_Backend.repositories.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomCleanupService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository memberRepository;

	public RoomCleanupService(RoomRepository roomRepository, RoomMemberRepository memberRepository) {
		this.roomRepository = roomRepository;
		this.memberRepository = memberRepository;
	}

	public void handleDisconnect(String roomCode, String sessionId) {

        memberRepository.deleteByRoomCodeAndSessionId(roomCode, sessionId);

        long remaining = memberRepository.countByRoomCode(roomCode);
        if (remaining == 0) {
            roomRepository.findByRoomCode(roomCode)
                    .ifPresent(roomRepository::delete);
        }
    }
}

