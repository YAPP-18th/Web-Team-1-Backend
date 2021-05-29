package com.yapp18.retrospect.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.yapp18.retrospect.domain.tag.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
//@AllArgsConstructor
@Getter
public class SearchDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "회고글 목록 조회(조회순)", description = "회고글 목록 조회 모델")
    // post 조회
    public static class ListResponse{
        // postIdx 넘겨줘야함.
        @ApiModelProperty(value = "회고글 idx")
        private Long postIdx;

        @ApiModelProperty(value = "회고글 제목")
        private String title;

        @ApiModelProperty(value = "카테고리")
        private String category;

        @ApiModelProperty(value = "내용 ")
        private String contents;

        @ApiModelProperty(value = "작성자 닉네임 ")
        private String nickname;

        @ApiModelProperty(value = "작성자 프로필 사진")
        private String profile;

        @ApiModelProperty(value = "키워드 ")
        private List<Tag> tag = new ArrayList<>();

        @ApiModelProperty(value = "조회수")
        private int view;

        @JsonFormat(pattern = "MMM dd, yyyy", locale = "en_GB")
        @ApiModelProperty(value = "생성날짜")
        private LocalDateTime created_at;

        @ApiModelProperty(value = "댓글 수")
        private Long commentCnt;

        @ApiModelProperty(value = "스크랩 수 ")
        private Long scrapCnt;

        @QueryProjection
        public ListResponse(Long postIdx,String title, String category, String contents, String nickname,
                            String profile,
//                            List<Tag>  tag,
                            LocalDateTime created_at, int view,
                            Long commentCnt, Long scrapCnt){
            this.postIdx = postIdx;
            this.title =  title;
            this.category = category;
            this.contents = contents;
            this.nickname = nickname;
            this.profile = profile;
//            this.tag = tag;
            this.created_at = created_at;
            this.view = view;
            this.commentCnt = commentCnt;
            this.scrapCnt = scrapCnt;

        }

    }

    @Data
    @NoArgsConstructor
    @ApiModel(value = "회고글 목록 조회(조회순)", description = "회고글 목록 조회 모델")
    // post 조회
    public static class SearchListResponse{
        // postIdx 넘겨줘야함.
        @ApiModelProperty(value = "회고글 idx")
        private Long postIdx;

        @ApiModelProperty(value = "회고글 제목")
        private String title;

        @ApiModelProperty(value = "카테고리")
        private String category;

        @ApiModelProperty(value = "내용 ")
        private String contents;

        @ApiModelProperty(value = "작성자 닉네임 ")
        private String nickname;

        @ApiModelProperty(value = "작성자 프로필 사진")
        private String profile;

        @ApiModelProperty(value = "키워드 ")
        private List<String> tag = new ArrayList<>();

        @ApiModelProperty(value = "조회수")
        private int view;

        @JsonFormat(pattern = "MMM dd, yyyy", locale = "en_GB")
        @ApiModelProperty(value = "생성날짜")
        private LocalDateTime created_at;

        @ApiModelProperty(value = "댓글 수")
        private Long commentCnt;

        @ApiModelProperty(value = "스크랩 수 ")
        private Long scrapCnt;

        @Builder
        public SearchListResponse(SearchDto.ListResponse result, List<String> tag){
            this.postIdx = result.getPostIdx();
            this.title =  result.getTitle();
            this.category = result.getCategory();
            this.contents = result.getContents();
            this.nickname = result.getNickname();
            this.profile = result.getProfile();
            this.tag = tag;
            this.created_at = result.getCreated_at();
            this.view = result.getView();
            this.commentCnt = result.getCommentCnt();
            this.scrapCnt = result.getScrapCnt();

        }



    }
}
