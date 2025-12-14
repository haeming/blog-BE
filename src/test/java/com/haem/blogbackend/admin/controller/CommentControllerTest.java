package com.haem.blogbackend.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haem.blogbackend.admin.component.JwtProvider;
import com.haem.blogbackend.admin.service.AdminService;
import com.haem.blogbackend.admin.service.CommentService;
import com.haem.blogbackend.admin.dto.request.CommentCreateRequestDto;
import com.haem.blogbackend.admin.dto.response.CommentResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@WithMockUser(username = "admin")
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("댓글 생성")
    void createComment() throws Exception {
        // given
        CommentCreateRequestDto requestDto = CommentCreateRequestDto.builder()
                .postId(1L)
                .content("test comment")
                .nickname("admin") // Add nickname
                .password("password") // Add password
                .build();

        CommentResponseDto responseDto = CommentResponseDto.builder()
                .id(1L)
                .postId(1L)
                .content("test comment")
                .createdAt(LocalDateTime.now())
                .build();

        given(commentService.createComment(anyString(), any(CommentCreateRequestDto.class))).willReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/admin/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("test comment"));
    }
}
