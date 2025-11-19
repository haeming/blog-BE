package com.haem.blogbackend.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haem.blogbackend.admin.component.JwtProvider;
import com.haem.blogbackend.admin.service.AdminService;
import com.haem.blogbackend.admin.service.PostService;
import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.domain.Post;
import com.haem.blogbackend.dto.request.PostCreateRequestDto;
import com.haem.blogbackend.dto.request.PostUpdateInfoRequestDto;
import com.haem.blogbackend.dto.response.PostResponseDto;
import com.haem.blogbackend.dto.response.PostSummaryResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@WithMockUser
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("게시물 목록 조회")
    void getPosts() throws Exception {
        // given
        Admin admin = new Admin("admin", "password");
        Category category = new Category("cat");
        Post post = new Post(category, admin, "title", "content");
        Page<PostSummaryResponseDto> page = new PageImpl<>(List.of(
                PostSummaryResponseDto.from(post, "cat")
        ));
        given(postService.getPosts(any(PageRequest.class))).willReturn(page);

        // when & then
        mockMvc.perform(get("/api/admin/posts?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value("title"));
    }

    @Test
    @DisplayName("게시물 개수 조회")
    void getPostCount() throws Exception {
        // given
        given(postService.getPostCount()).willReturn(20L);

        // when & then
        mockMvc.perform(get("/api/admin/posts/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("20"));
    }

    @Test
    @DisplayName("특정 게시물 조회")
    void getPost() throws Exception {
        // given
        Long postId = 1L;
        Admin admin = new Admin("admin", "password");
        Category category = new Category("cat");
        Post post = new Post(category, admin, "title", "content");
        PostResponseDto responseDto = PostResponseDto.from(post);
        given(postService.getPost(postId)).willReturn(responseDto);

        // when & then
        mockMvc.perform(get("/api/admin/posts/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"));
    }

    @Test
    @DisplayName("게시물 생성")
    void createPost() throws Exception {
        // given
        PostCreateRequestDto requestDto = PostCreateRequestDto.builder().title("new post").content("new content").build();
        MockMultipartFile data = new MockMultipartFile("data", "", "application/json", objectMapper.writeValueAsBytes(requestDto));
        Admin admin = new Admin("admin", "password");
        Post post = new Post(null, admin, "new post", "new content");
        given(postService.createPost(any(), any(), any())).willReturn(PostResponseDto.from(post));

        // when & then
        mockMvc.perform(multipart("/api/admin/posts")
                        .file(data)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("new post"));
    }

    @Test
    @DisplayName("게시물 삭제")
    void deletePost() throws Exception {
        // given
        Long postId = 1L;

        // when & then
        mockMvc.perform(delete("/api/admin/posts/{id}", postId).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시물 정보 수정")
    void updatePostInfo() throws Exception {
        // given
        Long postId = 1L;
        PostUpdateInfoRequestDto requestDto = new PostUpdateInfoRequestDto("updated title", "updated content", 1L);
        Admin admin = new Admin("admin", "password");
        Category category = new Category("cat");
        Post post = new Post(category, admin, "updated title", "updated content");
        given(postService.updatePostInfo(any(), any())).willReturn(PostResponseDto.from(post));

        // when & then
        mockMvc.perform(patch("/api/admin/posts/{id}/info", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("updated title"));
    }
}