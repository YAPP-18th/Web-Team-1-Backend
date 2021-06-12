package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.domain.like.Like;
import com.yapp18.retrospect.domain.like.LikeRepository;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.web.advice.EntityNullException;
import com.yapp18.retrospect.web.dto.LikeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public boolean isExist(LikeDto.InputRequest inputRequest, Long userIdx){
        return likeRepository.findByPostIdxAndUserIdx(inputRequest.getPostIdx(), userIdx) != null;
    }

    @Transactional
    public Long inputLikes(LikeDto.InputRequest inputRequest, Long userIdx){
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));

        Post post = postRepository.findByPostIdx(inputRequest.getPostIdx())
                .orElseThrow(() -> new EntityNullException(ErrorInfo.POST_NULL));

        return likeRepository.save(inputRequest.toEntity(post, user)).getLikeIdx();
    }

    @Transactional
    public void deleteLikes(Long likeIdx){
        Like like = likeRepository.findById(likeIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.LIKE_NULL));

        likeRepository.delete(like);
    }
}
