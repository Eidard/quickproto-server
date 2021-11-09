package wheresoever.quickprotoserver.repository.post;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import wheresoever.quickprotoserver.domain.Post;
import wheresoever.quickprotoserver.domain.QMember;

import java.util.Optional;

import static wheresoever.quickprotoserver.domain.QComment.comment;
import static wheresoever.quickprotoserver.domain.QCommentChild.commentChild;
import static wheresoever.quickprotoserver.domain.QMember.member;
import static wheresoever.quickprotoserver.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Post> findPostDetailById(Long postId) {


        return Optional.ofNullable(
                queryFactory
                        .selectFrom(post)
                        .where(post.id.eq(postId))
                        .join(post.comments, comment).fetchJoin()
                        .join(comment.commentChildren, commentChild).fetchJoin()
                        //.orderBy(comment.id.asc())
                        //.orderBy((commentChild.id.asc()))
                        .fetchOne());

        /*
        *  진짜 ㄹㅇ로 "RDB에서의 테이블 -> JPA의 객체" 변환 방식 그림으로 설명 필요함
        * Post - comment를 List로, Comment - commentChildren을 Set으로 변경하면
        * 대댓글의 숫자만큼 댓글이 post에 추가로 달림
        * 댓글 1개에 대댓글 4개가 붙어있다면, 대댓글 4개 달린 댓글이 4개로 증가되어 post에 붙었음
       * */
    }

}
