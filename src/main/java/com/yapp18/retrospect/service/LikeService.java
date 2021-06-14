package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.domain.like.Like;
import com.yapp18.retrospect.domain.like.LikeRepository;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.mapper.PostMapper;
import com.yapp18.retrospect.web.advice.EntityNullException;
import com.yapp18.retrospect.web.dto.ApiPagingResultResponse;
import com.yapp18.retrospect.web.dto.LikeDto;
import com.yapp18.retrospect.web.dto.PostDto;
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
    private final PostMapper postMapper;

    @Transactional
    public boolean isExist(LikeDto.InputRequest inputRequest, Long userIdx){
        return likeRepository.findByPostIdxAndUserIdx(inputRequest.getPostIdx(), userIdx) != null;
    }

    @Transactional(readOnly = true)
    public ApiPagingResultResponse<PostDto.ListResponse> getLikeListCreatedAt(Pageable page, Long userIdx){
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));

        List<Like> likeList = likeRepository.findByUserOrderByCreatedAtDesc(user, page);

        Long lastIdx = likeList.isEmpty() ? null : likeList.get(likeList.size() - 1).getLikeIdx(); // 낮은 조회수 체크

        List<PostDto.ListResponse> result = likeList.stream()
                .map(like -> postRepository.findById(like.getPost().getPostIdx())
                        .orElseThrow(() -> new EntityNullException(ErrorInfo.POST_NULL)))
                .map(post -> postMapper.postToListResponse(post, userIdx))
                .collect(Collectors.toList());

        return new ApiPagingResultResponse<>(isNext(user, lastIdx), result);
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
