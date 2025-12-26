package com.haem.blogbackend.admin.service;

import com.haem.blogbackend.admin.component.EntityFinder;
import com.haem.blogbackend.admin.component.ImageProcessor;
import com.haem.blogbackend.admin.repository.*;
import com.haem.blogbackend.common.enums.BasePath;
import com.haem.blogbackend.common.exception.notfound.AdminNotFoundException;
import com.haem.blogbackend.common.exception.notfound.PostNotFoundException;
import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.domain.Image;
import com.haem.blogbackend.domain.Post;
import com.haem.blogbackend.admin.dto.request.PostCreateRequestDto;
import com.haem.blogbackend.admin.dto.request.PostUpdateInfoRequestDto;
import com.haem.blogbackend.admin.dto.response.PostResponseDto;
import com.haem.blogbackend.admin.dto.response.PostSummaryResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
public class PostService {
    private final PostRepository postRepository;
    private final AdminRepository adminRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ImageProcessor imageProcessor;
    private final EntityFinder entityFinder;
    private final CommentRepository commentRepository;

    public PostService(
            PostRepository postRepository,
            AdminRepository adminRepository,
            CategoryRepository categoryRepository,
            ImageRepository imageRepository,
            ImageProcessor imageProcessor,
            EntityFinder entityFinder,
            CommentRepository commentRepository){
        this.postRepository = postRepository;
        this.adminRepository = adminRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.imageProcessor = imageProcessor;
        this.entityFinder = entityFinder;
        this.commentRepository = commentRepository;
    }

    public long getPostCount(){
        return postRepository.countByDeletedAtIsNull();
    }

    public Page<PostSummaryResponseDto> getPosts(Pageable pageable){
        return postRepository.findByDeletedAtIsNull(pageable)
                .map(post -> {
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
        Admin admin = getAdminOrThrow(accountName);
        Category category = getCategoryOrThrow(requestDto.getCategoryId());
        Post post = Post.create(category, admin, requestDto.getTitle(), requestDto.getContent());
        postRepository.save(post);

        saveFiles(post, files);
        syncImagesByContent(post, requestDto.getContent());

        return PostResponseDto.from(post);
    }

    @Transactional
    public void deletePost (Long id){
        Post post = getPostOrThrow(id);
        deleteAllImages(post);
        post.softDelete();
        commentRepository.softDeleteByPostId(id);
    }

    @Transactional
    public PostResponseDto updatePostInfo(Long id, PostUpdateInfoRequestDto requestDto){
        Post post = getPostOrThrow(id);
        String beforeContent = post.getContent();
        post.updateIfPresent(requestDto.getTitle(), requestDto.getContent());

        String afterContent = post.getContent();
        if (!afterContent.equals(beforeContent)) {
            syncImagesByContent(post, afterContent);
        }

        return PostResponseDto.from(post);
    }
    
    private Admin getAdminOrThrow(String accountName){
        return entityFinder.findByStringKeyOrThrow(
                accountName,
                adminRepository::findByAccountName,
                () -> new AdminNotFoundException(accountName)
        );
    }

    private Category getCategoryOrThrow(Long categoryId){
        return entityFinder.findByIdOrNull(categoryId,categoryRepository);
    }

    private Post getPostOrThrow(Long postId){
        return entityFinder.findByIdOrThrow(
                postId,
                postRepository,
                () -> new PostNotFoundException(postId)
        );
    }

    // url 추출
    private Set<String> extractImageUrlsFromContent(String content){
        Set<String> urls = new HashSet<>();
        if(content == null || content.isBlank()){
            return urls;
        }

        Matcher matcher = Pattern.compile("\\(/uploadFiles[^)]+\\)").matcher(content);
        while (matcher.find()) {
            String url = matcher.group().replace("(", "").replace(")", "");
            urls.add(url);
        }
        return urls;
    }
//    private void extractImagesFromContent(Post post, String content){
//        Matcher matcher = Pattern.compile("\\(/uploadFiles[^)]+\\)").matcher(content);
//        while (matcher.find()) {
//            String path = matcher.group().replace("(", "").replace(")", "");
//            Image image = new Image(post, path, Paths.get(path).getFileName().toString());
//            imageRepository.save(image);
//        }
//    }

    private void syncImagesByContent(Post post, String content) {
        Set<String> newUrls = extractImageUrlsFromContent(content);

        // 기존 이미지 url -> Image 매핑
        Map<String, Image> existingMap = post.getImages().stream()
                .collect(Collectors.toMap(Image::getImageUrl, img -> img, (a, b) -> a));

        // 1) 삭제
        for (Image img : new ArrayList<>(post.getImages())) {
            if (!newUrls.contains(img.getImageUrl())) {
                imageProcessor.deleteImage(img); // 파일 삭제 + DB 삭제
                post.removeImage(img);           // 컬렉션에서도 제거(정합성)
            }
        }

        // 2) 추가
        for (String url : newUrls) {
            if (!existingMap.containsKey(url)) {
                String originalName = Paths.get(url).getFileName().toString();
                Image image = new Image(post, url, originalName);

                post.addImage(image);
                imageRepository.save(image); // 현재 ImageProcessor가 save 시 repo.save를 쓰므로 여기서도 명시적으로 save
            }
        }
    }


    private void saveFiles(Post post, MultipartFile[] files) {
        if (files != null) {
            for (MultipartFile file : files) {
                imageProcessor.saveImage(post, file, BasePath.POST);
            }
        }
    }

    private void deleteAllImages(Post post) {
        for (Image image : new ArrayList<>(post.getImages())) {
            imageProcessor.deleteImage(image);
            post.removeImage(image);
        }
    }
}
