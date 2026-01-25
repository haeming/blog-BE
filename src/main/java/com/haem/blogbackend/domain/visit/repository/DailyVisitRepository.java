package com.haem.blogbackend.domain.visit.repository;

import com.haem.blogbackend.domain.visit.entity.DailyVisit;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface DailyVisitRepository extends JpaRepository<DailyVisit, Long> {
    @Query(
            value = """
        INSERT INTO daily_visit (visit_date, ip_hash, user_agent)
        VALUES (:date, :ipHash, :ua)
        ON CONFLICT (visit_date, ip_hash) DO NOTHING
      """,
            nativeQuery = true
    )
    @Modifying
    int insertIgnore(
            @Param("date") LocalDate date,
            @Param("ipHash") String ipHash,
            @Param("ua") String ua
    );

    long countByVisitDate(LocalDate date);
}
