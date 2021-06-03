package com.yapp18.retrospect.domain.like;

import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostAndUserUserIdx(Post post, Long UserIdx);
}