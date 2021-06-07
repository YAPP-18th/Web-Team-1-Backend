package com.yapp18.retrospect.domain.comment;

import com.yapp18.retrospect.domain.BaseTimeEntity;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name="comment_tb")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_idx", nullable = false, unique = true)
    private Long commentIdx;

    @Column(nullable = false)
    private String comments;

    @ManyToOne
    @JoinColumn(name = "post_idx")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User user;

    @Builder
    public Comment(Long commentIdx, String comments, Post post, User user) {
        this.commentIdx = commentIdx;
        this.comments = comments;
        this.post = post;
        this.user = user;
    }

    public Comment update(String comments, Post post, User user) {
        this.comments = comments;
        this.post = post;
        this.user = user;

        return this;
    }

    public Comment update(Comment com) {
        this.commentIdx = com.getCommentIdx();
        this.comments = com.getComments();
        this.post = com.getPost();
        this.user = com.getUser();

        return this;
    }
}
