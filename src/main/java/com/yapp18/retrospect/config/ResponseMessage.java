package com.yapp18.retrospect.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
//@RequiredArgsConstructor
public enum ResponseMessage {
    POST_SAVE("회고글 저장 성공"),
    POST_UPDATE("회고글 수정 성공"),
    POST_FIND_RECENT("회고글 최신순 조회 성공"),
    POST_FIND("회고글 누적조회순 조회 성공"),
    POST_DELETE("회고글 삭제 성공");

    private final String ResponseMessage;

    ResponseMessage(String ResponseMessage) {
        this.ResponseMessage = ResponseMessage;
    }

    public String getResponseMessage(){
        return this.ResponseMessage;
    }
}
