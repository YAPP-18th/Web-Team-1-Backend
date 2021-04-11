package com.yapp18.retrospect.domain.post;

import com.yapp18.retrospect.domain.BaseTimeEntity;
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
    // 참조 방향 Post -> User
    @ManyToOne(cascade = CascadeType.ALL)
    // @JoinColumn 어노테이션은 외래 키를 매핑 할 때 사용합니다.
    // name 속성에는 매핑 할 외래 키 이름을 지정합니다.
    @JoinColumn(name = "user_idx")
    //user_idx가 삭제되면 CASCADE 처리
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(length = 20, nullable = false)
    private String category;
    @Column(length = 40, nullable = false)
    private String template;
    @Column(nullable = false)
    private String title;
    //columnDefinition = "TEXT" - VARCHAR 255 제한 해제
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @Column(nullable = false)
    @ColumnDefault("0")
    private Long view;

    // 우선 user_idx라는 컬럼은 만들지 않았으므로 Builder에는 포함되지 않음
    @Builder
    public Post(User user, String category, String template, String title, String content, Long view) {
        this.user = user;
        this.category = category;
        this.template = template;
        this.title = title;
        this.content = content;
        this.view = view;
    }
}

