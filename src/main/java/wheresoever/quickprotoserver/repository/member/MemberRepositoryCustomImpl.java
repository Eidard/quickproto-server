package wheresoever.quickprotoserver.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wheresoever.quickprotoserver.domain.Post;
import wheresoever.quickprotoserver.domain.QComment;
import wheresoever.quickprotoserver.domain.QCommentChild;
import wheresoever.quickprotoserver.domain.QPost;

import static wheresoever.quickprotoserver.domain.QComment.comment;
import static wheresoever.quickprotoserver.domain.QCommentChild.commentChild;
import static wheresoever.quickprotoserver.domain.QPost.post;


public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

}
