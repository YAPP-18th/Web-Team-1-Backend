package com.yapp18.retrospect.domain.tag;

import com.yapp18.retrospect.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
@Entity
@Getter
@NoArgsConstructor
@Table(name="tag_tb")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tag_idx;

    @Column(length = 30, nullable = false)
    private String tag;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tagged_post_idx") // 'like된 포스트'의 idx
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @Builder
    public Tag(String tag, Post post) {
        this.tag = tag;
        this.post = post;
    }
}
