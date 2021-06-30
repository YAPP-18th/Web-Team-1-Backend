package com.yapp18.retrospect.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface GenericMapper<E, D> {
    E toEntity(D dto);
    D toDto(E entity);
}
