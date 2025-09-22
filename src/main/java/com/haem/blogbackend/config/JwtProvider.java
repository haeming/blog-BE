package com.haem.blogbackend.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.haem.blogbackend.domain.Admin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 생성 (subject = accountName)
    public String generateToken(Admin admin) {
        return Jwts.builder()
                .subject(admin.getAccountName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key())
                .compact();
    }

    // 내부 파싱 공용 메서드
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            Claims c = parseClaims(token);
            return c.getExpiration() != null && c.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getAccountName(String token) {
        return parseClaims(token).getSubject();
    }

    // Spring Security Authentication 생성
    public Authentication getAuthentication(String token) {
        String accountName = getAccountName(token);
        UserDetails user = User.withUsername(accountName)
                .password("")
                .authorities("ROLE_ADMIN")
                .build();
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }
}
