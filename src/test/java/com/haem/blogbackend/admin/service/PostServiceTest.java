package com.haem.blogbackend.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import com.haem.blogbackend.admin.component.ImageProcessor;
import com.haem.blogbackend.admin.repository.AdminRepository;
import com.haem.blogbackend.admin.repository.CategoryRepository;
import com.haem.blogbackend.admin.repository.ImageRepository;
import com.haem.blogbackend.admin.repository.PostRepository;
import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.domain.Post;
import com.haem.blogbackend.dto.request.PostCreateRequestDto;
import com.haem.blogbackend.dto.request.PostUpdateInfoRequestDto;
import com.haem.blogbackend.dto.response.PostResponseDto;
import com.haem.blogbackend.dto.response.PostSummaryResponseDto;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageProcessor imageProcessor;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("게시물 개수 조회")
    void getPostCount() {
        // given
        when(postRepository.count()).thenReturn(10L);

        // when
        long count = postService.getPostCount();

        // then
        assertThat(count).isEqualTo(10L);
    }

    @Test
    @DisplayName("게시물 목록 조회")
    void getPosts() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<Post> posts = new ArrayList<>();
        Admin admin = new Admin("admin", "password");
        Category category = new Category("category");
        posts.add(new Post(category, admin, "title1", "content1"));
        posts.add(new Post(category, admin, "title2", "content2"));
        Page<Post> postPage = new PageImpl<>(posts, pageable, posts.size());

        when(postRepository.findAll(pageable)).thenReturn(postPage);

        // when
        Page<PostSummaryResponseDto> result = postService.getPosts(pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("title1");
    }

    @Test
    @DisplayName("특정 게시물 조회")
    void getPost() {
        // given
        Long postId = 1L;
        Admin admin = new Admin("admin", "password");
        Category category = new Category("category");
        Post post = new Post(category, admin, "title", "content");
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        PostResponseDto responseDto = postService.getPost(postId);

        // then
        assertThat(responseDto.getTitle()).isEqualTo("title");
        assertThat(responseDto.getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("게시물 생성")
    void createPost() {
        // given
        String accountName = "admin";
        Admin admin = new Admin(accountName, "password");
        Long categoryId = 1L;
        Category category = new Category("category");
        PostCreateRequestDto requestDto = PostCreateRequestDto.builder()
                .title("title")
                .content("content")
                .categoryId(categoryId)
                .build();
        MockMultipartFile[] files = {new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes())};

        when(adminRepository.findByAccountName(accountName)).thenReturn(Optional.of(admin));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post post = invocation.getArgument(0);
            // Simulate saving by returning the same post, now it has a category
            return post;
        });

        // when
        PostResponseDto responseDto = postService.createPost(accountName, requestDto, files);

        // then
        assertThat(responseDto.getTitle()).isEqualTo("title");
        verify(imageProcessor, times(1)).saveImage(any(Post.class), any(MockMultipartFile.class), any());
    }

    @Test
    @DisplayName("게시물 삭제")
    void deletePost() {
        // given
        Long postId = 1L;
        Admin admin = new Admin("admin", "password");
        Post post = new Post(null, admin, "title", "content");
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        postService.deletePost(postId);

        // then
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    @DisplayName("게시물 정보 수정")
    void updatePostInfo() {
        // given
        Long postId = 1L;
        Admin admin = new Admin("admin", "password");
        Post post = new Post(null, admin, "old title", "old content");
        PostUpdateInfoRequestDto requestDto = new PostUpdateInfoRequestDto("new title", "new content", null);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        PostResponseDto responseDto = postService.updatePostInfo(postId, requestDto);

        // then
        assertThat(responseDto.getTitle()).isEqualTo("new title");
        assertThat(responseDto.getContent()).isEqualTo("new content");
    }
}
