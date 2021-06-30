package com.yapp18.retrospect.service;

import com.yapp18.retrospect.domain.banner.Banner;
import com.yapp18.retrospect.domain.banner.BannerRepository;
import com.yapp18.retrospect.domain.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BannerService {

    private final BannerRepository bannerRepository;

    // 배너 정보 출력
    @Transactional
    public Long getRecommendBanner(String project, int emotion, String term){
        return bannerRepository.findTemplateTemplateIdxByProjectAndEmotionAndTerm(project,emotion,term).getTemplate().getTemplateIdx();
    }
}
