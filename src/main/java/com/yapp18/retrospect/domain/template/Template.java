package com.yapp18.retrospect.domain.template;

import com.yapp18.retrospect.domain.BaseTimeEntity;
import com.yapp18.retrospect.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="template_tb")
public class Template extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="template_idx", nullable = false)
    private Long templateIdx;

    @Column(name = "template_name", nullable = false)
    private String templateName;

    @Column(nullable = false)
    private String template;
}
