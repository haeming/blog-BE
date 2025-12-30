package com.haem.blogbackend.domain.visit.service;

import com.haem.blogbackend.domain.visit.entity.DailyVisitStats;
import com.haem.blogbackend.domain.visit.repository.DailyVisitRepository;
import com.haem.blogbackend.domain.visit.repository.DailyVisitStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class VisitService {
    private final DailyVisitRepository dailyVisitRepository;
    private final DailyVisitStatsRepository dailyVisitStatsRepository;

    public VisitService(DailyVisitRepository dailyVisitRepository, DailyVisitStatsRepository dailyVisitStatsRepository){
        this.dailyVisitRepository = dailyVisitRepository;
        this.dailyVisitStatsRepository = dailyVisitStatsRepository;
    }

    // 신규 방문시 호출 (집계 갱신)
    @Transactional
    public void aggregateToday(LocalDate date){
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
}
