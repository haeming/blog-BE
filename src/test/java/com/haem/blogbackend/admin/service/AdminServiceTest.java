package com.haem.blogbackend.admin.service;

import com.haem.blogbackend.admin.component.JwtProvider;
import com.haem.blogbackend.admin.repository.AdminRepository;
import com.haem.blogbackend.common.exception.notfound.AdminNotFoundException;
import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.dto.request.AdminLoginRequestDto;
import com.haem.blogbackend.dto.response.AdminLoginResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AdminService adminService;

    @Test
    @DisplayName("관리자 로그인 성공")
    void adminLogin_success() {
        // given
        AdminLoginRequestDto requestDto = new AdminLoginRequestDto("admin", "password");
        Admin admin = new Admin("admin", "encoded_password");
        when(adminRepository.findByAccountName("admin")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("password", "encoded_password")).thenReturn(true);
        when(jwtProvider.generateToken(admin)).thenReturn("test_token");

        // when
        AdminLoginResponseDto responseDto = adminService.adminLogin(requestDto);

        // then
        assertThat(responseDto.getAccountName()).isEqualTo("admin");
        assertThat(responseDto.getToken()).isEqualTo("test_token");
    }

    @Test
    @DisplayName("관리자 로그인 실패 - 존재하지 않는 관리자")
    void adminLogin_fail_adminNotFound() {
        // given
        AdminLoginRequestDto requestDto = new AdminLoginRequestDto("admin", "password");
        when(adminRepository.findByAccountName("admin")).thenReturn(Optional.empty());

        // when & then
        assertThrows(AdminNotFoundException.class, () -> {
            adminService.adminLogin(requestDto);
        });
    }

    @Test
    @DisplayName("관리자 로그인 실패 - 비밀번호 불일치")
    void adminLogin_fail_passwordNotMatch() {
        // given
        AdminLoginRequestDto requestDto = new AdminLoginRequestDto("admin", "password");
        Admin admin = new Admin("admin", "encoded_password");
        when(adminRepository.findByAccountName("admin")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("password", "encoded_password")).thenReturn(false);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.adminLogin(requestDto);
        });
    }

    @Test
    @DisplayName("토큰 검증 및 관리자 정보 조회")
    void verifyAndGetAdmin() {
        // given
        String token = "test_token";
        String accountName = "admin";
        Admin admin = new Admin(accountName, "password");
        when(jwtProvider.validateToken(token)).thenReturn(true);
        when(jwtProvider.getAccountName(token)).thenReturn(accountName);
        when(adminRepository.findByAccountName(accountName)).thenReturn(Optional.of(admin));

        // when
        Admin result = adminService.verifyAndGetAdmin(token);

        // then
        assertThat(result.getAccountName()).isEqualTo(accountName);
    }
}
