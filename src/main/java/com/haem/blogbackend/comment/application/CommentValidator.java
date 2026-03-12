package com.haem.blogbackend.comment.application;

import com.haem.blogbackend.comment.domain.CommentProfanityException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentValidator {

    private static final List<String> BANNED_WORDS = List.of(
            "욕설1",
            "욕설2",
            "욕설3"
    );

    public void validateProfanity(String content) {
        String lowerCaseContent = content.toLowerCase();

        boolean hasBannedWord = BANNED_WORDS.stream()
                .map(String::toLowerCase)
                .anyMatch(lowerCaseContent::contains);

        if (hasBannedWord) {
            throw new CommentProfanityException();
        }
    }
}
