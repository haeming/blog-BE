package com.haem.blogbackend.admin.dto.response;

public class CategoryPostCountResponse {
    private final Long categoryId;
    private final String categoryName;
    private final long postCount;

    public CategoryPostCountResponse(Long categoryId,
                                     String categoryName,
                                     long postCount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.postCount = postCount;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public long getPostCount() {
        return postCount;
    }
}
