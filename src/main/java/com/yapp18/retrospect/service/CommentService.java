package com.yapp18.retrospect.service;

import com.yapp18.retrospect.domain.comment.CommentRepository;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.mapper.CommentMapper;
import com.yapp18.retrospect.web.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public Long inputComments(CommentDto.CommentRequest commentRequest, Long userIdx){
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new NullPointerException("해당 아이디는 없습니다."));

        Post post = postRepository.findByPostIdx(commentRequest.getPostIdx())
                .orElseThrow(() -> new NullPointerException("해당 회고글은 없습니다."));

        return commentRepository.save(commentRequest.toEntity(user, post)).getCommentIdx();
    }

//    @Transactional(readOnly = true)
//    public List<CommentDto.ListResponse> getCommmentsListByPostIdx(Long postIdx, Pageable page){
//        return commentRepository.findAllByPostIdx(postIdx, page)
//                .stream()
//                .map(commentMapper::commentToListResponse)
//                .collect(Collectors.toList());
//
//        return new ApiPagingResultResponse<>(isNext(lastIdx),result);
//    }

//    public boolean isNext(Long cursorId){
//        if (cursorId == null) return false;
//        return postRepository.existsByPostIdxLessThan(cursorId);
//    }
}
