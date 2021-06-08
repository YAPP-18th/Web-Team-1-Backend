package com.yapp18.retrospect.domain.post;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp18.retrospect.domain.tag.QTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


//@RequiredArgsConstructor
@Repository
public class PostQueryRepository extends QuerydslRepositorySupport{

    private final JPAQueryFactory queryFactory;


    public PostQueryRepository(JPAQueryFactory queryFactory) {
        super(Post.class);
        this.queryFactory = queryFactory;
    }

    public List<Post> findAllByTitleFirst(String keyword, String type,Pageable page){
        QPost post = QPost.post;
        BooleanBuilder builder = new BooleanBuilder();

        if (type.equals("title")) builder.and(post.title.contains(keyword));
        if (type.equals("contents")) builder.and(post.contents.contains(keyword));
        if (type.equals("all")) builder.and(post.title.contains(keyword).or(post.contents.contains(keyword)));


        return queryFactory.select(post)
                .from(post).where(builder)
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    // 검색
    public List<Post> findAllByTitle(String keyword, String type, Long cursorId, Pageable page, LocalDateTime createdAt){
        QPost post = QPost.post;
        BooleanBuilder builder = new BooleanBuilder();

        if (type.equals("title")) builder.and(post.title.contains(keyword));
        if (type.equals("contents")) builder.and(post.contents.contains(keyword));
        if (type.equals("all")) builder.and(post.title.contains(keyword).or(post.contents.contains(keyword)));


        return queryFactory.select(post)
                .from(post).where(builder.and((post.createdAt.eq(createdAt)).and(post.postIdx.lt(cursorId))).or(post.createdAt.lt(createdAt)))
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .orderBy(post.createdAt.desc(), post.postIdx.desc())
                .fetch();
    }

    // 해시태그로 검색
    public List<Post> findAllByHashTag(String hashtag,Pageable page){
        QPost post = QPost.post;
        QTag tag = QTag.tag1;

        return queryFactory.select(post)
                .from(post).where(post.tagList.any().tag.eq(hashtag))
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    public List<Post> findCursorIdByHashTag(Long cursorId,Pageable page,String hashtag,LocalDateTime createdAt){
        QPost post = QPost.post;

        return queryFactory.select(post)
                .from(post).where(post.tagList.any().tag.eq(hashtag).and((post.createdAt.eq(createdAt)).and(post.postIdx.lt(cursorId))).or(post.createdAt.lt(createdAt)))
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .orderBy(post.createdAt.desc(), post.postIdx.desc())
                .fetch();
    }


}
