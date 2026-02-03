package com.chatBackend.Texter_Backend.utils;

import java.time.Instant;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class RateLimiter {

    private static final int MAX_MESSAGES = 10;
    private static final long WINDOW_MS = 10_000;

    private static final Map<String, Deque<Long>> userTimestamps = new ConcurrentHashMap<>();

    public static boolean allow(String sessionId) {

        long now = Instant.now().toEpochMilli();

        userTimestamps.putIfAbsent(sessionId, new ConcurrentLinkedDeque<>());

        Deque<Long> timestamps = userTimestamps.get(sessionId);

        while (!timestamps.isEmpty() && now - timestamps.peekFirst() > WINDOW_MS) {
            timestamps.pollFirst();
        }

        if (timestamps.size() >= MAX_MESSAGES) {
            return false;
        }

        timestamps.addLast(now);
        return true;
    }
}
