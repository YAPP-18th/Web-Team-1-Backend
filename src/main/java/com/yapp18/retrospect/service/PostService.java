package com.yapp18.retrospect.service;

import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.template.TemplateRepository;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;

    // 회고글 저장
    public Post inputPosts(PostDto.saveResponse saveResponse){
        Optional<User> user = userRepository.findById(saveResponse.getUserIdx());
        if(!user.isPresent()) throw new NullPointerException("해당 아이디는 없습니다.");

        Optional<Template> template = templateRepository.findById(saveResponse.getTemplateIdx());
        if(!template.isPresent()) throw new NullPointerException("해당 템플릿이 없습니다.");
        // 공식 템플릿이 아닌 경우?

        return postRepository.save(saveResponse.toEntity(user.get(), template.get()));
    }


}
