package com.yapp18.retrospect.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
//@RequiredArgsConstructor
public enum ResponseMessage {
    // banner
    BANNER_GET("추천 배너 조회 성공"),
    // 회고글 crud
    POST_SAVE("회고글 저장 성공"),
    POST_UPDATE("회고글 수정 성공"),
    POST_FIND_RECENT("회고글 최신순 조회 성공"),
    POST_FIND_VIEW("회고글 누적조회순 조회 성공"),
    POST_FIND_CREATED("회고글 생성날짜순 조회 성공"),
    POST_DELETE("회고글 삭제 성공"),

    // 검색
    SEARCH_TITLE("제목으로 검색 성공"),
    SEARCH_CONTENTS("내용으로 검색 성공 "),
    SEARCH_ALL("전체로 검색 성공 "),

    POST_FIND_CATEGORY("카테고리 필터링 조회 성공"),
    POST_DETAIL("회고글 상세 조회 성공"),

    USER_FIND("사용자 정보 조회 성공"),
    USER_OWN_FIND("자신의 사용자 정보 조회 성공"),
    USER_UPDATE("사용자 정보 수정 성공"),
    USER_DELETE("사용자 삭제 성공"),

    MY_LIST("내가 쓴 글 조회 성공"),
    MY_RECENT_LIST("최근 읽은 글 조회 성공"),
    AUTH_ISSUE("Access Token 발급 성공"),
    AUTH_REISSUE("Access Token 재발급 성공"),
    TEMPLATE_FIND("템플릿 조회 성공 "),
    NICKNAME_FIND("닉네임 중복 여부 조회 성공 (중복됨 = true)"),

    COMMENT_DETAIL("댓글 상세 조회 성공"),
    COMMENT_SAVE("댓글 작성 성공"),
    COMMENT_UPDATE("댓글 수정 성공"),
    COMMENT_DELETE("댓글 삭제 성공"),
    COMMENT_FIND_POSTIDX("회고글에 달린 댓글 리스트 조회 성공"),
    COMMENT_COUNT_POSTIDX("회고글에 달린 댓글 갯수 조회 성공"),

    LIKE_FIND("스크랩 누적순 조회 성공"),
    LIKE_SAVE("스크랩 추가 성공"),
    LIKE_DELETE("스크랩 한 글 삭제 성공"),

    // 이미지 업로드
    IMAGE_UPLOAD("이미지 업로드 성공 ")
    ;

    private final String ResponseMessage;

    ResponseMessage(String ResponseMessage) {
        this.ResponseMessage = ResponseMessage;
    }

    public String getResponseMessage(){
        return this.ResponseMessage;
    }
}
