package com.yapp18.retrospect.web.controller;

import com.yapp18.retrospect.config.ResponseMessage;
import com.yapp18.retrospect.service.TokenService;
import com.yapp18.retrospect.service.UserService;
import com.yapp18.retrospect.web.dto.ApiDefaultResponse;
import com.yapp18.retrospect.web.dto.UserDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(value = "UserController") // swagger 리소스 명시
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    @GetMapping("/profiles")
    public ResponseEntity<Object> getProfiles(HttpServletRequest request){
        String token = tokenService.getTokenFromRequest(request);
        Long userIdx = tokenService.getUserIdx(token);
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