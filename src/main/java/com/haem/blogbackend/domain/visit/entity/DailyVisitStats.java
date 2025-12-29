package com.haem.blogbackend.domain.visit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_visit_stats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyVisitStats {

    @Id
    @Column(name = "visit_date")
    private LocalDate visitDate;

    @Column(name = "unique_visitors", nullable = false)
    private long uniqueVisitors;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void updateCount(long count) {
        this.uniqueVisitors = count;
        this.updatedAt = LocalDateTime.now();
    }
}
