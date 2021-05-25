package com.yapp18.retrospect.mapper;

public interface EntityMapper<D,E> {
    E toEntity(D dto);

    D toDto(E entity);
}
