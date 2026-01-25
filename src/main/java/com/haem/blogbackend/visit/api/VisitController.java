package com.haem.blogbackend.visit.api;

import com.haem.blogbackend.visit.application.VisitService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/visits")
public class VisitController {
    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping("/today")
    public long getTodayUniqueVisitors() {
        return visitService.getTodayUniqueVisitors(LocalDate.now());
    }

    @PostMapping("/ping")
    public void ping(HttpServletRequest request) {
        System.out.println("VISIT PING CALLED");
        LocalDate today = LocalDate.now();
        String ip = extractClientIp(request);
        String ua = request.getHeader("User-Agent");
        visitService.trackVisit(today, ip, ua);
    }

    private String extractClientIp(HttpServletRequest request) {
        // Nginx/프록시 환경 대응
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) {
            return xRealIp.trim();
        }
        return request.getRemoteAddr();
    }
}
