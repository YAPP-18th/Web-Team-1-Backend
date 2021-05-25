package com.yapp18.retrospect.domain.post;

import com.yapp18.retrospect.domain.BaseTimeEntity;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.web.dto.PostDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Optional;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@DynamicUpdate // 변경된 것만 바꾸기
@Table(name="post_tb")
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_idx", nullable = false, unique = true)
    private Long postIdx;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String contents;

    @ColumnDefault(value = "0")
    private int view;

    @Column(name = "cover_image")
    private String coverImage;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User user;

    @ManyToOne
    @JoinColumn(name = "template_idx")
    private Template template;

//    @OneToMany(mappedBy = "post")
//    @JoinColumn(name = "comment_idx")
//    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Post(Long postIdx,String title, String category, String contents,
                User user, Template template) {
        this.postIdx = postIdx;
        this.title = title;
        this.category = category;
        this.contents = contents;
        this.user = user;
        this.template = template;
    }

    public void update(PostDto.updateResponse requestDto){
        this.title = requestDto.getTitle();
        this.category = requestDto.getCategory();
        this.contents = requestDto.getContents();
    }



}