package com.yapp18.retrospect.domain.template;

import com.yapp18.retrospect.domain.BaseTimeEntity;
import com.yapp18.retrospect.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name="template_tb")
public class Template extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long template_idx;

    @Column(length = 30, nullable = false)
    private String template_name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String template;



    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "own_user_idx", insertable=false, updatable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Builder
    public Template(Long template_idx, String template_name, String template, User user) {
        this.template_idx = template_idx;
        this.template_name = template_name;
        this.template = template;
        this.user = user;
    }
}
