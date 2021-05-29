package com.yapp18.retrospect.domain.post;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp18.retrospect.domain.comment.QComment;
import com.yapp18.retrospect.domain.like.QLike;
import com.yapp18.retrospect.domain.tag.QTag;
import com.yapp18.retrospect.domain.tag.Tag;
import com.yapp18.retrospect.domain.user.QUser;
import com.yapp18.retrospect.web.dto.PostDto;
import com.yapp18.retrospect.web.dto.QPostDto_ListResponse;
import com.yapp18.retrospect.web.dto.QSearchDto_ListResponse;
import com.yapp18.retrospect.web.dto.SearchDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

//@RequiredArgsConstructor
@Repository
public class PostQueryRepository extends QuerydslRepositorySupport{

    private final JPAQueryFactory queryFactory;


    public PostQueryRepository(JPAQueryFactory queryFactory) {
        super(Post.class);
        this.queryFactory = queryFactory;
    }


    // 최신순 페이징
    public List<PostDto.ListResponse> findByPostIdx(Long cursorId, Integer pageSize, LocalDateTime create_at){
        QPost post = QPost.post;
        QUser user = QUser.user;
        QTag tag = QTag.tag1;
        QComment comment = QComment.comment1;
        QLike like = QLike.like;

        return queryFactory
                .select(new QPostDto_ListResponse(post.postIdx, post.title, post.category, post.contents,
                        user.nickname, user.profile, tag.tag, post.created_at, post.view,
                        comment.post.postIdx.count().as("commentCnt"), like.post.postIdx.count().as("scrapCnt")))
                .distinct()
                .from(post)
                .leftJoin(user).on(post.user.userIdx.eq(user.userIdx))
                .leftJoin(post.tag).fetchJoin()
                .leftJoin(comment).on(post.postIdx.eq(comment.post.postIdx))
                .leftJoin(like).on(post.postIdx.eq(like.post.postIdx))
                .where((post.created_at.eq(create_at).and(post.postIdx.lt(cursorId))).or(post.created_at.lt(create_at)))
                .orderBy(post.created_at.desc(),post.postIdx.desc()) // 조회순으로 바꿔야함.
                .limit(pageSize)
                .groupBy(post, user, tag, comment, like)
                .fetch();


    }

//    public List<Order> findAllByUserName(String userName) {
//        return from(order)
//                .join(order.orderItems, orderItem).fetchJoin()
//                .where(order.userName.eq(userName))
//                .distinct()
//                .fetch();
//    }

    // 누적 조회수
    public List<PostDto.ListResponse> findByPostIdxOrderByViewDesc(Integer pageSize, int view){
        QPost post = QPost.post;
        QUser user = QUser.user;
        QTag tag = QTag.tag1;
        QComment comment = QComment.comment1;
        QLike like = QLike.like;

        return queryFactory
                .select(new QPostDto_ListResponse(post.postIdx, post.title, post.category, post.contents,
                        user.nickname, user.profile, tag.tag, post.created_at, post.view,
                        comment.post.postIdx.count().as("commentCnt"), like.post.postIdx.count().as("scrapCnt")))
                .from(post)
                .leftJoin(user).on(post.user.userIdx.eq(user.userIdx))
                .leftJoin(tag).on(post.postIdx.eq(tag.post.postIdx))
                .leftJoin(comment).on(post.postIdx.eq(comment.post.postIdx))
                .leftJoin(like).on(post.postIdx.eq(like.post.postIdx))
                .where(post.view.loe(view))
                .orderBy(post.view.desc())
                .limit(pageSize)
                .distinct()
                .groupBy(post, user, tag, comment, like)
                .fetch().stream().distinct().collect(Collectors.toList());
    }

