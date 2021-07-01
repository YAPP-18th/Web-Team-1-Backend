package com.yapp18.retrospect.domain.user;

import com.sun.istack.NotNull;
import com.yapp18.retrospect.domain.BaseTimeEntity;
import com.yapp18.retrospect.security.oauth2.AuthProvider;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name="user_tb")
// 기본 생성자 접근을 protected으로 변경하면 외부에서 해당 생성자를 접근 할 수 없으므로 Builder를 통해서만 객체 생성 가능하므로 안전성 보장
@Builder
@AllArgsConstructor
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

    @Column(unique = true)
    private String nickname;

    private String profile;

    private String job;

    private String intro;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

//    @OneToMany(mappedBy = "user")
//    private final List<Like> like = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user")
//    private final List<Comment> comments = new ArrayList<>();

    public User updateNickname(String nickname){
        this.nickname = nickname;
        return this;
    }

    public User updateProfile(String profile, String name, String nickname, String job, String intro){
        this.name = name;
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
