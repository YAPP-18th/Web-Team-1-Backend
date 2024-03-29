package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.config.TokenErrorInfo;
import com.yapp18.retrospect.domain.like.Like;
import com.yapp18.retrospect.domain.like.LikeRepository;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.mapper.LikeMapper;
import com.yapp18.retrospect.web.advice.EntityNullException;
import com.yapp18.retrospect.web.dto.LikeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostService postService;
    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;

    @Transactional
    public Like findByPostIdxAndUserIdx(Long postIdx, Long userIdx){
        return likeRepository.findByPostIdxAndUserIdx(postIdx, userIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.LIKE_NULL));
    }

    @Transactional
    public List<LikeDto.BasicResponse> getLikeListCreatedAt(User user, Long cursorIdx, Pageable pageable){
        List<Like> likeList =  cursorIdx == 0 ?
                likeRepository.findByUserOrderByCreatedAtDesc(user, pageable):
                likeRepository.cursorFindByUserOrderByCreatedAtDesc(user.getUserIdx(), cursorIdx, pageable);

        return likeList.stream()
                .map(like -> likeMapper.toDto(like))
                .collect(Collectors.toList());
    }

    @Transactional
    public Like inputLikes(User user, Long postIdx){
        Post post = postService.findByPostIdx(postIdx);

        return likeRepository.save(likeMapper.toEntity(user, post));
    }

    @Transactional
    public void deleteLikes(User user, Long postIdx){
        Like like = findByPostIdxAndUserIdx(user.getUserIdx(), postIdx);

        if(!like.isWriter(user)){
            throw new AccessDeniedException(TokenErrorInfo.ACCESS_DENIED.getMessage());
        }

        likeRepository.delete(like);
    }

    // 다음 페이지 여부 확인
    public boolean isNext(User user, Long cursorId){
        if (cursorId == null) return false;
        return likeRepository.existsByUserAndLikeIdxLessThan(user, cursorId);
    }
    // 누적순 페이징
//    private List<Like> getLikesAll(Long cursorId, Pageable page){
//        return
//                likeRepository.findAllByOrderByCreatedAtDesc(page) :
//                postRepository.findView(postRepository.findViewByPostIdx(cursorId).getView(), page, postRepository.findCreatedAtByPostIdx(cursorId).getCreatedAt());
//    }
}