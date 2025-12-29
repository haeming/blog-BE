package com.haem.blogbackend.domain.visit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table (name = "daily_visit",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"visit_date", "ip_hash"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Column(name = "ip_hash", nullable = false, length = 64)
    private String ipHash;

    @Column(name = "first_seen_at", nullable = false, updatable = false)
    private LocalDateTime firstSeenAt;

    @Column(name = "user_agent")
    private String userAgent;

    // 기본 생성자
    protected DailyVisit() {
    }

    // 생성자
    private DailyVisit(LocalDate visitDate, String ipHash, String userAgent) {
        this.visitDate = visitDate;
        this.ipHash = ipHash;
        this.userAgent = userAgent;
    }

    // 생명주기 콜백
    @PrePersist
    protected void onCreate() {
        this.firstSeenAt = LocalDateTime.now();
    }

    // ---------- Getter ----------

    public Long getId() {
        return id;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public String getIpHash() {
        return ipHash;
    }

    public LocalDateTime getFirstSeenAt() {
        return firstSeenAt;
    }

    public String getUserAgent() {
        return userAgent;
    }

    // ---------- Factory Method ----------

    public static DailyVisit create(LocalDate visitDate, String ipHash, String userAgent) {
        return new DailyVisit(visitDate, ipHash, userAgent);
    }
}
