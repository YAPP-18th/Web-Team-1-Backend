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
    @Column(length = 50, nullable = false)
    private String email;
    private String profile_url;
    @Column(length = 20, nullable = false)
    private String platform;
    private String access_token;

    @Builder
    public User(String name, String email, String profile_url, String platform, String access_token) {
        this.name = name;
        this.email = email;
        this.profile_url = profile_url;
        this.platform = platform;
        this.access_token = access_token;
    }
}
