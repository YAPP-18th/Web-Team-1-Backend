package com.yapp18.retrospect.domain.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yapp18.retrospect.domain.BaseTimeEntity;
import com.yapp18.retrospect.domain.comment.Comment;
import com.yapp18.retrospect.domain.image.Image;
import com.yapp18.retrospect.domain.like.Like;
import com.yapp18.retrospect.domain.tag.Tag;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.web.dto.PostDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Entity
@Builder
@DynamicUpdate // 변경된 것만 바꾸기
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="post_tb")
//@Builder
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "template_idx")
    private Template template; // 어떻게?

    @JsonIgnore
    @OneToMany(mappedBy = "post",orphanRemoval = true)
    private List<Tag> tagList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post",orphanRemoval = true)
    private  List<Like> like = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private  List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Image> imageList = new ArrayList<>();

    public void updatePost(PostDto.updateRequest requestDto){
        this.title = requestDto.getTitle();
        this.category = requestDto.getCategory();
        this.contents = requestDto.getContents();
    }

    public void updateview(int view){
        this.view = getView() + 1;
    }
}