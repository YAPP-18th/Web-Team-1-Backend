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
    @Column(name="template_idx", nullable = false)
    private Long templateIdx;

    @Column(name = "template_name", nullable = false)
    private String templateName;

    @Column(nullable = false)
    private String template;

    @Builder
    public Template(Long templateIdx, String templateName,String template) {
        this.templateIdx = templateIdx;
        this.templateName = templateName;
        this.template = template;
    }
}
