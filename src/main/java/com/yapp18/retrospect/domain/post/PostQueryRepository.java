package com.yapp18.retrospect.domain.post;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public class PostQueryRepository extends QuerydslRepositorySupport{

    private final JPAQueryFactory queryFactory;


    public PostQueryRepository(JPAQueryFactory queryFactory) {
        super(Post.class);
        this.queryFactory = queryFactory;
    }

    // 해시태그로 검색
    public List<Post> findAllByHashTag(String hashtag,Pageable page){
        QPost post = QPost.post;

        return queryFactory.select(post)
                .from(post).where(post.tagList.any().tag.eq(hashtag))
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    public List<Post> findCursorIdByHashTag(Long cursorId,Pageable page,String hashtag){
        QPost post = QPost.post;

        return queryFactory.select(post)
                .from(post).where(post.tagList.any().tag.eq(hashtag).and(post.postIdx.lt(cursorId)))
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .orderBy(post.createdAt.desc(), post.postIdx.desc())
                .fetch();
    }


    public List<Post> FirstSearch(String type,String query,String keyword,Pageable page){
        QPost post = QPost.post;
        BooleanBuilder builder = new BooleanBuilder();

        // 일반 검색 = 상세검색이 없고 category 이외의 type 검색.
        if (query == null && !type.equals("category")){
            if (type.equals("title")) builder.and(post.title.contains(keyword));
            if (type.equals("contents")) builder.and(post.contents.contains(keyword));
            if (type.equals("all")) builder.and(post.title.contains(keyword).or(post.contents.contains(keyword)));
        }

        // category 상세검색
        if (query != null && !type.equals("category")){
            if (type.equals("title")) builder.and(post.title.contains(keyword).and(post.category.in(query)));
            if (type.equals("contents")) builder.and(post.contents.contains(keyword)).and(post.category.in(query));
            if (type.equals("all")) builder.and(post.title.contains(keyword).or(post.contents.contains(keyword)).and(post.category.in(query)));
        }

        return queryFactory.select(post)
                .from(post).where(builder)
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .orderBy(post.createdAt.desc())
                .fetch();
    }

    public List<Post> Search(String type,String query,String keyword,Pageable page, Long cursorId){
        QPost post = QPost.post;
        BooleanBuilder builder = new BooleanBuilder();

        // 일반 검색 = 상세검색이 없고 category 이외의 type 으로 검색.
        if (query == null && !type.equals("category")){
            if (type.equals("title")) builder.and(post.title.contains(keyword));
            if (type.equals("contents")) builder.and(post.contents.contains(keyword));
            if (type.equals("all")) builder.and(post.title.contains(keyword).or(post.contents.contains(keyword)));
        }

        // 카테고리까지 상세검색
        if (query != null && !type.equals("category")){
            if (type.equals("title")) builder.and(post.title.contains(keyword).and(post.category.in(query)));
            if (type.equals("contents")) builder.and(post.contents.contains(keyword)).and(post.category.in(query));
            if (type.equals("all")) builder.and(post.title.contains(keyword).or(post.contents.contains(keyword)).and(post.category.in(query)));
        }

        return queryFactory.select(post)
                .from(post).where(builder.and(post.postIdx.lt(cursorId)))
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .orderBy(post.createdAt.desc(), post.postIdx.desc())
                .fetch();
    }




    //
//    // 통합검색
//    public List<Post> findAllByTitleFirst(String keyword, String type,Pageable page){
//        QPost post = QPost.post;
//        BooleanBuilder builder = new BooleanBuilder();
//
//        //post.tagList.any().tag.eq(hashtag)
//        if (type.equals("title")) builder.and(post.title.contains(keyword));
//        if (type.equals("contents")) builder.and(post.contents.contains(keyword));
//        if (type.equals("all")) builder.and(post.title.contains(keyword).or(post.contents.contains(keyword)));
//
//
//        return queryFactory.select(post)
//                .from(post).where(builder)
//                .offset(page.getOffset())
//                .limit(page.getPageSize())
//                .orderBy(post.createdAt.desc())
//                .fetch();
//    }


//    public List<Post> findAllByTitle(String keyword, String type, Long cursorId, Pageable page, LocalDateTime createdAt){
//        QPost post = QPost.post;
//        BooleanBuilder builder = new BooleanBuilder();
//
//        if (type.equals("title")) builder.and(post.title.contains(keyword));
//        if (type.equals("contents")) builder.and(post.contents.contains(keyword));
//        if (type.equals("all")) builder.and(post.title.contains(keyword).or(post.contents.contains(keyword)));
//
//
//        return queryFactory.select(post)
//                .from(post).where(builder.and((post.createdAt.eq(createdAt)).and(post.postIdx.lt(cursorId))).or(post.createdAt.lt(createdAt)))
//                .offset(page.getOffset())
//                .limit(page.getPageSize())
//                .orderBy(post.createdAt.desc(), post.postIdx.desc())
//                .fetch();
//    }

}
