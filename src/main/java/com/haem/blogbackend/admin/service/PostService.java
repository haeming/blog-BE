package com.haem.blogbackend.admin.service;

import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.haem.blogbackend.admin.repository.AdminRepository;
import com.haem.blogbackend.admin.repository.CategoryRepository;
import com.haem.blogbackend.admin.repository.ImageRepository;
import com.haem.blogbackend.admin.repository.PostRepository;
import com.haem.blogbackend.common.enums.BasePath;
import com.haem.blogbackend.common.exception.notfound.AdminNotFoundException;
import com.haem.blogbackend.common.exception.notfound.CategoryNotFoundException;
import com.haem.blogbackend.common.exception.notfound.PostNotFoundException;
import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.domain.Image;
import com.haem.blogbackend.domain.Post;
import com.haem.blogbackend.dto.request.PostCreateRequestDto;
import com.haem.blogbackend.dto.request.PostUpdateInfoRequestDto;
import com.haem.blogbackend.dto.response.PostResponseDto;
import com.haem.blogbackend.dto.response.PostSummaryResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
@Service
public class PostService {
    private final PostRepository postRepository;
    private final AdminRepository adminRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    public PostService(
            PostRepository postRepository,
            AdminRepository adminRepository,
            CategoryRepository categoryRepository,
            ImageRepository imageRepository,
            ImageService imageService) {
        this.postRepository = postRepository;
        this.adminRepository = adminRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.imageService = imageService;
    }

    public long getPostCount(){
        return postRepository.count();
    }

    public Page<PostSummaryResponseDto> getPosts(Pageable pageable){
        return postRepository.findAll(pageable).map(post -> {
            String categoryName = Optional.ofNullable(post.getCategory())
                    .map(Category::getCategoryName)
                    .orElse("미분류");
            return PostSummaryResponseDto.from(post, categoryName);
        });
    }

    public PostResponseDto getPost (Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return PostResponseDto.from(post);
    }

    @Transactional
    public PostResponseDto createPost(String accountName, PostCreateRequestDto requestDto, MultipartFile[] files) {
        Admin admin = findAdminOrNull(accountName);

        Category category = findCategoryOrNull(requestDto.getCategoryId());
        
        Post post = Post.create(category, admin, requestDto.getTitle(), requestDto.getContent());
        postRepository.save(post);

        extractImagesFromContent(post, requestDto.getContent());

        saveFiles(post, files);

        return PostResponseDto.from(post);
    }

    @Transactional
    public void deletePost (Long id){
        Post post = findPostOrNull(id);

        deleteAllImages(post);
        postRepository.delete(post);
    }

    @Transactional
    public PostResponseDto updatePostInfo(Long id, PostUpdateInfoRequestDto requestDto){
        Post post = findPostOrNull(id);
        updatePostIfValid(post, requestDto);
        return PostResponseDto.from(post);
    }

    
    private Admin findAdminOrNull(String accountName){
        if(accountName == null || accountName.isEmpty()){
            throw new AdminNotFoundException(accountName);
        }
        return adminRepository.findByAccountName(accountName)
            .orElseThrow(() -> new AdminNotFoundException(accountName));
    }

    private Category findCategoryOrNull(Long categoryId){
        if(categoryId == null || categoryId == 0){
            throw new CategoryNotFoundException(categoryId);
        }
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    private Post findPostOrNull(Long postId){
        if(postId == null || postId == 0){
            throw new PostNotFoundException(postId);
        }
        return postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));
    }

    private void extractImagesFromContent(Post post, String content){
        Matcher matcher = Pattern.compile("\\(/uploadFiles[^)]+\\)").matcher(content);
        while (matcher.find()) {
            String path = matcher.group().replace("(", "").replace(")", "");
            Image image = new Image(post, path, Paths.get(path).getFileName().toString());
            imageRepository.save(image);
        }
    }

    private void saveFiles(Post post, MultipartFile[] files) {
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                imageService.saveImage(post, file, BasePath.POST);
            }
        }
    }

    private void deleteAllImages(Post post) {
        for (Image image : post.getImages()) {
            imageService.deleteImage(image);
        }
    }

    private void updatePostIfValid(Post post, PostUpdateInfoRequestDto requestDto){
        if(requestDto.getTitle() != null && !requestDto.getTitle().isBlank()){
            post.setTitle(requestDto.getTitle());
        }
        if(requestDto.getContent() != null && !requestDto.getContent().isBlank()){
            post.setContent(requestDto.getContent());
        }
    }
}
