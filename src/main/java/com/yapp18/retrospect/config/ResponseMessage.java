package com.yapp18.retrospect.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
//@RequiredArgsConstructor
public enum ResponseMessage {
    // 회고글 crud
    POST_SAVE("회고글 저장 성공"),
    POST_UPDATE("회고글 수정 성공"),
    POST_FIND_RECENT("회고글 최신순 조회 성공"),
    POST_FIND("회고글 누적조회순 조회 성공"),
    POST_DELETE("회고글 삭제 성공"),

    // 검색
    SEARCH_TITLE("제목으로 검색 성공"),
    SEARCH_CONTENTS("내용으로 검색 성공 "),
    SEARCH_ALL("전체로 검색 성공 "),

    POST_FIND_CATEGORY("카테고리 필터링 조회 성공"),
    POST_DETAIL("회고글 상세 조회 성공"),

    PROFILE_FIND("프로필 조회 성공"),
    PROFILE_UPDATE("프로필 업데이트 성공"),
    MY_LIST("내가 쓴 글 조회 성공"),
    AUTH_ISSUE("Access Token 발급 성공"),
    AUTH_REISSUE("Access Token 재발급 성공"),
    TEMPLATE_FIND("템플릿 조회 성공 "),
    NICKNAME_FIND("닉네임 중복 여부 조회 성공 (중복됨 = true)"),

    COMMENT_FIND_POSTIDX("회고 글에 작성된 댓글 조회 성공");

    private final String ResponseMessage;

    ResponseMessage(String ResponseMessage) {
        this.ResponseMessage = ResponseMessage;
    }

    public String getResponseMessage(){
        return this.ResponseMessage;
    }
}
