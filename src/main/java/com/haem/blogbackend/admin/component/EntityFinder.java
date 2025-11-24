package com.haem.blogbackend.admin.component;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class EntityFinder {
    public <T, ID, E extends RuntimeException> T findByIdOrThrow(
            JpaRepository<T, ID> repository,
            ID id,
            Supplier<E> exceptionSupplier) {
        if(id == null || id instanceof Long && ((Long)id).equals(0L)){
            throw exceptionSupplier.get();
        }
        return repository.findById(id)
                .orElseThrow(exceptionSupplier);
    }
}
