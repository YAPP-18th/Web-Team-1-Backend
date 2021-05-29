package com.yapp18.retrospect.service;

import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostQueryRepository;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.tag.Tag;
import com.yapp18.retrospect.domain.tag.TagRepository;
import com.yapp18.retrospect.web.dto.PostDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ListService {

    private final PostService postService;
    private final PostQueryRepository postQueryRepository;

    public List<PostDto.ListResponse> findAllPostsByUserIdx(Long userIdx,Long page, Integer pageSize ){
        Post post = postService.findRecentPost(page);
        return postQueryRepository.findAllByUserUserIdx(userIdx, page, pageSize, post.getCreated_at());
    }

}
