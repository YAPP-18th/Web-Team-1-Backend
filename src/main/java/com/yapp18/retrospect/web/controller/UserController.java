package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.mapper.UserMapper;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.service.UserService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@Api(value = "UserController") // swagger 리소스 명시
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    @GetMapping("/find")
    public ResponseEntity<Object> findNickname(@ApiParam (value = "닉네임", required = true, example = "dok2")
                                               @RequestParam(value = "nickname") String nickname) throws UnsupportedEncodingException {
//        nickname = URLDecoder.decode(nickname, "UTF-8");
        boolean existNickname = userService.findUserByNickname(nickname);
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.NICKNAME_FIND.getResponseMessage(), existNickname), HttpStatus.OK);
    }

    @GetMapping("/profiles/{userIdx}")
    public ResponseEntity<Object> getProfiles(@PathVariable(value = "userIdx") Long userIdx){
        UserDto.ProfileResponse response = userService.getUserProfiles(userIdx);
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.PROFILE_FIND.getResponseMessage(), response), HttpStatus.OK);
    }

    @PatchMapping("/profiles")
    public ResponseEntity<Object> updateProfiles(HttpServletRequest request, @RequestBody UserDto.UpdateRequest updateRequest){
        String token = tokenService.getTokenFromRequest(request);
        Long userIdx = tokenService.getUserIdx(token);
        UserDto.ProfileResponse updated = userService.updateUserProfiles(userIdx, updateRequest);
        return new ResponseEntity<>(ApiDefaultResponse.res(200, ResponseMessage.PROFILE_UPDATE.getResponseMessage(), updated), HttpStatus.OK);
    }
}