package wheresoever.quickprotoserver.repository.postlike;

import com.querydsl.core.QueryFactory;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Projection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import wheresoever.quickprotoserver.domain.Post;
import wheresoever.quickprotoserver.domain.QMember;
import wheresoever.quickprotoserver.domain.QPostLike;
import wheresoever.quickprotoserver.dto.PostLikeDto;


import java.util.List;

import static wheresoever.quickprotoserver.domain.QPostLike.postLike;

@RequiredArgsConstructor
public class PostLikeRepositoryImpl implements PostLikeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostLikeDto> findAllAsDtoByPost(Post post, Pageable pageable) {
        QueryResults<PostLikeDto> results = queryFactory
                .select(Projections.constructor(PostLikeDto.class,
                        postLike.member.id,
                        postLike.member.nickname
                ))
                .from(postLike)
                .where(postLike.post.eq(post))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(postLike.at.desc())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
