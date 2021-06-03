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
import com.yapp18.retrospect.mapper.PostMapper;
import com.yapp18.retrospect.web.dto.ApiPagingResultResponse;
import com.yapp18.retrospect.web.dto.PostListDto;
import com.yapp18.retrospect.web.dto.PostDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    private final PostMapper postMapper;


    // 최신순
    public ApiPagingResultResponse<PostListDto> getPostsListRecent(Long cursorId, Pageable page){
        List<PostListDto> result = getPostsRecent(cursorId, page).stream().map(postMapper::postToListResponse)
                .collect(Collectors.toList());
        Long lastIdx = result.isEmpty() ? null : result.get(result.size()-1).getPostIdx();

        return new ApiPagingResultResponse<>(isNext(lastIdx), result);
    }

    // 조회순
    public ApiPagingResultResponse<PostListDto> getPostsListView(Long cursorId, Pageable page){
        List<PostListDto> result = getPostsView(cursorId, page).stream().map(postMapper::postToListResponse)
                .collect(Collectors.toList());
        Long lastIdx = result.isEmpty() ? null : result.get(result.size()-1).getPostIdx(); // 낮은 조회수 체크
        return new ApiPagingResultResponse<>(isNext(lastIdx), result);
    }



    // 회고글 카테고리 검색
//    public ApiPagingResultResponse<PostDto.ListResponse> getPostsListByContents(String category, String order, Long cursorId, Integer pageSize){
//        List<PostDto.ListResponse> result;
//        if (order.equals("recent")){
//            Post post = findRecentPost(cursorId);
//            result = postQueryRepository.findByCategory(cursorId, pageSize,post.getCreatedAt(), category); // 최신순+카테고리  검색
//        } else{
//            Post post = findViewPost(cursorId);
//            result = postQueryRepository.findByCategoryOrderByViewDesc(category,pageSize, post.getView()); // 조회순+ 카테고리 검색
//        }
//        Long lastIdx = result.isEmpty() ? null : result.get(result.size()-1).getPostIdx();
//
//        return new ApiPagingResultResponse<>(isNext(lastIdx), result);
//    }

    // 회고글 상세페이지
    public PostDto.detailResponse findPostContents(Long postIdx, Long userIdx){
        Post post = postRepository.findById(postIdx).orElseThrow(() -> new NullPointerException("해당 post_idx가 없습니다."));
        List<String> tag = tagRepository.findByPostPostIdx(postIdx)
                .stream()
                .map(Tag::getTag)
                .collect(Collectors.toList());
        boolean writer = userIdx != 0 && isWriter(post.getUser().getUserIdx(), userIdx);
        return new PostDto.detailResponse(post, tag, writer);
    }


    // 회고글 저장
    public Long inputPosts(PostDto.saveResponse saveResponse, Long userIdx){
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
        return post.getPostIdx();
    }


    // 회고글 수정
    public Long updatePosts(Long userIdx,Long postIdx, PostDto.updateRequest requestDto){
        // postIdx가 있는지 chk
        Post post = postRepository.findById(postIdx)
                .orElseThrow(()-> new IllegalArgumentException("해당 회고글이 없습니다."));
        // user 체크
        if (post.getUser().getUserIdx().equals(userIdx)) post.updatePost(requestDto);

        return post.getPostIdx();
    }


    // 회고글 삭제
    public boolean deletePosts(Long userIdx,Long postIdx) {
        Post post = postRepository.findById(postIdx)
                .orElseThrow(()-> new IllegalArgumentException("해당 회고글이 없습니다."));
        if (isWriter(post.getUser().getUserIdx(), userIdx)){
            postRepository.deleteById(postIdx);
            return true;
        }
        return false;
    }



    // 다음 페이지 여부 확인
    private boolean isNext(Long cursorId){
        if (cursorId == null) return false;
        return postRepository.existsByPostIdxLessThan(cursorId);
    }

    // 최신순 페이징
    private List<Post> getPostsRecent(Long cursorId, Pageable page) {
        return cursorId == null || cursorId == 0 ?
                postRepository.findAllByOrderByPostIdxDesc(page) : // 가장 최초 포스트
                postRepository.findRecent(cursorId, page, postRepository.findCreatedAtByPostIdx(cursorId).getCreatedAt());
    }

    // 누적순 페이징
    private List<Post> getPostsView(Long cursorId, Pageable page){
        return cursorId == null || cursorId == 0 ?
                postRepository.findAllByOrderByViewDesc(page) :
                postRepository.findView(postRepository.findViewByPostIdx(cursorId).getView(), page,postRepository.findCreatedAtByPostIdx(cursorId).getCreatedAt());
    }

    // 작성자 판별
    private boolean isWriter(Long postUserIdx,Long userIdx){
        return postUserIdx.equals(userIdx);
    }

}
