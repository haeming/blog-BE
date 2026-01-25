package com.haem.blogbackend.visit.application;

import com.haem.blogbackend.visit.domain.DailyVisitStats;
import com.haem.blogbackend.visit.domain.DailyVisitRepository;
import com.haem.blogbackend.visit.domain.DailyVisitStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;

@Service
public class VisitService {
    private final DailyVisitRepository dailyVisitRepository;
    private final DailyVisitStatsRepository dailyVisitStatsRepository;

    public VisitService(DailyVisitRepository dailyVisitRepository, DailyVisitStatsRepository dailyVisitStatsRepository){
        this.dailyVisitRepository = dailyVisitRepository;
        this.dailyVisitStatsRepository = dailyVisitStatsRepository;
    }

    // 배치/복구 전용
    @Transactional
    public void recalculateDailyStats(LocalDate date) {
        long count = dailyVisitRepository.countByVisitDate(date);

        DailyVisitStats stats = dailyVisitStatsRepository
                .findById(date)
                .orElseGet(() -> DailyVisitStats.create(date, 0));

        stats.updateCount(count);
        dailyVisitStatsRepository.save(stats);
    }

    // 메인 표시용 조회
    @Transactional(readOnly = true)
    public long getTodayUniqueVisitors(LocalDate date) {
        return dailyVisitStatsRepository.findById(date)
                .map(DailyVisitStats::getUniqueVisitors)
                .orElse(0L);
    }

    @Transactional
    public void trackVisit(LocalDate date, String rawIp, String userAgent) {
        String ipHash = sha256(rawIp);

        int inserted = dailyVisitRepository.insertIgnore(date, ipHash, userAgent);

        // 신규 방문(오늘 해당 ipHash 첫 방문)일 때만 집계 갱신
        if (inserted > 0) {
            DailyVisitStats stats = dailyVisitStatsRepository
                    .findById(date)
                    .orElseGet(() -> DailyVisitStats.create(date, 0));

            stats.updateCount(stats.getUniqueVisitors() + 1);
            dailyVisitStatsRepository.save(stats);
        }
    }

    private String sha256(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dig = md.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(dig.length * 2);
            for (byte b : dig) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("ip hash failed", e);
        }
    }
}
