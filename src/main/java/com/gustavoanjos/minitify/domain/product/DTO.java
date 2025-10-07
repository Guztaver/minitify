package com.gustavoanjos.minitify.domain.product;

/**
 * Generic DTO interface to be implemented by DTO records that can convert to entities.
 * @param <T> the target entity type
 */
public interface DTO<T> {
    T toEntity();
}

