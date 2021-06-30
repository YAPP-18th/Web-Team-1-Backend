package com.yapp18.retrospect.domain.image;

import com.yapp18.retrospect.domain.BaseTimeEntity;
import com.yapp18.retrospect.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.lang.annotation.Target;

@NoArgsConstructor
@Getter
@Entity
@Table(name ="image_tb")
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_idx", nullable = false, unique = true)
    private Long imageIdx;

    @Column(name ="image_url",nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "post_idx")
    private Post post;

    @Builder
    public Image(Long imageIdx, String imageUrl, Post post){
        this.imageIdx = imageIdx;
        this.imageUrl = imageUrl;
        this.post = post;
    }

    public void update(String imageUrl){
        this.imageUrl = imageUrl;
    }

}
