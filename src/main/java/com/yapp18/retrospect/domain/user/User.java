package com.yapp18.retrospect.domain.user;

import com.yapp18.retrospect.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name="user_tb")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_idx;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String nickname;
    @Column(length = 100)
    private String intro;
    @Column(length = 50, nullable = false)
    private String email;
    private String picture;
    @Column(length = 20, nullable = false)
    private String platform;
    @Column(length = 20)
    private String job;
    private String access_token;

    @Builder
    public User(Long user_idx, String name, String nickname, String intro, String email, String picture, String platform, String job, String access_token) {
        this.user_idx = user_idx;
        this.name = name;
        this.nickname = nickname;
        this.intro = intro;
        this.email = email;
        this.picture = picture;
        this.platform = platform;
        this.job = job;
        this.access_token = access_token;
    }
}
