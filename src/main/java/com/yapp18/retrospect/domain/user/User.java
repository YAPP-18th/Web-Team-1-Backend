package com.yapp18.retrospect.domain.user;

import com.yapp18.retrospect.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name="user_tb")
// 기본 생성자 접근을 protected으로 변경하면 외부에서 해당 생성자를 접근 할 수 없으므로 Builder를 통해서만 객체 생성 가능하므로 안전성 보장
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(length = 20, nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(Role role, String name, String nickname, String intro, String email,
                String profile, String provider, String providerId, String job) {
        this.role = role;
        this.name = name;
        this.nickname = nickname;
        this.intro = intro;
        this.email = email;
        this.profile = profile;
        this.provider = provider;
        this.providerId = providerId;
        this.job = job;
    }

    public User simpleUpdate(String name){
        this.name = name;
        return this;
    }

    public User updateProfile(String profile, String nickname, String job, String intro){
        this.profile = profile;
        this.nickname = nickname;
        this.job = job;
        this.intro = intro;
        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
