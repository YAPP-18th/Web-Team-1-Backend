package com.yapp18.retrospect.domain.user;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.yapp18.retrospect.annotation.RetrospectDataTest;
import com.yapp18.retrospect.config.ErrorInfo;
import com.yapp18.retrospect.web.advice.EntityNullException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@RetrospectDataTest
@DatabaseSetup({
        "classpath:dbunit/entity/user.xml"
})
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void 멤버_조회(){
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new EntityNullException(ErrorInfo.USER_NULL));

        assertThat(user.getUserIdx()).isEqualTo(1L);
    }
}
