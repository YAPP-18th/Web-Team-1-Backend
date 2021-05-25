package com.yapp18.retrospect.service;

import com.yapp18.retrospect.domain.image.Image;
import com.yapp18.retrospect.domain.image.ImageRepository;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostQueryRepository;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.tag.Tag;
import com.yapp18.retrospect.domain.tag.TagRepository;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.template.TemplateRepository;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.web.dto.ApiPagingResultResponse;
import com.yapp18.retrospect.web.dto.PostDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;
    private final TagRepository tagRepository;
    private final ImageRepository imageRepository;
    private final TokenService tokenService;


    // 회고글 목록 조회: 최신순
    public ApiPagingResultResponse<PostDto.ListResponse> getPostsList(Long cursorId, Integer pageSize){
        if (cursorId == null || cursorId == 0){ // 최초 검색 시 가장 최신 post로
            cursorId = postRepository.findTop1ByOrderByPostIdxDesc().get(0).getPostIdx();
        }
        Post post = postRepository.findById(cursorId).orElseThrow(()-> new NullPointerException("해당 회고글 idx가 없습니다."));

        List<PostDto.ListResponse> result = postQueryRepository.findByPostIdx(cursorId, pageSize,post.getCreated_at()); // cursor 방식으로 페이징(시간 + id)
        Long lastIdx = result.isEmpty() ? null : result.get(result.size()-1).getPostIdx();

        return new ApiPagingResultResponse<>(isNext(lastIdx), result);
    }

    // 회고글 목록 조회: 누적조회순
    public ApiPagingResultResponse<PostDto.ListResponse> getPostsListByView(Long cursorId, Integer pageSize){
        if (cursorId == null || cursorId == 0){ // 최초 검색 시 누적조회순으로
            cursorId = postRepository.findTop1ByOrderByViewDesc().get(0).getPostIdx();
        }
        Post post = postRepository.findById(cursorId).orElseThrow(() -> new NullPointerException("해당 회고글 idx가 없습니다."));

        List<PostDto.ListResponse> result = postQueryRepository.findByPostIdxOrderByViewDesc(pageSize, post.getView()); // view 로 페이징
        int lastView = result.isEmpty() ? 0 : result.get(result.size()-1).getView(); // 해당 조회수보다 낮은 글이 있는지 체크
        return new ApiPagingResultResponse<>(isNextView(lastView), result);

    }

    // 회고글 상세페이지
    public PostDto.detailResponse findPostContents(Long postIdx){
        Post post = postRepository.findById(postIdx).orElseThrow(() -> new NullPointerException("해당 post_idx가 없습니다."));
        List<String> tag = tagRepository.findByPostPostIdx(postIdx).stream()
                .map(Tag::getTag)
                .collect(Collectors.toList());
        return new PostDto.detailResponse(post, tag);
    }


    // 회고글 저장
    public Post inputPosts(PostDto.saveResponse saveResponse, Long userIdx){
        Optional<User> user = userRepository.findByUserIdx(userIdx);
        if(!user.isPresent()) throw new NullPointerException("해당 아이디는 없습니다.");

        Optional<Template> template = templateRepository.findById(saveResponse.getTemplateIdx());
        if(!template.isPresent()) throw new NullPointerException("해당 템플릿이 없습니다.");

        Post post = postRepository.save(saveResponse.toEntity(user.get(), template.get()));

        if (!saveResponse.getImage().isEmpty()){ // image가 있을 때만 저장
            for (String url : saveResponse.getImage()) {
                imageRepository.save(Image.builder().imageUrl(url).post(post).build());
            }
        }

        // tag 저장
        if (! saveResponse.getTag().isEmpty()){ // tag가 있을 때만 저장
            for (String tag : saveResponse.getTag()){
                System.out.println("해시태그 내용" + tag);
                tagRepository.save(Tag.builder().tag(tag).post(post).build());
            }
        }
        return post;
    }


    // 회고글 수정
    public Long updatePosts(Long userIdx,Long postIdx, PostDto.updateResponse requestDto){
        // postIdx가 있는지 chk
        Post post = postRepository.findById(postIdx)
                .orElseThrow(()-> new IllegalArgumentException("해당 회고글이 없습니다."));

        // user 체크
        if (post.getUser().getUserIdx().equals(userIdx)) post.update(requestDto);

        return post.getPostIdx();
    }



    // 회고글 삭제
    public boolean deletePosts(Long userIdx,Long postIdx) {
        Post post = postRepository.findById(postIdx)
                .orElseThrow(()-> new IllegalArgumentException("해당 회고글이 없습니다."));
        if (post.getUser().getUserIdx().equals(userIdx) ) {
            postRepository.deleteById(postIdx);
            return true;
        }
        return false;
    }



    private boolean isNext(Long cursorId){
        if (cursorId == null) return false;
        return postRepository.existsByPostIdxLessThan(cursorId);
    }

    private boolean isNextView(int view){
        if (view == 0) return false;
        return postRepository.existsByViewLessThan(view);
    }

    private boolean isWriter(Long userIdx){
        return false;
    }

}
