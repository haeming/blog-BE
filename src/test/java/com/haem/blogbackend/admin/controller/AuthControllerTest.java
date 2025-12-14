package com.haem.blogbackend.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haem.blogbackend.admin.service.AdminService;
import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.admin.dto.request.AdminLoginRequestDto;
import com.haem.blogbackend.admin.dto.response.AdminLoginResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("관리자 로그인")
    void adminLogin() throws Exception {
        // given
        AdminLoginRequestDto requestDto = new AdminLoginRequestDto("admin", "password");
        Admin admin = new Admin("admin", "password");
        AdminLoginResponseDto responseDto = AdminLoginResponseDto.from(admin, "test-token");
        given(adminService.adminLogin(any(AdminLoginRequestDto.class))).willReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountName").value("admin"))
                .andExpect(jsonPath("$.token").value("test-token"));
    }

    @Test
    @DisplayName("토큰 검증")
    @WithMockUser
    void verifyToken() throws Exception {
        // given
        String token = "test-token";
        Admin admin = new Admin("admin", "password");
        given(adminService.verifyAndGetAdmin(token)).willReturn(admin);

        // when & then
        mockMvc.perform(get("/api/admin/check-token")
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountName").value("admin"));
    }
}