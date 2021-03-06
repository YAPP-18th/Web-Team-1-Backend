package com.yapp18.retrospect.domain.tag;

import com.yapp18.retrospect.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
    @Column(name="tag_idx", nullable = false)
    private Long tagIdx;

    @Column(nullable = false)
    private String tag;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_idx")
    @ToString.Exclude
    private Post post;

    @Builder
    public Tag(Long tagIdx, String tag, Post post) {
        this.tagIdx = tagIdx;
        this.tag = tag;
        this.post = post;
    }


}
