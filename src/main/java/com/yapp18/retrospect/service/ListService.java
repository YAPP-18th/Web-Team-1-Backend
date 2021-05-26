package com.yapp18.retrospect.service;

import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostQueryRepository;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ListService {

    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;

    public List<PostDto.ListResponse> findMyPostsByUserIdx(Long userIdx){
        return postQueryRepository.findAllByUserUserIdx(userIdx);
    }

}
