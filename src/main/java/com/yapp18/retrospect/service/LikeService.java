package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.domain.like.Like;
import com.yapp18.retrospect.domain.like.LikeRepository;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.mapper.LikeMapper;
import com.yapp18.retrospect.web.advice.EntityNullException;
import com.yapp18.retrospect.web.dto.ApiPagingResultResponse;
import com.yapp18.retrospect.web.dto.LikeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeMapper likeMapper;

    @Transactional
    public boolean isExist(LikeDto.InputRequest inputRequest, Long userIdx){
        return likeRepository.findByPostIdxAndUserIdx(inputRequest.getPostIdx(), userIdx) != null;
    }

    @Transactional(readOnly = true)
    public ApiPagingResultResponse<LikeDto.BasicResponse> getLikeListCreatedAt(Long cursorIdx, Long userIdx, Pageable pageable){
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));

        List<Like> result = cursorIdx == 0 ?
                likeRepository.findByUserOrderByCreatedAtDesc(user, pageable):
                likeRepository.cursorFindByUserOrderByCreatedAtDesc(cursorIdx, userIdx, pageable);

        Long lastIdx = result.isEmpty() ? null : result.get(result.size() - 1).getLikeIdx(); // 낮은 조회수 체크

        return new ApiPagingResultResponse<>(isNext(user, lastIdx),
                result.stream()
                .map(like -> likeMapper.likeToBasicResponse(like,
                        postRepository.findById(like.getPost().getPostIdx())
                                .orElseThrow(() -> new EntityNullException(ErrorInfo.POST_NULL))))
                .collect(Collectors.toList())
        );
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
    public void deleteLikes(Long userIdx, Long postIdx){
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));

        Post post = postRepository.findByPostIdx(postIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.POST_NULL));

        likeRepository.deleteByUserAndPost(user, post);
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
