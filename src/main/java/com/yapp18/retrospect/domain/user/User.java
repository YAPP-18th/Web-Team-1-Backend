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

    public User update(String name, String profile){
        this.name = name;
        this.profile = profile;
        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
