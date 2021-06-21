package com.yapp18.retrospect.service;

import com.yapp18.retrospect.config.AppProperties;
import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.domain.image.Image;
import com.yapp18.retrospect.domain.image.ImageRepository;
import com.yapp18.retrospect.domain.like.LikeRepository;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.post.PostRepository;
import com.yapp18.retrospect.domain.tag.Tag;
import com.yapp18.retrospect.domain.template.Template;
import com.yapp18.retrospect.domain.template.TemplateRepository;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.mapper.PostMapper;
import com.yapp18.retrospect.web.advice.EntityNullException;
import com.yapp18.retrospect.web.dto.ApiIsResultResponse;
import com.yapp18.retrospect.web.dto.ApiPagingResultResponse;
import com.yapp18.retrospect.web.dto.PostDto;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
//import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final TagService tagService;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;
    private final PostMapper postMapper;
    private final ImageService imageService;
    private final ListService listService;

    @Value("${app.values.s3PostImagePathSuffix}")
    public String s3PostImagePathSuffix;
//    @Value("${app.values.s3ProfileImagePathSuffix}")
//    public String s3ProfileImagePathSuffix;

    // post idx 조회 나중에 method로 뺄 것

    // 최신순
//    @Transactional(readOnly = true)
//    public ApiPagingResultResponse<PostDto.ListResponse> getPostsListRecent(Long cursorId, Pageable page, Long userIdx){
//        List<PostDto.ListResponse> result = getPostsRecent(cursorId, page).stream().map(post -> postMapper.postToListResponse(post, userIdx))
//                .collect(Collectors.toList());
//        Long lastIdx = result.isEmpty() ? null : result.get(result.size()-1).getPostIdx(); // 낮은 idx 체크
//
//        return new ApiPagingResultResponse<>(isNext(lastIdx),result);
//    }

    // 조회순
    @Transactional(readOnly = true)
    public ApiPagingResultResponse<PostDto.ListResponse> getPostsListView(Long cursorId, Pageable page, Long userIdx){
        List<PostDto.ListResponse> result = getPostsView(cursorId, page).stream().map(post->postMapper.postToListResponse(post, userIdx))
                .collect(Collectors.toList());
        Long lastIdx = result.isEmpty() ? null : result.get(result.size()-1).getPostIdx(); // 낮은 조회수 체크
        return new ApiPagingResultResponse<>(isNext(lastIdx), result);
    }

    @Transactional(readOnly = true)
    public ApiPagingResultResponse<PostDto.ListResponse> getPostsListCreatedAt(Long cursorIdx, Long userIdx, Pageable pageable){
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));

        List<Post> postList = cursorIdx == 0 || cursorIdx == null ?
                postRepository.findByUserOrderByCreatedAtDesc(user, pageable)
                : postRepository.cursorFindByUserOrderByCreatedAtDesc(cursorIdx, user.getUserIdx(), pageable);

        Long lastIdx = postList.isEmpty() ? null : postList.get(postList.size() - 1).getPostIdx(); // 낮은 조회수 체크

        List<PostDto.ListResponse> result = postList.stream()
                .map(post -> postMapper.postToListResponse(post, userIdx))
                .collect(Collectors.toList());

        return new ApiPagingResultResponse<>(isNext(user, lastIdx), result);
    }


