package com.haem.blogbackend.category.infrastructure;

public class CategoryPostCountView {
    private final Long categoryId;
    private final String categoryName;
    private final Long postCount;

    public CategoryPostCountView(Long categoryId,
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
