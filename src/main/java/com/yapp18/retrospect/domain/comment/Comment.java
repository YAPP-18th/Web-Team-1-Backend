package com.yapp18.retrospect.domain.comment;

import com.yapp18.retrospect.domain.BaseTimeEntity;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name="comment_tb")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comment_idx;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_idx")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    //댓글 단 사용자
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "own_user_idx")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Builder
    public Comment(String content, Post post, User user) {
        this.content = content;
        this.post = post;
        this.user = user;
    }
}
