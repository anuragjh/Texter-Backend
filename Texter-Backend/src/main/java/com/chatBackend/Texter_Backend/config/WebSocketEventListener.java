package com.chatBackend.Texter_Backend.config;

import com.chatBackend.Texter_Backend.services.RoomCleanupService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Objects;

@Component
public class WebSocketEventListener {

    private final RoomCleanupService cleanupService;

    public WebSocketEventListener(RoomCleanupService cleanupService) {
        this.cleanupService = cleanupService;
    }

    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String roomCode = accessor.getFirstNativeHeader("roomCode");
        if (roomCode != null) {
            Objects.requireNonNull(accessor.getSessionAttributes()).put("roomCode", roomCode);
        }
    }

    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();

        if (destination != null && destination.startsWith("/topic/room/")) {
            String roomCode = destination.substring("/topic/room/".length());
            Objects.requireNonNull(accessor.getSessionAttributes()).put("roomCode", roomCode);
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String roomCode = (String) Objects.requireNonNull(accessor.getSessionAttributes()).get("roomCode");
        String sessionId = accessor.getSessionId();

        if (roomCode != null && sessionId != null) {
            cleanupService.handleDisconnect(roomCode, sessionId);
        }
    }
}
