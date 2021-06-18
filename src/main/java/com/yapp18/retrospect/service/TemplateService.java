package com.yapp18.retrospect.service;


import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.template.TemplateRepository;
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
         return templateRepository.findById(templateIdx).map(Template::getTemplate).toString();
    }
}
