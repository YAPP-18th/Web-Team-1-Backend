package com.yapp18.retrospect.service;

import com.yapp18.retrospect.domain.user.User;
import com.yapp18.retrospect.domain.user.UserRepository;
import com.yapp18.retrospect.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + "로 된 사용자를 찾을 수 없습니다."));

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserByUserIdx(Long userIdx) throws UsernameNotFoundException {
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new UsernameNotFoundException(userIdx + "인덱스를 갖는 사용자를 찾을 수 없습니다."));

        return UserPrincipal.create(user);
    }
}
