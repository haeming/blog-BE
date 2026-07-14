package com.haem.blogbackend.auth.application;

import com.haem.blogbackend.auth.domain.LoginRateLimitExceededException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimitService {

    private static final int MAX_REQUEST_COUNT = 5;
    private static final long WINDOW_SECONDS = 300;

    private final Map<String, Deque<LocalDateTime>> requestHistoryByIp = new ConcurrentHashMap<>();

    public void checkAllowed(String ipAddress) {
        LocalDateTime threshold = LocalDateTime.now().minusSeconds(WINDOW_SECONDS);

        Deque<LocalDateTime> requestTimes = requestHistoryByIp.computeIfAbsent(ipAddress, key -> new ArrayDeque<>());

        synchronized (requestTimes) {
            while (!requestTimes.isEmpty() && requestTimes.peekFirst().isBefore(threshold)) {
                requestTimes.pollFirst();
            }

            if (requestTimes.size() >= MAX_REQUEST_COUNT) {
                throw new LoginRateLimitExceededException();
            }
        }
    }

    public void recordFailure(String ipAddress) {
        Deque<LocalDateTime> requestTimes = requestHistoryByIp.computeIfAbsent(ipAddress, key -> new ArrayDeque<>());

        synchronized (requestTimes) {
            requestTimes.addLast(LocalDateTime.now());
        }
    }
}
