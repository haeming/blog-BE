package com.haem.blogbackend.visit.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailyVisitStatsRepository extends JpaRepository<DailyVisitStats, LocalDate> {

}
