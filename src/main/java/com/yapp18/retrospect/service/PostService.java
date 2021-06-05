package com.yapp18.retrospect.service;

import com.yapp18.retrospect.domain.image.Image;
import com.yapp18.retrospect.domain.image.ImageRepository;
import com.yapp18.retrospect.domain.like.LikeRepository;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.tag.Tag;
import com.yapp18.retrospect.domain.tag.TagRepository;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.template.TemplateRepository;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.mapper.PostMapper;
import com.yapp18.retrospect.web.dto.ApiIsResultResponse;
import com.yapp18.retrospect.web.dto.ApiPagingResultResponse;
import com.yapp18.retrospect.web.dto.PostDto;
import com.yapp18.retrospect.web.dto.TagDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
//import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;
    private final TagRepository tagRepository;
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;
    private final PostMapper postMapper;

    // post idx 조회 나중에 method로 뺄 것

    // 최신순
    @Transactional(readOnly = true)
    public ApiPagingResultResponse<PostDto.ListResponse> getPostsListRecent(Long cursorId, Pageable page){
        List<PostDto.ListResponse> result = getPostsRecent(cursorId, page).stream().map(postMapper::postToListResponse)
                .collect(Collectors.toList());
        Long lastIdx = result.isEmpty() ? null : result.get(result.size()-1).getPostIdx(); // 낮은 idx 체크

        return new ApiPagingResultResponse<>(isNext(lastIdx),result);
    }

    // 조회순
    @Transactional(readOnly = true)
    public ApiPagingResultResponse<PostDto.ListResponse> getPostsListView(Long cursorId, Pageable page){
        List<PostDto.ListResponse> result = getPostsView(cursorId, page).stream().map(postMapper::postToListResponse)
                .collect(Collectors.toList());
        Long lastIdx = result.isEmpty() ? null : result.get(result.size()-1).getPostIdx(); // 낮은 조회수 체크
        return new ApiPagingResultResponse<>(isNext(lastIdx), result);
    }

    // 회고글 상세페이지
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ApiIsResultResponse<PostDto.detailResponse> findPostContents(Long postIdx, Long userIdx){
        Post post = postRepository.findById(postIdx).orElseThrow(() -> new NullPointerException("해당 post_idx가 없습니다."));
        post.updateview(post.getView()); // 조회수 증가
        return new ApiIsResultResponse<>(isWriter(post.getUser().getUserIdx(),userIdx),
                isScrap(post, userIdx),
                postMapper.postToDetailResponse(post)); // 작성자 판단
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
        saveTagList(saveResponse.getTag(), post);
        return post.getPostIdx();
    }


    // 회고글 수정
    @Transactional
    public Long updatePosts(Long userIdx,Long postIdx, PostDto.updateRequest requestDto){
        Post post = postRepository.findById(postIdx)
                .orElseThrow(()-> new IllegalArgumentException("해당 회고글이 없습니다."));
        List<String> tagList = post.getTagList().stream().map(Tag::getTag).collect(Collectors.toList()); // 기존 태그 목록

        // 수정할 tag 목록이 있고, 기존과 다른 내용이다. => tag 내용 수정해야함.
        if (!requestDto.getTagList().isEmpty() && !tagList.equals(requestDto.getTagList())){
            delTagList(compareList(tagList, requestDto.getTagList()), post); // 기존- 공통 = 삭제
            saveTagList(compareList(requestDto.getTagList(), tagList), post); // 새로운 - 공통 = 추가
        }
        if (post.getUser().getUserIdx().equals(userIdx)) post.updatePost(requestDto);
        return post.getPostIdx();
    }


    // 회고글 삭제
    @Transactional
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
    public boolean isNext(Long cursorId){
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
    private boolean isWriter(Long postUserIdx, Long userIdx){
        if (userIdx == 0L) return false;
        return postUserIdx.equals(userIdx);
    }

    // 스크랩 여부 판별
    private boolean isScrap(Post post, Long userIdx){
        return likeRepository.findByPostAndUserUserIdx(post, userIdx).isPresent();
    }

    // 리스트 비교
    private List<String> compareList(List<String> tagList, List<String> compareList){
        return tagList.stream().filter(x -> !compareList.contains(x)).collect(Collectors.toList());
    }

    // tag 저장
    private void saveTagList(List<String> tagList, Post post){
        if (!tagList.isEmpty()){ // tag가 있을 때만 저장
            for (String tag : tagList){
                tagRepository.save(Tag.builder().tag(tag).post(post).build());
            }
        }
    }

    private void delTagList(List<String> tagList, Post post){
        System.out.println(tagList);
        if (!tagList.isEmpty()){ // 삭제할 것이 있는 것의 tagIdx
            List<Long> result = post.getTagList().stream().filter(tag -> tagList.contains(tag.getTag())).map(Tag::getTagIdx).collect(Collectors.toList());
            System.out.println("삭제해야할 태그 인덱스"+result);
            tagRepository.deleteAllByTagIdxInQuery(result);
        }
    }


}