//    // 회고글 카테고리 검색
//    public ApiPagingResultResponse<PostDto.ListResponse> getPostsByCategory(String query, Long cursorId, Pageable page, Long userIdx){
//        List<PostDto.ListResponse> result = getPostCategory(cursorId, page, query).stream().map(post->postMapper.postToListResponse(post, userIdx))
//                .collect(Collectors.toList());
//        Long lastIdx = result.isEmpty() ? null : result.get(result.size()-1).getPostIdx(); // 낮은 idx 체크
//        return new ApiPagingResultResponse<>(isNext(lastIdx),result);
//    }

    // 회고글 상세페이지
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ApiIsResultResponse<PostDto.detailResponse> findPostContents(Long postIdx, Long userIdx){
        Post post = postRepository.findById(postIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.POST_NULL));
        post.updateview(post.getView()); // 조회수 증가
        if(userIdx != 0L){
            listService.saveRecentReadPosts(userIdx, postIdx); // 최근 읽은 글에 추가
        }
        return new ApiIsResultResponse<>(isWriter(post.getUser().getUserIdx(),userIdx),
                isScrap(post, userIdx),
                postMapper.postToDetailResponse(post)); // 작성자 판단
    }


    // 회고글 저장
    public Long inputPosts(PostDto.saveResponse saveResponse, Long userIdx){
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));

        Template template = templateRepository.findById(saveResponse.getTemplateIdx())
                .orElseThrow(() -> new EntityNullException(ErrorInfo.TEMPLATE_NULL));

        Post post = postRepository.save(saveResponse.toEntity(user, template));

        if (!saveResponse.getImageList().isEmpty()){
            System.out.println("--> imageList "+saveResponse.getImageList() +
                    "-----> s3:"+ imageService.getFileList(userIdx, s3PostImagePathSuffix));
            if (!saveResponse.getImageList().equals(
                    imageService.getFileList(userIdx, s3PostImagePathSuffix))){ // 일치하지 않을 때만
                imageService.deleteImageList(saveResponse.getImageList(), userIdx, s3PostImagePathSuffix); // s3에 불필요한 이미지 제거
            }
            // imageList 있을 때만 db에 저장
            for (String url : saveResponse.getImageList()) {
                imageRepository.save(Image.builder().imageUrl(url).post(post).build());
            }
        }
        // tag 저장
        if (!saveResponse.getTagList().isEmpty()){
            tagService.saveTagList(saveResponse.getTagList(), post);
        }
        return post.getPostIdx();
    }


    // 회고글 수정
    @Transactional
    public Long updatePosts(Long userIdx,Long postIdx, PostDto.updateRequest requestDto){
        Post post = postRepository.findById(postIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.POST_NULL));
        List<String> tagList = post.getTagList().stream().map(Tag::getTag).collect(Collectors.toList()); // 기존 태그 목록
        List<String> dbImageList = post.getImageList().stream().map(Image::getImageUrl).collect(Collectors.toList()); // 기존 이미지 목록

        System.out.println("기존 디비 이미지"+ dbImageList +" 수정 시 들어온 이미지" + requestDto.getImageList()); // 둘 다 http붙여서.

        // 수정할 tag 목록이 있고, 기존과 다른 내용이다. => tag 내용 수정해야함.
        if (!requestDto.getTagList().isEmpty() && !tagList.equals(requestDto.getTagList())){
            tagService.delTagList(compareList(tagList, requestDto.getTagList()), post); // 기존- 공통 = 삭제
            tagService.saveTagList(compareList(requestDto.getTagList(), tagList), post); // 새로운 - 공통 = 추가
        }

        // db와 이미지 리스트가 다르다 -> 수정사항이 있다.
        if (!requestDto.getImageList().isEmpty() && !dbImageList.equals(requestDto.getImageList())){
            imageService.updateNewImages(requestDto.getImageList(), dbImageList, post); // imageList에만 있는 것은 add 추가
            imageService.deleteDbImage(requestDto.getImageList(), dbImageList); // dbList에만 있는 것은 삭제

            if (!requestDto.getImageList().equals(
                    imageService.getFileList(userIdx, s3PostImagePathSuffix))){ // imageList != s3List
                imageService.deleteImageList(requestDto.getImageList(), userIdx, s3PostImagePathSuffix); // s3에 불필요한 이미지 제거
            }

        }

        if (post.getUser().getUserIdx().equals(userIdx)) post.updatePost(requestDto);
        return post.getPostIdx();
    }


    // 회고글 삭제
    @Transactional
    public boolean deletePosts(Long userIdx,Long postIdx) {
        Post post = postRepository.findById(postIdx)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.POST_NULL));
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

    // 다음 페이지 여부 확인
    public boolean isNext(User user, Long cursorId){
        if (cursorId == null) return false;
        return postRepository.existsByUserAndPostIdxLessThan(user, cursorId);
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

    // 스크랩 여부 판별 -> 리팩토링 필요
    private boolean isScrap(Post post, Long userIdx){
        return likeRepository.findByPostAndUserUserIdx(post, userIdx).isPresent();
    }

    // 리스트 비교
    private List<String> compareList(List<String> newList, List<String> compareList){
        return newList.stream().filter(x -> !compareList.contains(x)).collect(Collectors.toList());
    }

}
