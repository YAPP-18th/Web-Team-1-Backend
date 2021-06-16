package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.service.UserService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@Api(value = "UserController") // swagger 리소스 명시
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    @ApiOperation(value = "user", notes = "[사용자] 등록된 닉네임 조회") // api tag, 설명
    @GetMapping("")
    public ResponseEntity<Object> findNickname(@ApiParam (value = "닉네임", required = true, example = "dok2")
                                               @RequestParam(value = "nickname") String nickname) throws UnsupportedEncodingException {
        return new ResponseEntity<>(ApiDefaultResponse.res(200,
                ResponseMessage.NICKNAME_FIND.getResponseMessage(),
                userService.findUserByNickname(nickname)), HttpStatus.OK);
    }

    @ApiOperation(value = "profile", notes = "[프로필] 사용자 정보 조회") // api tag, 설명
    @GetMapping("/profiles/{userIdx}")
    public ResponseEntity<Object> getProfiles(@ApiParam (value = "사용자 user_idx", required = true, example = "1557")
                                                  @PathVariable(value = "userIdx") Long userIdx){
        UserDto.ProfileResponse response = userService.getUserProfiles(userIdx);
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.USER_FIND.getResponseMessage(), response), HttpStatus.OK);
    }

    @ApiOperation(value = "profile", notes = "[프로필] 사용자 정보 수정") // api tag, 설명
    @PatchMapping("/profiles")
    public ResponseEntity<Object> updateProfiles(HttpServletRequest request, @RequestBody UserDto.UpdateRequest updateRequest){
        Long userIdx = tokenService.getUserIdx(tokenService.getTokenFromRequest(request));
        return new ResponseEntity<>(ApiDefaultResponse.res(201,
                        ResponseMessage.USER_UPDATE.getResponseMessage(),
                        userService.updateUserProfiles(userIdx, updateRequest)),
                HttpStatus.CREATED);
    }

    @ApiOperation(value = "user", notes = "[사용자] 사용자 삭제(탈퇴)") // api tag, 설명
    @DeleteMapping("/profiles")
    public ResponseEntity<Object> deleteProfiles(HttpServletRequest request){
        Long userIdx = tokenService.getUserIdx(tokenService.getTokenFromRequest(request));
        userService.deleteUser(userIdx);
        return new ResponseEntity<>(ApiDefaultResponse.res(204, ResponseMessage.USER_DELETE.getResponseMessage()), HttpStatus.NO_CONTENT);
    }
}