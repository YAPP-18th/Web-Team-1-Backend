package com.yapp18.retrospect.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yapp18.retrospect.domain.tag.Tag;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
//@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostListDto {

    @ApiModelProperty(value = "회고글 idx")
    private Long postIdx;

    @ApiModelProperty(value = "회고글 제목")
    private String title;

    @ApiModelProperty(value = "카테고리")
    private String category;

    @ApiModelProperty(value = "내용")
    private String contents;

    @ApiModelProperty(value = "작성자 닉네임 ")
    private String nickname;

    @ApiModelProperty(value = "작성자 프로필 사진")
    private String profile;

    @ApiModelProperty(value = "키워드 ")
    private List<TagDto> tagList;

    @ApiModelProperty(value = "조회수")
    private int view;

    @JsonFormat(pattern = "MMM dd, yyyy", locale = "en_GB")
    @ApiModelProperty(value = "생성날짜")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "댓글 수")
    private Long commentCnt;

    @ApiModelProperty(value = "스크랩 수 ")
    private Long scrapCnt;

}
