package com.haem.blogbackend.common.web.filter;

import com.haem.blogbackend.domain.visit.service.VisitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDate;

public class VisitTrackingFilter extends OncePerRequestFilter {
    private final VisitService visitService;

    public VisitTrackingFilter(VisitService visitService) {
        this.visitService = visitService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // 일반적으로 방문 집계는 GET 페이지성 요청에만 적용 권장
        if (!"GET".equalsIgnoreCase(method)) return true;

        // 제외: 관리자 API
        if (uri.startsWith("/api/admin")) return true;

        // 제외: 스웨거/헬스체크/정적리소스 등 (프로젝트에 맞게 조정)
        if (uri.startsWith("/swagger") || uri.startsWith("/v3/api-docs")) return true;
        if (uri.startsWith("/actuator")) return true;
        if (uri.startsWith("/favicon.ico")) return true;

        // 정적 파일 확장자 제외 (필요 시 확장)
        if (uri.matches(".*\\.(css|js|map|png|jpg|jpeg|gif|svg|ico|woff|woff2|ttf)$")) return true;

        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String clientIp = resolveClientIp(request);
            String userAgent = request.getHeader("User-Agent");

            // 개인정보 보호: ip 자체를 저장하지 않고, 서비스에서 해시 처리/저장한다고 가정
            visitService.trackVisit(LocalDate.now(), clientIp, userAgent);

        } catch (Exception e) {
            // 방문 집계 실패가 전체 요청을 깨면 안 됩니다.
            // 로그만 남기고 서비스 정상 처리 계속.
            // log.warn("visit tracking failed", e);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveClientIp(HttpServletRequest request) {
        // 프록시/로드밸런서 환경 고려: X-Forwarded-For 우선
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            // XFF는 "client, proxy1, proxy2" 형태일 수 있음 → 첫 번째가 원 IP인 경우가 일반적
            return xff.split(",")[0].trim();
        }

        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }

        return request.getRemoteAddr();
    }

}
