package com.yapp18.retrospect.domain.image;

import com.yapp18.retrospect.domain.BaseTimeEntity;
import com.yapp18.retrospect.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.lang.annotation.Target;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    public void update(String imageUrl){
        this.imageUrl = imageUrl;
    }
}
