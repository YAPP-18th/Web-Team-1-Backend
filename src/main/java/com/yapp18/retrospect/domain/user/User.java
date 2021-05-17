package com.yapp18.retrospect.domain.user;

import com.yapp18.retrospect.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name="user_tb")
//@ToString(of = {"user_idx", "role", "name", "nickname", "intro", "email", "picture", "platform", "job"})
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_idx;
    //JPA로 데이터베이스를 저장할 때 enum값을 String으로 저장 될 수 있도록 선언
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
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

    @Builder
    public User(Role role, String name, String nickname, String intro, String email, String picture, String platform, String job) {
        this.role = role;
        this.name = name;
        this.nickname = nickname;
        this.intro = intro;
        this.email = email;
        this.picture = picture;
        this.platform = platform;
        this.job = job;
    }

    public User update(String name, String picture){
        this.name = name;
        this.picture = picture;
        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