    // 최신순 카테고리 페이징
    public List<PostDto.ListResponse> findByCategory(Long cursorId, Integer pageSize, LocalDateTime create_at,
                                                     String category){
        QPost post = QPost.post;
        QUser user = QUser.user;
        QTag tag = QTag.tag1;
        QComment comment = QComment.comment1;
        QLike like = QLike.like;

        return queryFactory
                .select(new QPostDto_ListResponse(post.postIdx, post.title, post.category, post.contents,
                        user.nickname, user.profile, tag.tag, post.created_at, post.view,
                        comment.post.postIdx.count().as("commentCnt"), like.post.postIdx.count().as("scrapCnt")))
                .from(post).where(post.category.eq(category))
                .leftJoin(user).on(post.user.userIdx.eq(user.userIdx))
                .leftJoin(tag).on(post.postIdx.eq(tag.post.postIdx))
                .leftJoin(comment).on(post.postIdx.eq(comment.post.postIdx))
                .leftJoin(like).on(post.postIdx.eq(like.post.postIdx))
                .where((post.created_at.eq(create_at).and(post.postIdx.lt(cursorId)))
                        .or(post.created_at.lt(create_at)))
                .orderBy(post.created_at.desc(),post.postIdx.desc()) // 조회순으로 바꿔야함.
                .limit(pageSize)
                .groupBy(post, user, tag, comment, like)
                .fetch();
    }

    // 누적 조회수 카테고리
    public List<PostDto.ListResponse> findByCategoryOrderByViewDesc(String category,Integer pageSize, int view){
        QPost post = QPost.post;
        QUser user = QUser.user;
        QTag tag = QTag.tag1;
        QComment comment = QComment.comment1;
        QLike like = QLike.like;

        return queryFactory
                .select(new QPostDto_ListResponse(post.postIdx, post.title, post.category, post.contents,
                        user.nickname, user.profile, tag.tag, post.created_at, post.view,
                        comment.post.postIdx.count().as("commentCnt"), like.post.postIdx.count().as("scrapCnt")))
                .from(post).where(post.category.eq(category)).orderBy(post.view.desc())
                .leftJoin(user).on(post.user.userIdx.eq(user.userIdx))
                .leftJoin(tag).on(post.postIdx.eq(tag.post.postIdx))
                .leftJoin(comment).on(post.postIdx.eq(comment.post.postIdx))
                .leftJoin(like).on(post.postIdx.eq(like.post.postIdx))
                .where(post.view.loe(view))
                .orderBy(post.view.desc())
                .limit(pageSize)
                .groupBy(post, user, tag, comment, like)
                .fetch();
    }

    // 내 회고글
    public List<PostDto.ListResponse> findAllByUserUserIdx(Long userIdx ,Long page, Integer pageSize, LocalDateTime create_at){
        QPost post = QPost.post;
        QUser user = QUser.user;
        QTag tag = QTag.tag1;
        QComment comment = QComment.comment1;
        QLike like = QLike.like;

        return queryFactory
                .select(new QPostDto_ListResponse(post.postIdx, post.title, post.category, post.contents,
                        user.nickname, user.profile, tag.tag, post.created_at, post.view,
                        comment.post.postIdx.count().as("commentCnt"), like.post.postIdx.count().as("scrapCnt")))
                .from(post).where(post.user.userIdx.eq(userIdx))
                .leftJoin(user).on(post.user.userIdx.eq(user.userIdx))
                .leftJoin(tag).on(post.postIdx.eq(tag.post.postIdx))
                .leftJoin(comment).on(post.postIdx.eq(comment.post.postIdx))
                .leftJoin(like).on(post.postIdx.eq(like.post.postIdx))
                .where((post.created_at.eq(create_at).and(post.postIdx.lt(page)))
                        .or(post.created_at.lt(create_at)))
                .orderBy(post.created_at.desc(),post.postIdx.desc()) // 조회순으로 바꿔야함.
                .limit(pageSize)
                .groupBy(post, user, tag, comment, like)
                .fetch();
    }


    // 내 회고글
    public List<SearchDto.ListResponse> findAllByTitle(String title){
        QPost post = QPost.post;
        QUser user = QUser.user;
        QComment comment = QComment.comment1;
        QLike like = QLike.like;

        List<SearchDto.ListResponse> result =  queryFactory
                .select(new QSearchDto_ListResponse(post.postIdx, post.title, post.category, post.contents,
                        user.nickname, user.profile,post.created_at, post.view,
                        comment.post.postIdx.count().as("commentCnt"), like.post.postIdx.count().as("scrapCnt")))
                .from(post).where(post.title.contains(title))
                .leftJoin(user).on(post.user.userIdx.eq(user.userIdx))
                .leftJoin(post.tag)
                .leftJoin(comment).on(post.postIdx.eq(comment.post.postIdx))
                .leftJoin(like).on(post.postIdx.eq(like.post.postIdx))
                .groupBy(post, user, comment, like)
                .fetch();
        System.out.println(result.get(0).getTag());
        return result;
    }




}
