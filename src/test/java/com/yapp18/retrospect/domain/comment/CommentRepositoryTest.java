package com.yapp18.retrospect.domain.comment;

import com.yapp18.retrospect.annotation.RetrospectDataTest;
import com.yapp18.retrospect.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

@RetrospectDataTest
public class CommentRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;
}