package com.yapp18.retrospect.domain.banner;

import com.yapp18.retrospect.domain.template.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner,Long> {

    Banner findTemplateTemplateIdxByProjectAndEmotionAndTerm(String project, int emotion, String Term);

}
