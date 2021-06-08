package com.yapp18.retrospect.domain.like;

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
@Table(name = "like_tb")
public class Like extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="like_idx", nullable = false, unique = true)
    private Long likeIdx;

    @ManyToOne
    @JoinColumn(name = "post_idx")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User user;

    @Builder
    public Like(Long likeIdx, Post post, User user){
        this.likeIdx = likeIdx;
        this.post = post;
        this.user = user;
    }

}