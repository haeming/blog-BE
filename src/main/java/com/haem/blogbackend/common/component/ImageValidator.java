package com.haem.blogbackend.common.component;

import java.util.function.Predicate;

@FunctionalInterface
public interface ImageValidator extends Predicate<byte[]> {
    boolean test(byte[] imageBytes);
}
