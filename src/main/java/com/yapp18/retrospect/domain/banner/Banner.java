package com.yapp18.retrospect.domain.banner;


import com.yapp18.retrospect.domain.template.Template;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
