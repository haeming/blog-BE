package com.haem.blogbackend.admin.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.haem.blogbackend.admin.component.DirectoryManagement;
import com.haem.blogbackend.admin.component.FileManagement;
import com.haem.blogbackend.admin.repository.CategoryRepository;
import com.haem.blogbackend.admin.repository.PostRepository;
import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.dto.request.CategoryCreateRequestDto;
import com.haem.blogbackend.dto.request.CategoryUpdateNameRequestDto;
import com.haem.blogbackend.dto.response.CategoryResponseDto;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private FileManagement fileManagement;

    @Mock
    private DirectoryManagement directoryManagement;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName("카테고리 개수 조회")
    void getCategoryCount() {
        // given
        when(categoryRepository.count()).thenReturn(5L);

        // when
        long count = categoryService.getCategoryCount();

        // then
        assertThat(count).isEqualTo(5L);
    }

    @Test
    @DisplayName("카테고리 생성 - 이미지 없음")
    void createCategory_withoutImage() throws IOException {
        // given
        CategoryCreateRequestDto requestDto = CategoryCreateRequestDto.from(new Category("test-category"));
        Category category = new Category("test-category");
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // when
        CategoryResponseDto responseDto = categoryService.createCategory(requestDto, null);

        // then
        assertThat(responseDto.getCategoryName()).isEqualTo("test-category");
        verify(fileManagement, never()).uploadFile(any(), any(), any());
    }

    @Test
    @DisplayName("카테고리 생성 - 이미지 있음")
    void createCategory_withImage() throws IOException {
        // given
        CategoryCreateRequestDto requestDto = CategoryCreateRequestDto.from(new Category("test-category"));
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes(StandardCharsets.UTF_8));
        Category category = new Category("test-category", "/path/to/image.jpg", "test.jpg");

        when(fileManagement.uploadFile(any(), any(), any())).thenReturn("/path/to/image.jpg");
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // when
        CategoryResponseDto responseDto = categoryService.createCategory(requestDto, file);

        // then
        assertThat(responseDto.getCategoryName()).isEqualTo("test-category");
        assertThat(responseDto.getImageUrl()).isEqualTo("/path/to/image.jpg");
        verify(fileManagement, times(1)).uploadFile(any(), any(), any());
    }

    @Test
    @DisplayName("카테고리 삭제")
    void deleteCategory() {
        // given
        Long categoryId = 1L;
        Category category = new Category("test-category", "/uploadFiles/category/2025/10/08/test.jpg", "test.jpg");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // when
        categoryService.deleteCategory(categoryId);

        // then
        verify(fileManagement, times(1)).deleteFile(anyString());
        verify(directoryManagement, times(1)).deleteEmptyParentDirectories(any(), any());
        verify(categoryRepository, times(1)).delete(any(Category.class));
    }

    @Test
    @DisplayName("카테고리 이름 수정")
    void updateCategoryName() {
        // given
        Long categoryId = 1L;
        Category category = new Category("old-name");
        CategoryUpdateNameRequestDto requestDto = new CategoryUpdateNameRequestDto("new-name");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // when
        CategoryResponseDto responseDto = categoryService.updateCategoryName(categoryId, requestDto);

        // then
        assertThat(responseDto.getCategoryName()).isEqualTo("new-name");
    }

    @Test
    @DisplayName("카테고리 이미지 수정")
    void updateCategoryImage() {
        // given
        Long categoryId = 1L;
        Category category = new Category("test-category", "/path/to/old-image.jpg", "old-image.jpg");
        MultipartFile file = new MockMultipartFile("file", "new-image.jpg", "image/jpeg", "test".getBytes(StandardCharsets.UTF_8));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(fileManagement.uploadFile(any(), any(), any())).thenReturn("/path/to/new-image.jpg");

        // when
        CategoryResponseDto responseDto = categoryService.updateCategoryImage(categoryId, file);

        // then
        verify(fileManagement, times(1)).deleteFile("/path/to/old-image.jpg");
        assertThat(responseDto.getImageUrl()).isEqualTo("/path/to/new-image.jpg");
    }

    @Test
    @DisplayName("모든 카테고리 조회")
    void getCategories() {
        // given
        List<Category> categories = List.of(new Category("cat1"), new Category("cat2"));
        when(categoryRepository.findAll()).thenReturn(categories);

        // when
        List<CategoryResponseDto> responseDtos = categoryService.getCategories();

        // then
        assertThat(responseDtos).hasSize(2);
        assertThat(responseDtos.get(0).getCategoryName()).isEqualTo("cat1");
    }

    @Test
    @DisplayName("특정 카테고리의 게시물 수 조회")
    void getPostCountByCategoryId() {
        // given
        Long categoryId = 1L;
        when(postRepository.countByCategoryId(categoryId)).thenReturn(10L);

        // when
        long count = categoryService.getPostCountByCategoryId(categoryId);

        // then
        assertThat(count).isEqualTo(10L);
    }
}
