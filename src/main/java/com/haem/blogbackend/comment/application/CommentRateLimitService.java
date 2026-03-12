package com.haem.blogbackend.comment.application;

import com.haem.blogbackend.comment.domain.CommentRateLimitExceededException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CommentRateLimitService {

    private static final int MAX_REQUEST_COUNT = 3;
    private static final long WINDOW_SECONDS = 60;

    private final Map<String, Deque<LocalDateTime>> requestHistoryByIp = new ConcurrentHashMap<>();

    public void validate(String ipAddress) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.minusSeconds(WINDOW_SECONDS);

        Deque<LocalDateTime> requestTimes = requestHistoryByIp.computeIfAbsent(ipAddress, key -> new ArrayDeque<>());

        synchronized (requestTimes) {
            while (!requestTimes.isEmpty() && requestTimes.peekFirst().isBefore(threshold)) {
                requestTimes.pollFirst();
            }

            if (requestTimes.size() >= MAX_REQUEST_COUNT) {
                throw new CommentRateLimitExceededException();
            }

            requestTimes.addLast(now);
        }
    }
}
