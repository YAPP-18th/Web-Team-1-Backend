package com.yapp18.retrospect.service;

import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
//    private final UserRepository userRepository;
//
//    public User findByEmail(String email){
//
//    }
//
//    public UserDto.Response findByEmail(String email){
//        User entity = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new IllegalArgumentException("이메일에 해당하는 유저가 없습니다. email =" + email)); // 추후에 AOP 처리
//        return new UserDto.Response(entity);
//    }
}
