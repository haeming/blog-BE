package com.haem.blogbackend.admin.service;

import com.haem.blogbackend.admin.repository.AdminRepository;
import com.haem.blogbackend.admin.repository.CommentRepository;
import com.haem.blogbackend.admin.repository.PostRepository;
import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.domain.Comment;
import com.haem.blogbackend.domain.Post;
import com.haem.blogbackend.admin.dto.request.CommentCreateRequestDto;
import com.haem.blogbackend.admin.dto.response.CommentResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("관리자 댓글 생성")
    void createCommentByAdmin() {
        // given
        String accountName = "admin";
        Long postId = 1L;
        CommentCreateRequestDto requestDto = CommentCreateRequestDto.builder()
                .postId(postId)
                .content("test comment")
                .build();

        Admin admin = new Admin(accountName, "password");
        Post post = mock(Post.class);
        when(post.getId()).thenReturn(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(adminRepository.findByAccountName(accountName)).thenReturn(Optional.of(admin));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        CommentResponseDto responseDto = commentService.createComment(accountName, requestDto);

        // then
        assertThat(responseDto.getContent()).isEqualTo("test comment");
        assertThat(responseDto.getPostId()).isEqualTo(postId);
    }
}
