package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.ErrorMessage;
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
    public Long inputComments(CommentDto.CommentInputRequest commentInputRequest, Long userIdx){
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new EntityNullException(ErrorMessage.USER_NULL));

        Post post = postRepository.findByPostIdx(commentInputRequest.getPostIdx())
                .orElseThrow(() -> new EntityNullException(ErrorMessage.POST_NULL));

        return commentRepository.save(commentInputRequest.toEntity(post, user)).getCommentIdx();
    }

    @Transactional(readOnly = true)
    public List<CommentDto.CommentResponse> getCommmentsListByPostIdx(Long postIdx, Pageable page){
        return commentRepository.findAllByPost(postIdx, page)
                .orElseThrow(() ->  new EntityNullException(ErrorMessage.COMMENT_NULL))
                .stream()
                .map(commentMapper::commentToListResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommentDto.CommentResponse getCommmentsByIdx(Long commentIdx){
        return commentRepository.findById(commentIdx)
                .map(commentMapper::commentToListResponse)
                .orElseThrow(() ->  new EntityNullException(ErrorMessage.COMMENT_NULL));
    }

    @Transactional
    public CommentDto.CommentResponse updateCommentsByIdx(CommentDto.CommentUpdateRequest commentUpdateRequest, Long commentIdx, Long userIdx){
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new EntityNullException(ErrorMessage.USER_NULL));

        Post post = postRepository.findByPostIdx(commentUpdateRequest.getPostIdx())
                .orElseThrow(() -> new EntityNullException(ErrorMessage.POST_NULL));

        Comment comment = commentRepository.findById(commentIdx)
                .map(entity -> entity.update(commentUpdateRequest.getComments(), post, user))
                .orElse(commentUpdateRequest.toEntity(user, post));

        return commentMapper.commentToListResponse(commentRepository.save(comment));
    }

    public void deleteCommentsByIdx(Long commentIdx){
        Comment comment = commentRepository.findById(commentIdx)
                .orElseThrow(() -> new EntityNullException(ErrorMessage.COMMENT_NULL));
        commentRepository.delete(comment);
    }
}
