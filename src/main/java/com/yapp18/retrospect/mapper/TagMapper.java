package com.yapp18.retrospect.mapper;


import com.yapp18.retrospect.domain.tag.Tag;
import com.yapp18.retrospect.web.dto.TagDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel="spring")
public interface TagMapper {
    TagMapper instance = Mappers.getMapper(TagMapper.class);

    TagDto tagToDto(Tag tag);
}
