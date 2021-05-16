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
    @Column(name="user_idx", nullable = false)
    private Long userIdx;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    private String profile;

    private String job;

    private String intro;

    @Column(nullable = false)
    private String platform;


    @Builder
    public User(Long userIdx, String email, String name, String nickname,
                String profile, String job, String intro,String platform){
        this.userIdx = userIdx;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.profile = profile;
        this.job = job;
        this.intro = intro;
        this.platform = platform;

    }
}
