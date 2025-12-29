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

    @PrePersist
    void onCreate() {
        this.firstSeenAt = LocalDateTime.now();
    }

    public static DailyVisit of(LocalDate date, String ipHash, String ua) {
        DailyVisit v = new DailyVisit();
        v.visitDate = date;
        v.ipHash = ipHash;
        v.userAgent = ua;
        return v;
    }
}
