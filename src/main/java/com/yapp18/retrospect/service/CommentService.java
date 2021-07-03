package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.config.TokenErrorInfo;
import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.comment.CommentRepository;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.web.advice.EntityNullException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final PostService postService;
    private final CommentRepository commentRepository;

    @Transactional
    public Comment inputComments(Comment comment){
        //타 서비스 의존성을 제거와 코드 가독성을 위해 Entity 외의 메서드 parameter는 최소화 할 것
        Long postIdx = comment.getPost().getPostIdx();
        comment.setPost(postService.findByPostIdx(postIdx));
        commentRepository.save(comment);
        return comment; // commentRepository.save(comment); 그대로 반환하면 테스트 코드에서 엔티티가 null로 날아옴..
    }

    @Transactional(readOnly = true)
    public Comment getCommmentsByIdx(Long commentIdx){
        return commentRepository.findById(commentIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.COMMENT_NULL));
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommmentsListByPostIdx(Long postIdx, Pageable page){
        Post post = postService.findByPostIdx(postIdx);
        return commentRepository.findAllByPost(post, page);
    }

    @Transactional(readOnly = true)
    public Long getCommmentsCountByPostIdx(Long postIdx){
        Post post = postService.findByPostIdx(postIdx);
        return commentRepository.countCommentByPost(post);
    }


    @Transactional
//    @PreAuthorize("#oldComment.user.userIdx == #user.userIdx")
    public Comment updateComments(Comment newComment){
        Long commentIdx = newComment.getCommentIdx();

        Comment oldComment = commentRepository.findById(commentIdx)
                .orElseThrow(() ->  new EntityNullException(ErrorInfo.COMMENT_NULL));

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