package com.yapp18.retrospect.service;


import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.tag.Tag;
import com.yapp18.retrospect.domain.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    // tag 저장
    public void saveTagList(List<String> tagList, Post post){
        if (!tagList.isEmpty()){ // tag가 있을 때만 저장
            for (String tag : tagList){
                tagRepository.save(Tag.builder().tag(tag).post(post).build());
            }
        }
    }

    // tag 삭제
    public void delTagList(List<String> tagList, Post post){
        System.out.println(tagList);
        if (!tagList.isEmpty()){ // 삭제할 것이 있는 것의 tagIdx
            List<Long> result = post.getTagList().stream().filter(tag -> tagList.contains(tag.getTag())).map(Tag::getTagIdx).collect(Collectors.toList());
            System.out.println("삭제해야할 태그 인덱스"+result);
            tagRepository.deleteAllByTagIdxInQuery(result);
        }
    }
}
