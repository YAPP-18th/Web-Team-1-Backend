package com.yapp18.retrospect.domain.scrap;

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
@Table(name="scrap_tb")
public class Scrap extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrap_idx;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_idx") // 'scrap한 유저'의 idx
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "scraped_post_idx") // 'scrap된 포스트'의 idx
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @Builder
    public Scrap(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}
