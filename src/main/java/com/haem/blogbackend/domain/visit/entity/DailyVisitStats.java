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
public class DailyVisitStats {

    @Id
    @Column(name = "visit_date")
    private LocalDate visitDate;

    @Column(name = "unique_visitors", nullable = false)
    private long uniqueVisitors;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 기본 생성자 (JPA 전용)
    protected DailyVisitStats() {
    }

    // 생성자
    private DailyVisitStats(LocalDate visitDate, long uniqueVisitors) {
        this.visitDate = visitDate;
        this.uniqueVisitors = uniqueVisitors;
        this.updatedAt = LocalDateTime.now();
    }

    // ---------- Getter ----------

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public long getUniqueVisitors() {
        return uniqueVisitors;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ---------- Factory Method ----------

    public static DailyVisitStats create(LocalDate visitDate, long uniqueVisitors) {
        return new DailyVisitStats(visitDate, uniqueVisitors);
    }

    // ---------- Business Method ----------

    public void updateCount(long count) {
        this.uniqueVisitors = count;
        this.updatedAt = LocalDateTime.now();
    }
}
