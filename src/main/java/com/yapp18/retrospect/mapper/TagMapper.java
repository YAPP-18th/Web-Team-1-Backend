package com.yapp18.retrospect.mapper;


import com.yapp18.retrospect.domain.tag.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel="spring")
public interface TagMapper {
    TagMapper instance = Mappers.getMapper(TagMapper.class);

//    @Mapping(target = "tagIdx", ignore = true)
//    @Mapping(target = "post", ignore = true)
    Tag tagToDto(Tag tag);
}
