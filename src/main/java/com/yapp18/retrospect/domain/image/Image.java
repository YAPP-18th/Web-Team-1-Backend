package com.yapp18.retrospect.domain.image;

import com.yapp18.retrospect.domain.BaseTimeEntity;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name="image_tb")
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long image_idx;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_idx")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @Column(nullable = false)
    private String image_url;

    @Builder
    public Image(Post post, String image_url) {
        this.post = post;
        this.image_url = image_url;
    }
}

