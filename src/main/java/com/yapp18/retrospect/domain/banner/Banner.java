package com.yapp18.retrospect.domain.banner;


import com.yapp18.retrospect.domain.template.Template;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "banner_tb")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_idx", nullable = false, unique = true)
    private Long bannerIdx;

    @Column
    private String project;

    @Column
    private int emotion;

    @Column
    private String term;

    @ManyToOne
    @JoinColumn(name = "template_idx")
    private Template template;


    @Builder
    public Banner(Long bannerIdx,String project, int emotion, String term, Template template){
        this.bannerIdx = bannerIdx;
        this.project = project;
        this.emotion = emotion;
        this.term = term;
        this.template = template;

    }
}
