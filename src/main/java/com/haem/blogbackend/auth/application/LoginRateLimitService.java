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

        requestHistoryByIp.compute(ipAddress, (key, requestTimes) -> {
            Deque<LocalDateTime> times = requestTimes != null ? requestTimes : new ArrayDeque<>();
            prune(times, threshold);

            if (times.size() >= MAX_REQUEST_COUNT) {
                throw new LoginRateLimitExceededException();
            }

            // 빈 기록은 Map에 남기지 않는다
            return times.isEmpty() ? null : times;
        });
    }

    public void recordFailure(String ipAddress) {
        LocalDateTime threshold = LocalDateTime.now().minusSeconds(WINDOW_SECONDS);

        requestHistoryByIp.compute(ipAddress, (key, requestTimes) -> {
            Deque<LocalDateTime> times = requestTimes != null ? requestTimes : new ArrayDeque<>();
            prune(times, threshold);
            times.addLast(LocalDateTime.now());
            return times;
        });
    }

    private void prune(Deque<LocalDateTime> requestTimes, LocalDateTime threshold) {
        while (!requestTimes.isEmpty() && requestTimes.peekFirst().isBefore(threshold)) {
            requestTimes.pollFirst();
        }
    }
}
