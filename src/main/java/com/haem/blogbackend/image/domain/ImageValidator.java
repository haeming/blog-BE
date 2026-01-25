package com.haem.blogbackend.image.domain;

import java.util.function.Predicate;

@FunctionalInterface
public interface ImageValidator extends Predicate<byte[]> {
    boolean test(byte[] imageBytes);
}
