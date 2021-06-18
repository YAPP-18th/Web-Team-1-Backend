package com.yapp18.retrospect.service;


import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.template.TemplateRepository;
import com.yapp18.retrospect.web.advice.EntityNullException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class TemplateService {

    private final TemplateRepository templateRepository;

    public String getTemplateContents(Long templateIdx){
        Template template =  templateRepository.findById(templateIdx).orElseThrow(()->new EntityNullException(ErrorInfo.TEMPLATE_NULL));
        return template.getTemplate();
    }
}
