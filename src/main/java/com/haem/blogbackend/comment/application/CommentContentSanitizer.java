package com.haem.blogbackend.comment.application;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class CommentContentSanitizer {

    public String sanitize(String content) {
        return Jsoup.clean(content, Safelist.none());
    }
}
