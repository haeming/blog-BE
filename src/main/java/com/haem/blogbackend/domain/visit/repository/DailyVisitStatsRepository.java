package com.haem.blogbackend.domain.visit.repository;

import com.haem.blogbackend.domain.visit.entity.DailyVisitStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailyVisitStatsRepository extends JpaRepository<DailyVisitStats, LocalDate> {

}
