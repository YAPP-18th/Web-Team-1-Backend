package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.comment.CommentRepository;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.mapper.CommentMapper;
import com.yapp18.retrospect.web.advice.EntityNullException;
import com.yapp18.retrospect.web.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public Comment inputComments(Comment comment){
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto.BasicResponse> getCommmentsListByPostIdx(Long postIdx, Long userIdx, Pageable page){
        Post post = postRepository.findByPostIdx(postIdx);

        return commentRepository.findAllByPost(post, page)
                .orElseThrow(() ->  new EntityNullException(ErrorInfo.COMMENT_NULL)) // exception 안하는게 나을지도
                .stream()
                .map(comment -> commentMapper.commentToBasicResponse(comment, isWriter(comment.getUser().getUserIdx(), userIdx)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long getCommmentsCountByPostIdx(Long postIdx){
        Post post = postRepository.findByPostIdx(postIdx);
        return commentRepository.countCommentByPost(post);
    }

    @Transactional(readOnly = true)
    public CommentDto.BasicResponse getCommmentsByIdx(Long commentIdx){
        return commentRepository.findById(commentIdx)
                .map(commentMapper::commentToBasicResponse)
                .orElseThrow(() ->  new EntityNullException(ErrorInfo.COMMENT_NULL));
    }


    @Transactional
    @PreAuthorize("#comment.user.userIdx == #userIdx")
    public CommentDto.BasicResponse updateCommentsByIdx(CommentDto.UpdateRequest updateRequest, Comment comment, Long userIdx){
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));

        Post post = postRepository.findByPostIdx(comment.getPost().getPostIdx());

        return commentMapper.commentToBasicResponse(commentRepository.save(
                comment.update(updateRequest.getComments(), post, user)
                )
        );
    }

    @Transactional
    @PreAuthorize("#comment.user.userIdx == #userIdx")
    public void deleteComments(Comment comment, Long userIdx){
        System.out.println(comment.getUser().getUserIdx());
        System.out.println(userIdx);
        commentRepository.delete(comment);
    }


    // 작성자 판별
    private boolean isWriter(Long commentUserIdx, Long userIdx){
        if (userIdx == 0L) return false;
        return commentUserIdx.equals(userIdx);
    }
}