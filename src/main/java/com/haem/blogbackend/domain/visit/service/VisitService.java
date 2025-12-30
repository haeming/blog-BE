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

    @Transactional
    public void aggregateToday(LocalDate date){
        long count = dailyVisitRepository.countByVisitDate(date);

        DailyVisitStats stats = dailyVisitStatsRepository
                .findById(date)
                .orElseGet(() -> DailyVisitStats.create(date, 0));

        stats.updateCount(count);
        dailyVisitStatsRepository.save(stats);
    }
}
