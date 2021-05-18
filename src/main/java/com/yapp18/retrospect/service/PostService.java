package com.yapp18.retrospect.service;

import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostQueryRepository;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.tag.Tag;
import com.yapp18.retrospect.domain.tag.TagRepository;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.template.TemplateRepository;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.web.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.awt.print.Pageable.*;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;
    private final TagRepository tagRepository;

    // 회고글 목록 조회
    public List<PostDto.ListResponse> getPostsList(Long cursorId, Integer pageSize){
        // 최초 idx 시 가장 최신 post로
        if (cursorId == null || cursorId == 0){
            cursorId = postRepository.findTop1ByOrderByPostIdxDesc().get(0).getPostIdx();
            System.out.println("=====>>>>>"+ cursorId);
        }
        // 마지막 chk 해야함

        return postQueryRepository.findByPostIdx(cursorId, pageSize);
    }

    // 회고글 저장
    public Post inputPosts(PostDto.saveResponse saveResponse){
        Optional<User> user = userRepository.findById(saveResponse.getUserIdx());
        if(!user.isPresent()) throw new NullPointerException("해당 아이디는 없습니다.");

        // 자유 템플릿인 경우 template_idx 0 으로 세팅
        Optional<Template> template = templateRepository.findById(saveResponse.getTemplateIdx());
        if(!template.isPresent()) throw new NullPointerException("해당 템플릿이 없습니다.");

        // post 저장
        Post post = postRepository.save(saveResponse.toEntity(user.get(), template.get()));
        // image 저장

        // tag 저장
        for (String tag : saveResponse.getTag()){
            System.out.println("해시태그 내용" + tag);
            tagRepository.save(Tag.builder().tag(tag).post(post).build());
        }
        return post;
    }

    // 회고글 수정

    // 회고글 삭제
    public boolean deletePosts(Long postIdx){
        // id 값이 있는지 검증
        boolean isPost = postRepository.existsById(postIdx);
        if (isPost){
            postRepository.deleteById(postIdx);
            return true;
        }
        return false;
    }

    // image

}
