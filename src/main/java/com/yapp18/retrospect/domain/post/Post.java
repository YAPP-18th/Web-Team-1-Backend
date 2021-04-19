package com.yapp18.retrospect.domain.post;

import com.yapp18.retrospect.domain.BaseTimeEntity;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name="post_tb")
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_idx;

    @Column(length = 20, nullable = false)
    private String category;
    @Column(nullable = false)
    private String title;
    //columnDefinition = "TEXT" - VARCHAR 255 제한 해제
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @Column(nullable = false)
    @ColumnDefault("0")
    private Long view;

    // 참조 방향 Post -> User
    @ManyToOne(cascade = CascadeType.ALL)
    // @JoinColumn 어노테이션은 외래 키를 매핑 할 때 사용합니다.
    // name 속성에는 매핑 할 외래 키 이름을 지정합니다.
    @JoinColumn(name = "own_user_idx")
    //user_idx가 삭제되면 CASCADE 처리
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "template_idx")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Template template;

    @Builder
    public Post(String category, String title, String content, Long view, User user, Template template) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.view = view;
        this.user = user;
        this.template = template;
    }
}

