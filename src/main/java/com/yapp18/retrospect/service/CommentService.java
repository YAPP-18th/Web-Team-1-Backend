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
        //타 서비스 의존성을 제거와 코드 가독성을 위해 Entity 외의 메서드 parameter는 최소화 할 것
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto.ListResponse> getCommmentsListByPostIdx(Long postIdx, Long userIdx, Pageable page){
        Post post = postRepository.findByPostIdx(postIdx);

        return commentRepository.findAllByPost(post, page)
                .orElseThrow(() ->  new EntityNullException(ErrorInfo.COMMENT_NULL)) // exception 안하는게 나을지도
                .stream()
                .map(comment -> commentMapper.toDto(comment, isWriter(comment.getUser().getUserIdx(), userIdx)))
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
                .map(commentMapper::toDto)
                .orElseThrow(() ->  new EntityNullException(ErrorInfo.COMMENT_NULL));
    }


    @Transactional
//    @PreAuthorize("#oldComment.user.userIdx == #user.userIdx")
    public Comment updateComments(Comment newComment){
        Long commentIdx = newComment.getCommentIdx();

        Comment oldComment = commentRepository.findById(commentIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.COMMENT_NULL));

        oldComment.update(newComment);

        return commentRepository.save(oldComment);
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