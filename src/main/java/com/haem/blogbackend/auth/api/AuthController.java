package com.haem.blogbackend.auth.api;

import com.haem.blogbackend.auth.application.AuthService;
import com.haem.blogbackend.auth.domain.InvalidTokenException;
import com.haem.blogbackend.auth.domain.Admin;
import com.haem.blogbackend.auth.api.dto.AdminLoginRequestDto;
import com.haem.blogbackend.auth.api.dto.AdminLoginResponseDto;
import com.haem.blogbackend.auth.api.dto.TokenVerifyResponseDto;
import com.haem.blogbackend.global.util.ClientIpResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AuthController {
    private final AuthService authService;
    private final ClientIpResolver clientIpResolver;

    public AuthController(AuthService authService, ClientIpResolver clientIpResolver) {
        this.authService = authService;
        this.clientIpResolver = clientIpResolver;
    }

    @PostMapping("/login")
    public AdminLoginResponseDto adminLogin(@RequestBody AdminLoginRequestDto requestDto, HttpServletRequest request){
        return authService.adminLogin(requestDto, clientIpResolver.resolve(request));
    }

    @GetMapping("/check-token")
    public TokenVerifyResponseDto verifyToken(@RequestHeader(value = "Authorization", required = false) String authHeader){
        String token = extractToken(authHeader);
        Admin admin = authService.verifyAndGetAdmin(token);

        return TokenVerifyResponseDto.from(admin);
    }

    private String extractToken(String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new InvalidTokenException();
        }
        return authHeader.substring(7);
    }
}
