package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.config.TokenErrorInfo;
import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.comment.CommentRepository;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.mapper.CommentMapper;
import com.yapp18.retrospect.web.advice.EntityNullException;
import com.yapp18.retrospect.web.dto.ApiPagingResultResponse;
import com.yapp18.retrospect.web.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public Comment inputComments(Comment comment){
        //타 서비스 의존성을 제거와 코드 가독성을 위해 Entity 외의 메서드 parameter는 최소화 할 것
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommmentsListByPostIdx(Long postIdx, Pageable page){
        Post post = postRepository.findById(postIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.POST_NULL));

        return commentRepository.findAllByPost(post, page);
    }

    @Transactional(readOnly = true)
    public Long getCommmentsCountByPostIdx(Long postIdx){
        Post post = postRepository.findByPostIdx(postIdx);
        return commentRepository.countCommentByPost(post);
    }

    @Transactional(readOnly = true)
    public Comment getCommmentsByIdx(Long commentIdx){
        return commentRepository.findById(commentIdx)
                .orElseThrow(() ->  new EntityNullException(ErrorInfo.COMMENT_NULL));
    }


    @Transactional
//    @PreAuthorize("#oldComment.user.userIdx == #user.userIdx")
    public Comment updateComments(Comment newComment){
        Long commentIdx = newComment.getCommentIdx();

        Comment oldComment = commentRepository.findById(commentIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.COMMENT_NULL));

        if(!oldComment.isWriter(newComment.getUser())){
            throw new AccessDeniedException(TokenErrorInfo.ACCESS_DENIED.getMessage());
        }

        oldComment.update(newComment);

        return commentRepository.save(oldComment);
    }

    @Transactional
    public void deleteComments(User user, Long commentIdx){
        Comment comment = commentRepository.findById(commentIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.COMMENT_NULL));

        if(!comment.isWriter(user)){
            throw new AccessDeniedException(TokenErrorInfo.ACCESS_DENIED.getMessage());
        }

        commentRepository.delete(comment);
    }

    // 다음 페이지 여부 확인
    public boolean isNext(Post post, Long cursorId){
        if (cursorId == null) return false;
        return commentRepository.existsByCommentIdxGreaterThanAndPost(cursorId, post);
    }
}