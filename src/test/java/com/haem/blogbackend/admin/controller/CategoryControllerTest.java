package com.haem.blogbackend.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haem.blogbackend.admin.component.JwtProvider;
import com.haem.blogbackend.admin.service.AdminService;
import com.haem.blogbackend.admin.service.CategoryService;
import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.dto.request.CategoryCreateRequestDto;
import com.haem.blogbackend.dto.request.CategoryUpdateNameRequestDto;
import com.haem.blogbackend.dto.response.CategoryResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest(CategoryController.class)
@WithMockUser
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("모든 카테고리 조회")
    void getCategories() throws Exception {
        // given
        List<CategoryResponseDto> categories = List.of(
                CategoryResponseDto.from(new Category("cat1")),
                CategoryResponseDto.from(new Category("cat2"))
        );
        given(categoryService.getCategories()).willReturn(categories);

        // when & then
        mockMvc.perform(get("/api/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].categoryName").value("cat1"));
    }

    @Test
    @DisplayName("카테고리 개수 조회")
    void getCategoryCount() throws Exception {
        // given
        given(categoryService.getCategoryCount()).willReturn(5L);

        // when & then
        mockMvc.perform(get("/api/admin/categories/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    @DisplayName("특정 카테고리의 게시물 수 조회")
    void getPostCountByCategoryId() throws Exception {
        // given
        Long categoryId = 1L;
        given(categoryService.getPostCountByCategoryId(categoryId)).willReturn(10L);

        // when & then
        mockMvc.perform(get("/api/admin/categories/{categoryId}/post-count", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    @DisplayName("카테고리 생성")
    void createCategory() throws Exception {
        // given
        CategoryCreateRequestDto requestDto = CategoryCreateRequestDto.builder().categoryName("new-cat").build();
        MockMultipartFile data = new MockMultipartFile("data", "", "application/json", objectMapper.writeValueAsBytes(requestDto));
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());

        given(categoryService.createCategory(any(), any())).willReturn(CategoryResponseDto.from(new Category("new-cat", "/path/to/image.jpg", "test.jpg")));

        // when & then
        mockMvc.perform(multipart("/api/admin/categories")
                        .file(data)
                        .file(file)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("new-cat"));
    }

    @Test
    @DisplayName("카테고리 삭제")
    void deleteCategory() throws Exception {
        // given
        Long categoryId = 1L;

        // when & then
        mockMvc.perform(delete("/api/admin/categories/{id}", categoryId).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("카테고리 이름 수정")
    void updateCategoryName() throws Exception {
        // given
        Long categoryId = 1L;
        CategoryUpdateNameRequestDto requestDto = new CategoryUpdateNameRequestDto("updated-name");
        given(categoryService.updateCategoryName(any(), any())).willReturn(CategoryResponseDto.from(new Category("updated-name")));

        // when & then
        mockMvc.perform(patch("/api/admin/categories/{id}/name", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("updated-name"));
    }

    @Test
    @DisplayName("카테고리 이미지 수정")
    void updateCategoryImage() throws Exception {
        // given
        Long categoryId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "new.jpg", "image/jpeg", "test".getBytes());
        given(categoryService.updateCategoryImage(any(), any())).willReturn(CategoryResponseDto.from(new Category("cat-name", "/path/to/new.jpg", "new.jpg")));

        // when & then
        mockMvc.perform(multipart("/api/admin/categories/{id}/image", categoryId)
                        .file(file)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageUrl").value("/path/to/new.jpg"));
    }
}