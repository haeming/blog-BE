package com.haem.blogbackend.global.util;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class EntityFinder {
    public <T, ID, E extends RuntimeException> T findByIdOrThrow(
            ID id,
            JpaRepository<T, ID> repository,
            Supplier<E> exceptionSupplier) {
        if(id == null || (id instanceof Long && id.equals(0L))){
            throw exceptionSupplier.get();
        }
        return repository.findById(id)
                .orElseThrow(exceptionSupplier);
    }

    public <T, ID> T findByIdOrNull(
            ID id,
            JpaRepository<T, ID> repository
    ){
        if(id == null || (id instanceof Long && id.equals(0L))){
            return null;
        }
        return repository.findById(id).orElse(null);
    }

    public <T, E extends RuntimeException> T findByStringKeyOrThrow(
            String key,
            Function<String, Optional<T>> finder,
            Supplier<E> exceptionSupplier) {
        if (key == null || key.isBlank()) {
            throw exceptionSupplier.get();
        }
        return finder.apply(key)
                .orElseThrow(exceptionSupplier);
    }
}
